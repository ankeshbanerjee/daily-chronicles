package com.example.dailychronicles.ui.screens.AllNotesScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailychronicles.data.respository.NoteRepository
import com.example.dailychronicles.data.db.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {
    private val _notes = MutableStateFlow(emptyList<Note>())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _filteredNotes = MutableStateFlow(emptyList<Note>())
    val filteredNotes = _filteredNotes.asStateFlow()

    var searchQuery = mutableStateOf("")
        private set

    fun loadAllNotes() {
        viewModelScope.launch {
            try {
                _notes.value = noteRepository.getAllNotes().also {
                    _filteredNotes.value = it
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("AllNotesError", e.message.toString())
                _isLoading.value = false
            }
        }
    }

    fun onChangeSearchQuery(query: String) {
        searchQuery.value = query
        if (searchQuery.value.isEmpty()) {
            _filteredNotes.value = _notes.value
            return
        }
        _filteredNotes.value = _notes.value.filter { note ->
            note.title.contains(
                searchQuery.value,
                ignoreCase = true
            )
        }
    }

    fun updateNote(note: Note, onUpdate: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                noteRepository.updateNote(note)
                onUpdate()
                _notes.value = noteRepository.getAllNotes().also {
                    _filteredNotes.value = it
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("UpdateNoteError", e.message.toString())
                _isLoading.value = false
            }
        }
    }

    fun deleteNote(note: Note, onDeleted: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                noteRepository.deleteNote(note)
                onDeleted()
                _notes.value = noteRepository.getAllNotes().also {
                    _filteredNotes.value = it
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("DeleteNoteError", e.message.toString())
                _isLoading.value = false
            }
        }
    }

    init {
        loadAllNotes()
    }
}
