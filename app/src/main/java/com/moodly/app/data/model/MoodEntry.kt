package com.moodly.app.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moodly.app.data.db.Converters
import java.time.LocalDate

/**
 * Entidad principal persistida con ROOM.
 */
@Entity(
    tableName = "mood_entries",
    indices = [Index(value = ["date"], unique = true)]
)
@TypeConverters(Converters::class)
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val mood: MoodLevel,
    val tags: Set<MoodTag> = emptySet(),
    val note: String? = null
)
