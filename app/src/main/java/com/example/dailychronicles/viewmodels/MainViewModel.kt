package com.example.dailychronicles.viewmodels

import androidx.lifecycle.ViewModel
import com.example.compose.AppThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    private val _appThemeState =
        MutableStateFlow(
            AppThemeState(
                isDarkTheme = _isDarkTheme.value,
                toggleTheme = { _isDarkTheme.value = !(_isDarkTheme.value) }
            )
        )

    val appThemeState = _appThemeState.asStateFlow()
    val isDarkTheme = _isDarkTheme.asStateFlow()
}