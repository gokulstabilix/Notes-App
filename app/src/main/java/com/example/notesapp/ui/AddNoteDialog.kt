package com.example.notesapp.ui


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onAddNote: (String) -> Unit
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (textState.text.isNotBlank()) {
                        onAddNote(textState.text)
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Note") },
        text = {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Enter note") }
            )
        }
    )
}
