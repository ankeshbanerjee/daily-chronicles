package com.example.dailychronicles.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailychronicles.data.db.dao.NoteDao
import com.example.dailychronicles.data.db.models.Note
import com.example.dailychronicles.data.db.converters.DateConverter

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase: RoomDatabase(){
    abstract fun noteDao(): NoteDao
}