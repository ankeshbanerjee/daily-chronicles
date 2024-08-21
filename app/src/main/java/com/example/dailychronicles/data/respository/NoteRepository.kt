package com.example.dailychronicles.data.respository

import com.example.dailychronicles.data.db.dao.NoteDao
import com.example.dailychronicles.data.db.models.Note
import java.time.LocalDate
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    suspend fun getAllNotes() = noteDao.getAllNotes()

    suspend fun getNotesByDate(date: LocalDate) = noteDao.getNotesByDate(date)

    suspend fun getRecentNotes() = noteDao.getRecentNotes()

    suspend fun createNote(note: Note) = noteDao.createNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}