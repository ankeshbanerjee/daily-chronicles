package com.example.dailychronicles.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _isHomeStart = MutableStateFlow(false)
    val isHomeStart = _isHomeStart.asStateFlow()

    fun setIsHomeStart(value: Boolean){
        _isHomeStart.value = value
    }
}