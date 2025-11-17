package com.example.notesapp.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.Note
import com.example.notesapp.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    val notes = repository.notes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(title: String, imagePath: String? ) {
        viewModelScope.launch {
            repository.insert(Note(title = title, imagePath = imagePath))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}
