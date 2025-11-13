package com.example.notesapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notesapp.ui.NoteEditorScreen
import com.example.notesapp.ui.NotesScreen
import com.example.notesapp.viewmodel.NotesViewModel

@Composable
fun NotesNavGraph(
    navController: NavHostController,
    viewModel: NotesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "notes_list"
    ) {
        composable("notes_list") {
            NotesScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("add_note") },
                onEditClick = { noteId ->
                    navController.navigate("edit_note/$noteId")
                }
            )
        }

        composable("add_note") {
            NoteEditorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("edit_note/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            NoteEditorScreen(
                viewModel = viewModel,
                noteId = noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
