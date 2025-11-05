package com.example.notesapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.Note
import androidx.compose.foundation.clickable

@Composable
fun NoteItem(note: Note, onDeleteClick: (Note) -> Unit, onEditClick: (Note) -> Unit) {
    // Get whether dark theme is active
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val backgroundColor = if (isDarkTheme) {
        Color(0xFF2C2C2C) // soft dark gray
    } else {
        Color(0xFFF2F2F2) // light gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEditClick(note) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { onDeleteClick(note) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = Color(0xFFEF5350)
                )
            }
        }
    }
}
