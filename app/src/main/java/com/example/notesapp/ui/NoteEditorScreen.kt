package com.example.notesapp.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.notesapp.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NotesViewModel,
    onBack: () -> Unit,
    noteId: Int? = null
) {
    val notes by viewModel.notes.collectAsState()
    val existingNote = notes.find { it.id == noteId }

    var textState by remember {
        mutableStateOf(TextFieldValue(existingNote?.title ?: ""))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == null) "Add Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val text = textState.text.trim()
                        if (text.isNotEmpty()) {
                            if (noteId == null) viewModel.addNote(text)
                            else viewModel.updateNote(existingNote!!.copy(title = text))
                            onBack()
                        }
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        TextField(
            value = textState,
            onValueChange = { textState = it },
            placeholder = { Text("Write your note here...") },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        )
    }
}
