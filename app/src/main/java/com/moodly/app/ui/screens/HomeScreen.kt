package com.moodly.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moodly.app.data.model.MoodEntry
import com.moodly.app.viewmodel.MoodViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: MoodViewModel,
    onRegisterClick: () -> Unit,
    onDayClick: (Long) -> Unit
) {
    val weekStart by viewModel.currentWeekStart.collectAsStateWithLifecycle()
    val weekMap by viewModel.weekEntriesMap.collectAsStateWithLifecycle()
    val summaryMood by viewModel.weekSummaryMood.collectAsStateWithLifecycle()

    val weekDays = (0..6).map { weekStart.plusDays(it.toLong()) }
    val today = LocalDate.now()
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Moodly 🌱",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Tu diario emocional privado",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Resumen semanal card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Navegacion de semana
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.previousWeek() }) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Semana anterior")
                    }
                    Text(
                        text = weekStart.format(monthFormatter).replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton(onClick = { viewModel.nextWeek() }) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Semana siguiente")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Emojis por dia
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    weekDays.forEach { day ->
                        val entry = weekMap[day]
                        DayEmoji(
                            day = day,
                            entry = entry,
                            isToday = day == today,
                            onClick = { entry?.let { onDayClick(it.id) } }
                        )
                    }
                }

                // Resumen
                if (summaryMood != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estado más frecuente esta semana: ${summaryMood!!.emoji} ${summaryMood!!.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Boton Registrar
        val todayEntry = weekMap[today]
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (todayEntry != null) "✏️  Editar registro de hoy" else "✨  Registrar cómo me siento hoy",
                fontSize = 16.sp
            )
        }

        if (todayEntry != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = todayEntry.mood.emoji, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Hoy te sientes: ${todayEntry.mood.label}", fontWeight = FontWeight.SemiBold)
                        if (!todayEntry.note.isNullOrBlank()) {
                            Text(
                                text = todayEntry.note,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayEmoji(
    day: LocalDate,
    entry: MoodEntry?,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayName = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es"))
        .take(2).uppercase()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = entry != null, onClick = onClick)
            .padding(4.dp)
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                )
        ) {
            Text(
                text = entry?.mood?.emoji ?: "·",
                fontSize = if (entry != null) 20.sp else 16.sp
            )
        }
    }
}
