package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.notesapp.ui.NotesScreen
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.viewmodel.NotesViewModel
import androidx.activity.enableEdgeToEdge
import com.example.notesapp.ui.NotesViewModelFactory
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.nav.NotesNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NotesApp
        val repository = app.repository


        setContent {
            NotesAppTheme {
                val viewModel: NotesViewModel = viewModel(
                    factory = NotesViewModelFactory(repository)
                )
                val navController = rememberNavController()

                NotesNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}


