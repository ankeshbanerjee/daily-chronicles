package com.example.dailychronicles

import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.compose.DailyChroniclesTheme
import com.example.compose.LocalAppTheme
import com.example.compose.backgroundDark
import com.example.compose.backgroundLight
import com.example.compose.onBackgroundLight
import com.example.dailychronicles.ui.screens.AddNoteScreen
import com.example.dailychronicles.ui.screens.AllNotesScreen
import com.example.dailychronicles.ui.screens.BiometricLoginScreen
import com.example.dailychronicles.ui.screens.HomeScreen
import com.example.dailychronicles.ui.screens.ViewNotesOnDateScreen
import com.example.dailychronicles.viewmodels.AddNoteScreenViewModel
import com.example.dailychronicles.viewmodels.AllNotesViewModel
import com.example.dailychronicles.viewmodels.HomeScreenViewModel
import com.example.dailychronicles.viewmodels.MainViewModel
import com.example.dailychronicles.viewmodels.ViewNotesOnDateScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

val LocalNavController = compositionLocalOf<NavController> { error("No nav controller found!") }

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()
        enableEdgeToEdge()
        setContent {
            DailyChroniclesTheme {
                val biometricManager = BiometricManager.from(this@MainActivity)
                when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        mainViewModel.setIsHomeStart(false)
                    }
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        mainViewModel.setIsHomeStart(false)
                    }
                }
                val isHomeStart = mainViewModel.isHomeStart.collectAsState()
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = if (isHomeStart.value) Home else BiometricLogin
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
                            val Date = backStackEntry.toRoute<AddNote>()
                            AddNoteScreen(viewModel, Date.date)
                        }
                        composable<ViewNotesOnDate> { backStackEntry ->
                            val Date = backStackEntry.toRoute<ViewNotesOnDate>()
                            val viewModel = hiltViewModel<ViewNotesOnDateScreenViewModel>()
                            ViewNotesOnDateScreen(viewModel, Date.date)
                        }
                        composable<AllNotes> {
                            val viewModel: AllNotesViewModel = hiltViewModel()
                            AllNotesScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
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