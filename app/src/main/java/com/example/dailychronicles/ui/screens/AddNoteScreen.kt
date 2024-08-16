package com.example.dailychronicles.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailychronicles.viewmodels.AddNoteScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.onBackgroundLight
import com.example.compose.onSurfaceLight
import com.example.compose.onSurfaceVariantDark
import com.example.compose.onSurfaceVariantLight
import com.example.compose.outlineLight
import com.example.compose.outlineVariantLight
import com.example.compose.secondaryLight
import com.example.dailychronicles.Home
import com.example.dailychronicles.LocalNavController
import com.example.dailychronicles.utils.Constant.Companion.BgColorsList
import com.example.dailychronicles.utils.showToast

@Composable
fun AddNoteScreen(viewModel: AddNoteScreenViewModel, date: String?) {
    val bgColorIndex = viewModel.selectedBgColorIndex.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val goBack = {navController.popBackStack()}
    val context = LocalContext.current
    fun addNote(){
        viewModel.handleAddNote(
            onNoteCreated = {
                showToast(context, "Note added successfully")
//                navController.navigate(Home){
//                    popUpTo(navController.graph.id){
//                        inclusive = true
//                    }
//                }
                navController.popBackStack()
            },
            onError = {
                showToast(context, "Error adding note")
            }
        )
    }
    AddNoteScreenContent(
        title = viewModel.noteTitle.value,
        onTitleChange = { viewModel.updateNoteTitle(it) },
        content = viewModel.noteContent.value,
        onContentChange = { viewModel.updateNoteContent(it) },
        bgColorIndex = bgColorIndex.value,
        onBgColorChange = { viewModel.updateSelectedBgColorIndex(it) },
        date = date,
        goBack = goBack,
        addNote = ::addNote
    )
}

@Composable
private fun AddNoteScreenContent(
    modifier: Modifier = Modifier, title: String, onTitleChange: (String) -> Unit,
    content: String, onContentChange: (String) -> Unit,
    bgColorIndex: Int, onBgColorChange: (Int) -> Unit,
    date: String?,
    goBack: () -> Boolean,
    addNote: () -> Unit,
) {
    val animatedBgColor by animateColorAsState(targetValue = Color(android.graphics.Color.parseColor(BgColorsList[bgColorIndex])),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(animatedBgColor)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
//                .clip(shape = CircleShape)
//                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = onBackgroundLight,
                modifier = modifier
                    .clip(CircleShape)
                    .clickable { goBack() }
            )
            Text(text = "What's on your mind?", color = onSurfaceVariantLight)
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = onBackgroundLight,
                modifier = modifier.clickable {
                    addNote()
                }
            )
        }
        LazyRow(
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            items(BgColorsList.size) { index ->
                Box(
                    modifier = modifier
                        .padding(end = 10.dp)
                        .clip(CircleShape)
                        .size(34.dp)
                        .clickable { onBgColorChange(index) }
                        .border(if (index == bgColorIndex) 2.dp else 0.dp, Color.Black, CircleShape)
                        .background(
                            color = Color(
                                android.graphics.Color.parseColor(BgColorsList[index])
                            )
                        )
                )
            }
        }
        TextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    text = "Title",
                    fontWeight = FontWeight.Thin,
                    color = secondaryLight,
                    fontSize = 24.sp
                )
            },
            textStyle = TextStyle(
                color = onBackgroundLight,
                fontSize = 24.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = outlineLight,
                unfocusedIndicatorColor = outlineVariantLight,
            ),
            modifier = modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        )
        TextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = {
                Text(
                    text = "Type your thoughts",
                    fontWeight = FontWeight.Thin,
                    color = secondaryLight,
                    fontSize = 16.sp
                )
            },
            textStyle = TextStyle(
                color = onBackgroundLight,
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Preview
@Composable
private fun AddNotesScreenPreview() {
    AddNoteScreenContent(title = "",
        onTitleChange = {},
        content = "",
        onContentChange = {},
        bgColorIndex = 0,
        onBgColorChange = {},
        date = "2021-09-01",
        goBack = {true},
        addNote = {}
    )
}