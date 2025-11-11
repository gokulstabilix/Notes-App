package com.example.notesapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.Note
import com.example.notesapp.viewmodel.NotesViewModel

@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                noteToEdit = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Notes", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search notes...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            val filteredNotes = notes.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }

            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No notes yet. Add a note!")
                }
            } else {
                LazyColumn {
                    items(filteredNotes) { note ->
                        NoteItem(
                            note = note,
                            onDeleteClick = {
//                                viewModel.deleteNote(note)
                                    noteToDelete = note
                                    showDeleteDialog = true

                            },
                            onEditClick = {
                                noteToEdit = it
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            NoteDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    if (noteToEdit == null) {
                        viewModel.addNote(it)
                    } else {
                        viewModel.updateNote(noteToEdit!!.copy(title = it))
                    }
                },
                noteToEdit = noteToEdit
            )
        }

        if (showDeleteDialog && noteToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteNote(noteToDelete!!)
                            showDeleteDialog = false
                            noteToDelete = null
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            noteToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

