package com.example.dailychronicles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.compose.DailyChroniclesTheme
import com.example.dailychronicles.ui.screens.AddNoteScreen
import com.example.dailychronicles.ui.screens.AllNotesScreen
import com.example.dailychronicles.ui.screens.HomeScreen
import com.example.dailychronicles.ui.screens.ViewNotesOnDateScreen
import com.example.dailychronicles.viewmodels.AddNoteScreenViewModel
import com.example.dailychronicles.viewmodels.AllNotesViewModel
import com.example.dailychronicles.viewmodels.HomeScreenViewModel
import com.example.dailychronicles.viewmodels.ViewNotesOnDateScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

val LocalNavController = compositionLocalOf<NavController> { error("No nav controller found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyChroniclesTheme(
                darkTheme = false
            ){
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(navController = navController, startDestination = Home){
                        composable<Home> {
                            val viewModel = hiltViewModel<HomeScreenViewModel>()
                            HomeScreen(viewModel)
                        }
                        composable<AddNote> {
                            val viewModel = hiltViewModel<AddNoteScreenViewModel>()
                            AddNoteScreen(viewModel)
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
data object Home

@Serializable
data object AddNote

@Serializable
data class ViewNotesOnDate(val date: String)

@Serializable
data object AllNotes