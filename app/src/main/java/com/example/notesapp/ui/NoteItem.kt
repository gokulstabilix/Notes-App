package com.example.notesapp.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.Note
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun NoteItem(
    note: Note,
    onDeleteClick: (Note) -> Unit,
    onEditClick: (Note) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    val darkGreyCard = Color(0xFF2C2C2E)
    val cardColor = if (isDark) darkGreyCard else MaterialTheme.colorScheme.surfaceContainer
    val textColor = if (isDark) Color(0xFFE5E5E7) else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onEditClick(note) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            // ---- IMAGE ----
            note.imagePath?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 18.dp,
                                topEnd = 18.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {

                // ---- TITLE ----
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ---- DELETE BUTTON ----
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (note.voicePath != null) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice note",
                            tint = textColor,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    IconButton(onClick = { onDeleteClick(note) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = if (isDark) Color(0xFFFF6B6B) else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
