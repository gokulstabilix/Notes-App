package com.example.notesapp.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.example.notesapp.data.Note

@Composable
fun NoteDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    noteToEdit: Note? = null
) {
    var textState by remember {
        mutableStateOf(TextFieldValue(noteToEdit?.title ?: ""))
    }

    val dialogTitle = if (noteToEdit == null) "Add Note" else "Edit Note"
    val confirmButtonText = if (noteToEdit == null) "Add" else "Save"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (textState.text.isNotBlank()) {
                        onConfirm(textState.text)
                        onDismiss()
                    }
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(dialogTitle) },
        text = {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Enter note") }
            )
        }
    )
}
