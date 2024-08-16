package com.example.dailychronicles.room_db.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailychronicles.room_db.dao.NoteDao
import com.example.dailychronicles.room_db.models.Note
import com.example.dailychronicles.room_db.typeConverters.DateConverter

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase: RoomDatabase(){
    abstract fun noteDao(): NoteDao
}