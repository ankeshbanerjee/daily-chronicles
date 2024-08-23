package com.example.dailychronicles.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(@ApplicationContext private val appContext: Context) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    private val sharedPrefs = appContext.getSharedPreferences("theme", Context.MODE_PRIVATE)

    fun toggleTheme(){
        _isDarkTheme.value = !(_isDarkTheme.value)
        with(sharedPrefs.edit()){
            putBoolean("isDark", _isDarkTheme.value)
            apply()
        }
    }

    val isDarkTheme = _isDarkTheme.asStateFlow()
}