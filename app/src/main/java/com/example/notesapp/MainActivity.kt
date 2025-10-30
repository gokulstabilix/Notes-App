package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.room.Room
import com.example.notesapp.repository.NoteRepository
import com.example.notesapp.ui.NotesScreen
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.viewmodel.NotesViewModel


import com.example.notesapp.data.NoteDatabase
import com.example.notesapp.ui.NotesViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "notes_db"
        ).build()

        val repository = NoteRepository(db.noteDao())

        setContent {
            NotesAppTheme {
                val viewModel: NotesViewModel = viewModel(
                    factory = NotesViewModelFactory(repository)
                )
                NotesScreen(viewModel)
            }
        }
    }
}


