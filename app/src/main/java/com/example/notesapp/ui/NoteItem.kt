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
fun NoteItem(
    note: Note,
    onDeleteClick: (Note) -> Unit,
    onEditClick: (Note) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // Your custom grey card for dark mode
    val darkGreyCard = Color(0xFF2C2C2E) // modify if needed

    val cardColor = if (isDark) {
        darkGreyCard   // 👈 custom grey card
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val textColor = if (isDark) Color(0xFFE5E5E7) else MaterialTheme.colorScheme.onSurface

    val deleteTint = MaterialTheme.colorScheme.error


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onEditClick(note) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                modifier = Modifier.weight(1f),
                maxLines = 5
            )
            IconButton(onClick = { onDeleteClick(note) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = deleteTint
                )
            }
        }
    }
}
