package com.example.dailychronicles.ui.screens.AddNoteScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dailychronicles.app.host.AddNote
import com.example.dailychronicles.data.respository.NoteRepository
import com.example.dailychronicles.data.db.models.Note
import com.example.dailychronicles.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddNoteScreenViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var noteTitle = mutableStateOf("")
        private set

    var noteContent = mutableStateOf("")
        private set

    val _selectedBgColorIndex = MutableStateFlow(0)
    var selectedBgColorIndex = _selectedBgColorIndex.asStateFlow()

    private val date = savedStateHandle.toRoute<AddNote>().date

    fun updateNoteTitle(input: String) {
        noteTitle.value = input
    }

    fun updateNoteContent(input: String) {
        noteContent.value = input
    }

    fun updateSelectedBgColorIndex(idx: Int) {
        _selectedBgColorIndex.value = idx
    }

    fun handleAddNote(onNoteCreated: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val title = noteTitle.value
                val content = noteContent.value
                val bgColorIndex = selectedBgColorIndex.value
                val note = Note(
                    title = title,
                    content = content,
                    dateAdded = date?.let { LocalDate.parse(it) } ?: LocalDate.now(),
                    backgroundColor = Constant.BgColorsList[bgColorIndex]
                )
                repository.createNote(note)
                onNoteCreated()
            } catch (e: Exception) {
                Log.e("CreateNoteError", e.message.toString())
                onError()
            }
        }
    }

}