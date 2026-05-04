package com.moodly.app.data.db

import androidx.room.*
import com.moodly.app.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    fun getEntriesBetween(start: String, end: String): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryByDate(date: String): MoodEntry?

    @Query("SELECT * FROM mood_entries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): MoodEntry?

    @Delete
    suspend fun delete(entry: MoodEntry)
}
