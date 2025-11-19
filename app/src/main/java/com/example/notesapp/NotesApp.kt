package com.example.notesapp

import android.app.Application
import androidx.room.Room
import com.example.notesapp.data.NoteDatabase
import com.example.notesapp.repository.NoteRepository

class NotesApp : Application() {

    val database by lazy {
        Room.databaseBuilder(
            this,
            NoteDatabase::class.java,
            "notes_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val repository by lazy {
        NoteRepository(database.noteDao())
    }
}
