package com.example.dailychronicles.ui.screens.ViewNotesOnDateScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.secondaryLight
import com.example.dailychronicles.app.host.AddNote
import com.example.dailychronicles.app.host.LocalNavController
import com.example.dailychronicles.data.db.models.Note
import com.example.dailychronicles.ui.composables.BottomSheetContent
import com.example.dailychronicles.ui.screens.ViewNotesOnDateScreen.components.NoteCard
import com.example.dailychronicles.utils.Constant
import com.example.dailychronicles.utils.showToast
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ViewNotesOnDateScreen(viewModel: ViewNotesOnDateScreenViewModel, date: String) {
    val navController = LocalNavController.current
    val navigateToAddNote = { date: String ->
        navController.navigate(route = AddNote(date = date))
    }
    val notes = viewModel.filteredNotes.collectAsStateWithLifecycle()
    val goBack = { navController.popBackStack() }
    val onValueChange = { query: String -> viewModel.onChangeSearchQuery(query) }
    val searchQuery = viewModel.searchQuery
    val isLoading = viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val onDelete = { note: Note ->
        viewModel.deleteNote(note, onDeleted = {
            showToast(context, "Note deleted!")
        })
    }
    val updateNote = { note: Note ->
        viewModel.updateNote(note, onUpdate = {
            showToast(context, "Note updated!")
        })
    }
    val launchedEffectKey = rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = launchedEffectKey) {
        viewModel.loadNotesOnDate()
    }
    ViewNotesOnDateScreenContent(
        date = date,
        notes = notes.value,
        goBack = goBack,
        searchQuery = searchQuery.value,
        onValueChange = onValueChange,
        isLoading = isLoading.value,
        updateNote = updateNote,
        onDelete = onDelete,
        navigateToAddNote = navigateToAddNote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewNotesOnDateScreenContent(
    date: String,
    notes: List<Note>,
    goBack: () -> Boolean,
    searchQuery: String,
    onValueChange: (String) -> Unit,
    isLoading: Boolean,
    updateNote: (note: Note) -> Unit,
    onDelete: (note: Note) -> Unit,
    navigateToAddNote: (date: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val sheetState = rememberModalBottomSheetState()
    var bottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    fun closeBottomSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            bottomSheetVisible = false
        }
    }

    var currNoteIndex by remember {
        mutableStateOf(0)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(
                WindowInsets.systemBars
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
                        .clip(CircleShape)
                        .clickable { goBack() })
                Text(
                    text = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                        .format(LocalDate.parse(date)),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground
                )
            }
            OutlinedTextField(
                value = searchQuery, onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Search notes",
                        color = MaterialTheme.colorScheme.outlineVariant,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp
                ),
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                suffix = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                modifier = modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 14.dp)
                    .fillMaxWidth()
            )
//            if (isLoading) {
//                LoadingComposable()
//                return
//            }
            LazyColumn {
                items(notes.size) { idx ->
                    NoteCard(note = notes[idx],
                        onDelete = onDelete, onEditPressed = {
                            currNoteIndex = idx
                            bottomSheetVisible = true
                        })
                }
            }

        }
        FloatingActionButton(
            onClick = {
                navigateToAddNote(date)
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = modifier
                .padding(bottom = 24.dp, end = 18.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }



    if (bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                bottomSheetVisible = false
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.background,
        ) {
            BottomSheetContent(notes[currNoteIndex], onSubmit = {
                closeBottomSheet()
                updateNote(it)
            })
        }
    }
}

@Preview
@Composable
private fun PreviewViewNotesOnDateScreen() {
    ViewNotesOnDateScreenContent(date = LocalDate.now().toString(),
        notes = listOf(
            Note(
                title = "Title",
                content = "Content",
                dateAdded = LocalDate.now(),
                backgroundColor = Constant.Companion.BgColors.LightPurple
            )
        ),
        goBack = { true },
        searchQuery = "",
        onValueChange = {},
        isLoading = false,
        updateNote = {},
        onDelete = {},
        navigateToAddNote = {})
}