package com.example.dailychronicles.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dailychronicles.di.MyApplication
import com.example.dailychronicles.utils.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _showLock = MutableStateFlow(true)
    val showLock = _showLock.asStateFlow()

    fun setShowLock(value: Boolean) {
        _showLock.value = value
    }
}