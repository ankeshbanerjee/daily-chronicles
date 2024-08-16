package com.example.dailychronicles.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.secondaryLight
import com.example.dailychronicles.LocalNavController
import com.example.dailychronicles.room_db.models.Note
import com.example.dailychronicles.ui.composables.BottomSheetContent
import com.example.dailychronicles.ui.composables.LoadingComposable
import com.example.dailychronicles.utils.Constant
import com.example.dailychronicles.utils.Constant.Companion.BgColorsList
import com.example.dailychronicles.utils.showToast
import com.example.dailychronicles.viewmodels.AllNotesViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun AllNotesScreen(viewModel: AllNotesViewModel) {
    val navController = LocalNavController.current
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
    AllNotesScreenContent(
        notes = notes.value,
        goBack = goBack,
        searchQuery = searchQuery.value,
        onValueChange = onValueChange,
        isLoading = isLoading.value,
        updateNote = updateNote,
        onDelete = onDelete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllNotesScreenContent(
    notes: List<Note>,
    goBack: () -> Boolean,
    searchQuery: String,
    onValueChange: (String) -> Unit,
    isLoading: Boolean,
    updateNote: (note: Note) -> Unit,
    onDelete: (note: Note) -> Unit,
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


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(
                WindowInsets.systemBars
            )
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
                text = "Your Notes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
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
        if (isLoading) {
            LoadingComposable()
            return
        }
        LazyColumn {
            items(notes.size) { idx ->
                NoteCard(note = notes[idx], onDelete = onDelete, onEditPressed = {
                    currNoteIndex = idx
                    bottomSheetVisible = true
                })
            }
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
private fun PreviewAllNotesScreen() {
    AllNotesScreenContent(notes = listOf(
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
        onDelete = {}
    )
}

@Composable
private fun NoteCard(
    note: Note,
    onDelete: (note: Note) -> Unit,
    onEditPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(android.graphics.Color.parseColor(note.backgroundColor)))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row {
                Text(
                    text = note.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = secondaryLight
                )
                Spacer(modifier = modifier.weight(1f))
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = modifier
                        .clip(
                            CircleShape
                        )
                        .clickable {
                            onEditPressed()
                        }
                )
                Spacer(modifier = modifier.width(8.dp))
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = modifier
                        .clip(
                            CircleShape
                        )
                        .clickable {
                            onDelete(note)
                        }
                )
            }
            Text(text = note.content, fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
private fun NoteCardPreview() {
    NoteCard(
        Note(
            title = "Title",
            content = "Content",
            dateAdded = LocalDate.now(),
            backgroundColor = Constant.Companion.BgColors.LightBlue
        ),
        onEditPressed = {},
        onDelete = {}
    )
}