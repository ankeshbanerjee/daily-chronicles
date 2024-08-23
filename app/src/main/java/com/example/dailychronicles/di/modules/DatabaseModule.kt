package com.example.dailychronicles.di.modules

import android.content.Context
import androidx.room.Room
import com.example.dailychronicles.data.db.dao.NoteDao
import com.example.dailychronicles.data.db.database.NoteDatabase
import com.example.dailychronicles.data.respository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDb(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, "note_database").build()

    @Provides
    fun providesNoteDao(noteDb: NoteDatabase): NoteDao = noteDb.noteDao()

    @Provides
    @Singleton
    fun providesNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepository(noteDao)
}