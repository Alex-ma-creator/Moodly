package com.moodly.app.data.db

import androidx.room.TypeConverter
import com.moodly.app.data.model.MoodLevel
import com.moodly.app.data.model.MoodTag
import java.time.LocalDate

class Converters {

    // LocalDate <-> String ISO
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    // MoodLevel <-> String
    @TypeConverter
    fun fromMoodLevel(mood: MoodLevel): String = mood.name

    @TypeConverter
    fun toMoodLevel(value: String): MoodLevel = MoodLevel.valueOf(value)

    // Set<MoodTag> <-> String (CSV de nombres de enum)
    @TypeConverter
    fun fromTagSet(tags: Set<MoodTag>): String =
        tags.joinToString(",") { it.name }

    @TypeConverter
    fun toTagSet(value: String): Set<MoodTag> =
        if (value.isBlank()) emptySet()
        else value.split(",")
            .filter { it.isNotBlank() }
            .map { MoodTag.valueOf(it) }
            .toSet()
}
