package com.moodly.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moodly.app.data.db.MoodDatabase
import com.moodly.app.data.model.MoodEntry
import com.moodly.app.data.model.MoodLevel
import com.moodly.app.data.model.MoodTag
import com.moodly.app.data.repository.MoodRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * DECISIÓN TÉCNICA - MVVM con StateFlow:
 * - StateFlow en lugar de LiveData: es 100% Kotlin, no requiere dependencia de lifecycle-livedata.
 * - stateIn con SharingStarted.WhileSubscribed(5000): cancela la colección cuando no hay
 *   observadores, pero mantiene el estado 5 seg para evitar recargas en rotaciones de pantalla.
 * - AndroidViewModel para acceder al Application context y crear la DB sin leaks.
 */
class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoodRepository by lazy {
        val db = MoodDatabase.getInstance(application)
        MoodRepository(db.moodDao())
    }

    // --- Estado de la semana actual ---
    private val _currentWeekStart = MutableStateFlow(LocalDate.now().with(DayOfWeek.MONDAY))
    val currentWeekStart: StateFlow<LocalDate> = _currentWeekStart.asStateFlow()

    val weekEntries: StateFlow<List<MoodEntry>> = _currentWeekStart.flatMapLatest { start ->
        repository.getWeekEntries(start, start.plusDays(6))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mapa de dia -> entrada para acceso rápido en la Home
    val weekEntriesMap: StateFlow<Map<LocalDate, MoodEntry>> = weekEntries.map { list ->
        list.associateBy { it.date }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Resumen: mood más frecuente de la semana
    val weekSummaryMood: StateFlow<MoodLevel?> = weekEntries.map { entries ->
        entries.groupBy { it.mood }
            .maxByOrNull { it.value.size }
            ?.key
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- Historial completo ---
    val allEntries: StateFlow<List<MoodEntry>> = repository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Estado del formulario ---
    private val _formMood = MutableStateFlow(MoodLevel.OKAY)
    val formMood: StateFlow<MoodLevel> = _formMood.asStateFlow()

    private val _formNote = MutableStateFlow("")
    val formNote: StateFlow<String> = _formNote.asStateFlow()

    private val _formTags = MutableStateFlow<Set<MoodTag>>(emptySet())
    val formTags: StateFlow<Set<MoodTag>> = _formTags.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    // --- Detalle de entrada ---
    private val _selectedEntry = MutableStateFlow<MoodEntry?>(null)
    val selectedEntry: StateFlow<MoodEntry?> = _selectedEntry.asStateFlow()

    fun onMoodSelected(mood: MoodLevel) { _formMood.value = mood }
    fun onNoteChanged(note: String) { _formNote.value = note }
    fun onTagToggled(tag: MoodTag) {
        _formTags.value = _formTags.value.toMutableSet().apply {
            if (contains(tag)) remove(tag) else add(tag)
        }
    }

    fun resetForm() {
        _formMood.value = MoodLevel.OKAY
        _formNote.value = ""
        _formTags.value = emptySet()
        _saveSuccess.value = false
    }

    fun prepareForm() {
        viewModelScope.launch {
            val todayEntry = repository.getTodayEntry()
            if (todayEntry != null) {
                _formMood.value = todayEntry.mood
                _formNote.value = todayEntry.note ?: ""
                _formTags.value = todayEntry.tags
            } else {
                resetForm()
            }
            _saveSuccess.value = false
        }
    }

    fun saveEntry() {
        viewModelScope.launch {
            val todayEntry = repository.getTodayEntry()
            val entry = MoodEntry(
                id = todayEntry?.id ?: 0,
                date = LocalDate.now(),
                mood = _formMood.value,
                tags = _formTags.value,
                note = _formNote.value.takeIf { it.isNotBlank() }
            )
            repository.saveEntry(entry)
            _saveSuccess.value = true
        }
    }

    fun loadEntryById(id: Long) {
        viewModelScope.launch {
            _selectedEntry.value = repository.getEntryById(id)
        }
    }

    fun previousWeek() {
        _currentWeekStart.value = _currentWeekStart.value.minusWeeks(1)
    }

    fun nextWeek() {
        val nextWeek = _currentWeekStart.value.plusWeeks(1)
        if (!nextWeek.isAfter(LocalDate.now().with(DayOfWeek.MONDAY))) {
            _currentWeekStart.value = nextWeek
        }
    }
}
