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

@Composable
fun NotesScreen() {
    var notes by remember { mutableStateOf(listOf<Note>()) }
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
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
            // Heading
            Text(
                text = "Notes",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search notes...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Filtered notes
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
                        NoteItem(note = note, onDeleteClick = {
                            notes = notes.filter { it.id != note.id }
                        })
                    }
                }
            }
        }

        if (showDialog) {
            AddNoteDialog(
                onDismiss = { showDialog = false },
                onAddNote = { title ->
                    notes = notes + Note(id = notes.size + 1, title = title)
                }
            )
        }
    }
}
