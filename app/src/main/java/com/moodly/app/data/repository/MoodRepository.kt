package com.moodly.app.data.repository

import com.moodly.app.data.db.MoodDao
import com.moodly.app.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * DECISIÓN TÉCNICA: El Repository actúa como única fuente de verdad (Single Source of Truth).
 * El ViewModel nunca accede al DAO directamente, lo que facilita pruebas unitarias
 * al poder reemplazar el Repository con un fake/mock.
 */
class MoodRepository(private val dao: MoodDao) {

    fun getAllEntries(): Flow<List<MoodEntry>> = dao.getAllEntries()

    fun getWeekEntries(startOfWeek: LocalDate, endOfWeek: LocalDate): Flow<List<MoodEntry>> =
        dao.getEntriesBetween(startOfWeek.toString(), endOfWeek.toString())

    suspend fun saveEntry(entry: MoodEntry) = dao.insert(entry)

    suspend fun getTodayEntry(): MoodEntry? = dao.getEntryByDate(LocalDate.now().toString())

    suspend fun getEntryById(id: Long): MoodEntry? = dao.getEntryById(id)

    suspend fun deleteEntry(entry: MoodEntry) = dao.delete(entry)
}
