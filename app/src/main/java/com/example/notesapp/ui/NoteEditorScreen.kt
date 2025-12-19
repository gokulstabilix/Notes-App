package com.example.notesapp.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import com.example.notesapp.viewmodel.NotesViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.core.app.ActivityCompat
import android.app.Activity


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
    var voicePath by remember { mutableStateOf(existingNote?.voicePath) }
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var pendingStartRecording by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val voiceNoteManager = remember { VoiceNoteManager(context) }
    DisposableEffect(Unit) {
        onDispose {
            voiceNoteManager.release()
        }
    }

    fun startRecording() {
        val path = voiceNoteManager.startRecording()
        if (path != null) {
            voicePath = path
            isRecording = true
            isPlaying = false
        }
    }

    fun stopRecording() {
        val path = voiceNoteManager.stopRecording()
        if (path != null) {
            voicePath = path
        }
        isRecording = false
    }

    fun startPlayback() {
        val path = voicePath ?: return
        voiceNoteManager.startPlayback(path) {
            isPlaying = false
        }
        isPlaying = true
    }

    fun pausePlayback() {
        voiceNoteManager.pausePlayback()
        isPlaying = false
    }

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

    val recordPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && pendingStartRecording) {
            pendingStartRecording = false
            startRecording()
        } else {
            pendingStartRecording = false
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

                    IconButton(onClick = {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            if (!isRecording) {
                                startRecording()
                            } else {
                                stopRecording()
                            }
                        } else {
                            val shouldShowRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    Manifest.permission.RECORD_AUDIO
                                )

                            if (shouldShowRationale) {
                                // User denied before (can ask again)
                                pendingStartRecording = true
                                recordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            } else {
                                // Permission permanently denied
                                Toast.makeText(
                                    context,
                                    "Microphone permission denied. Enable it from Settings.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = if (isRecording) "Stop Recording" else "Record Voice"
                        )
                    }
                    Button(
                        onClick = {
                            val text = textState.text.trim()
                            if (
                                text.isNotEmpty() ||
                                selectedImage != null ||
                                voicePath != null
                            ) {
                                if (noteId == null) {
                                    viewModel.addNote(text, selectedImage, voicePath)
                                } else {
                                    viewModel.updateNote(
                                        existingNote!!.copy(
                                            title = text,
                                            imagePath = selectedImage,
                                            voicePath = voicePath
                                        )
                                    )
                                }
                                onBack()
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Add text, image, or voice",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
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

            // -------- VOICE NOTE PREVIEW --------
            when {
                isRecording -> {
                    // SHOW WHILE RECORDING
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 72.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Recording...", color = Color.Red)

                            Row {
                                IconButton(onClick = {
                                    stopRecording()
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = "Stop & Save Recording")
                                }

                                IconButton(onClick = {
                                    isRecording = false
                                    voiceNoteManager.cancelRecording()
                                    voicePath = null
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                voicePath != null -> {
                    // SHOW AFTER RECORDING STOPPED
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 72.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Voice Note", style = MaterialTheme.typography.bodyMedium)

                            Row {
                                IconButton(onClick = {
                                    if (isPlaying) pausePlayback() else startPlayback()
                                }) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Pause" else "Play"
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // -------- IMAGE PREVIEW --------
            selectedImage?.let { imageUri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { selectedImage = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
