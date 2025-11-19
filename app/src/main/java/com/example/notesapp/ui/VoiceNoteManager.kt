package com.example.notesapp.ui

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import java.io.File

class VoiceNoteManager(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentOutputFile: String? = null

    fun startRecording(): String? {
        stopPlayback()

        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            ?: context.filesDir
        val outputFile = File(outputDir, "note_${System.currentTimeMillis()}.3gp")
        currentOutputFile = outputFile.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(currentOutputFile)
            prepare()
            start()
        }
        return currentOutputFile
    }

    fun stopRecording(): String? {
        mediaRecorder?.apply {
            try {
                stop()
            } catch (_: RuntimeException) {
                // stop() failed, ignore
            }
            release()
        }
        mediaRecorder = null
        return currentOutputFile
    }

    // ---------------------
    // ✔ CANCEL RECORDING
    // ---------------------
    fun cancelRecording() {
        try {
            mediaRecorder?.apply {
                try {
                    stop() // may throw error → safe inside try
                } catch (_: Exception) { }
                release()
            }
        } catch (_: Exception) { }

        mediaRecorder = null

        // Delete the audio file because user cancelled
        currentOutputFile?.let { path ->
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }

        currentOutputFile = null
    }
    // ---------------------

    fun startPlayback(path: String, onCompleted: (() -> Unit)? = null) {
        stopPlayback()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
            setOnCompletionListener {
                onCompleted?.invoke()
                stopPlayback()
            }
        }
    }

    fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun stopPlayback() {
        mediaPlayer?.apply {
            try {
                stop()
            } catch (_: IllegalStateException) {}
            release()
        }
        mediaPlayer = null
    }

    fun release() {
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
