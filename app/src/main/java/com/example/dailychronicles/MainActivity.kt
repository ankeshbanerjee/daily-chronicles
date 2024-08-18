package com.example.dailychronicles

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compose.DailyChroniclesTheme
import com.example.dailychronicles.ui.screens.AddNoteScreen
import com.example.dailychronicles.ui.screens.AllNotesScreen
import com.example.dailychronicles.ui.screens.BiometricLoginScreen
import com.example.dailychronicles.ui.screens.HomeScreen
import com.example.dailychronicles.ui.screens.ViewNotesOnDateScreen
import com.example.dailychronicles.utils.NotificationWorker
import com.example.dailychronicles.viewmodels.AddNoteScreenViewModel
import com.example.dailychronicles.viewmodels.AllNotesViewModel
import com.example.dailychronicles.viewmodels.HomeScreenViewModel
import com.example.dailychronicles.viewmodels.MainViewModel
import com.example.dailychronicles.viewmodels.ViewNotesOnDateScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

val LocalNavController = compositionLocalOf<NavController> { error("No nav controller found!") }


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()
        enableEdgeToEdge()
        setContent {
            DailyChroniclesTheme {
                // request permission for notification
                val postNotificationPermission =
                    rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
                LaunchedEffect(key1 = true) {
                    Log.d("launchedMainActivity", "launched")
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }
                // biometric authentication
                val biometricManager = BiometricManager.from(this@MainActivity)
                when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        mainViewModel.setShowLock(false)
                    }

                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        mainViewModel.setShowLock(false)
                    }
                }
                val showLock = mainViewModel.showLock.collectAsState()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                if (!(showLock.value)) {
                    LifecycleStartEffect(key1 = Unit) {
                        // user has to login with biometric each time the app comes to foreground
                        Log.d("launchedMainActivity", "launched start")
                        if (navBackStackEntry?.destination?.route != BiometricLogin::class.qualifiedName)
                            navController.navigate(route = BiometricLogin)
                        onStopOrDispose { }
                    }
                }
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = Home
                    ) {
                        composable<BiometricLogin> {
                            BiometricLoginScreen()
                        }
                        composable<Home> {
                            val viewModel = hiltViewModel<HomeScreenViewModel>()
                            HomeScreen(viewModel)
                        }
                        composable<AddNote> { backStackEntry ->
                            val viewModel = hiltViewModel<AddNoteScreenViewModel>()
                            val dateArg = backStackEntry.toRoute<AddNote>()
                            AddNoteScreen(viewModel, dateArg.date)
                        }
                        composable<ViewNotesOnDate> { backStackEntry ->
                            val dateArg = backStackEntry.toRoute<ViewNotesOnDate>()
                            val viewModel = hiltViewModel<ViewNotesOnDateScreenViewModel>()
                            ViewNotesOnDateScreen(viewModel, dateArg.date)
                        }
                        composable<AllNotes> {
                            val viewModel: AllNotesViewModel = hiltViewModel()
                            AllNotesScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
        scheduleNotificationWork()
    }

    private fun scheduleNotificationWork(){
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(12, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "notification_work",
            ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }
}

@Serializable
data object BiometricLogin

@Serializable
data object Home

@Serializable
data class AddNote(
    val date: String? = null
)

@Serializable
data class ViewNotesOnDate(val date: String)

@Serializable
data object AllNotes