package com.example.notesapp.ui


import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.notesapp.viewmodel.NotesViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NotesViewModel,
    onBack: () -> Unit,
    noteId: Int? = null
) {
    val notes by viewModel.notes.collectAsState()
    val existingNote = notes.find { it.id == noteId }

    var selectedImage by remember { mutableStateOf(existingNote?.imagePath) }
    var textState by remember {
        mutableStateOf(TextFieldValue(existingNote?.title ?: ""))
    }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImage = uri.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (noteId == null) "New Note" else "Edit Note",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        imagePickerLauncher.launch(arrayOf("image/*"))
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Pick Image")
                    }

                    Button(
                        onClick = {
                            val text = textState.text.trim()
                            if (text.isNotEmpty()) {
                                if (noteId == null) {
                                    viewModel.addNote(text, selectedImage)
                                } else {
                                    viewModel.updateNote(
                                        existingNote!!.copy(
                                            title = text,
                                            imagePath = selectedImage
                                        )
                                    )
                                }
                                onBack()
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            // -------- TEXT INPUT --------
            TextField(
                value = textState,
                onValueChange = { textState = it },
                placeholder = { Text("Write your note here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),  // Larger note area
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -------- IMAGE PREVIEW --------
            selectedImage?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
