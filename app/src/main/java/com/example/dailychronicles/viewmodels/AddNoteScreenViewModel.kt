package com.example.dailychronicles.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailychronicles.respositories.NoteRepository
import com.example.dailychronicles.room_db.models.Note
import com.example.dailychronicles.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddNoteScreenViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel(){
    var noteTitle = mutableStateOf("")
        private set

    var noteContent = mutableStateOf("")
        private set

    val _selectedBgColorIndex = MutableStateFlow(0)
    var selectedBgColorIndex = _selectedBgColorIndex.asStateFlow()


    fun updateNoteTitle(input: String){
        noteTitle.value = input
    }

    fun updateNoteContent(input: String){
        noteContent.value = input
    }

    fun updateSelectedBgColorIndex(idx: Int) {
        _selectedBgColorIndex.value = idx
    }

    fun handleAddNote(onNoteCreated: () -> Unit, onError: () -> Unit){
        val title = noteTitle.value
        val content = noteContent.value
        val bgColorIndex = selectedBgColorIndex.value
        val note = Note(
            title = title,
            content = content,
            dateAdded = LocalDate.now(),
            backgroundColor = Constant.BgColorsList[bgColorIndex]
        )
        viewModelScope.launch {
            try {
                repository.createNote(note)
                onNoteCreated()
            } catch (e: Exception){
                Log.e("CreateNoteError", e.message.toString())
                onError()
            }
        }
    }

}