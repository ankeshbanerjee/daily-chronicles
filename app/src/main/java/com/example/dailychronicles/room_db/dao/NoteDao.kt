package com.example.dailychronicles.room_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailychronicles.room_db.models.Note
import java.time.LocalDate

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table")
    suspend fun getAllNotes() : List<Note>

    @Query("SELECT * FROM note_table WHERE date_added = :date")
    suspend fun getNotesByDate(date: LocalDate) : List<Note>

    @Query("SELECT * FROM note_table ORDER BY date_added DESC LIMIT 10")
    suspend fun getRecentNotes() : List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table")
    fun clearAllNotes()
}