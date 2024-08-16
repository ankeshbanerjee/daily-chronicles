package com.example.dailychronicles.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)

    fun toggleTheme(){
        _isDarkTheme.value = !(_isDarkTheme.value)
    }

    val isDarkTheme = _isDarkTheme.asStateFlow()
}