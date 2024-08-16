package com.example.dailychronicles.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailychronicles.respositories.NoteRepository
import com.example.dailychronicles.room_db.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {
    private val _allNotes = MutableStateFlow(emptyList<Note>())
    val allNotes = _allNotes.asStateFlow()

    private val _recentNotes = MutableStateFlow(emptyList<Note>())
    val recentNotes = _recentNotes.asStateFlow()

    private val _noteDates = MutableStateFlow(emptyList<LocalDate>())
    val noteDates = _noteDates.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun loadNotes() {
        viewModelScope.launch {
            try {
                val job1 = async { noteRepository.getAllNotes() }
                val job2 = async { noteRepository.getRecentNotes() }
                _allNotes.value = job1.await()
                _recentNotes.value = job2.await()
                _noteDates.value = _allNotes.value.map { it.dateAdded }.distinct().sorted()
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("GetAllNotesError", e.message.toString())
                _isLoading.value = false
            }
        }
    }

    init {
        loadNotes()
    }
}