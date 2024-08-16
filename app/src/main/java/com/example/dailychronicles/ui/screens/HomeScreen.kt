package com.example.dailychronicles.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailychronicles.AddNote
import com.example.dailychronicles.AllNotes
import com.example.dailychronicles.LocalNavController
import com.example.dailychronicles.ViewNotesOnDate
import com.example.dailychronicles.room_db.models.Note
import com.example.dailychronicles.ui.composables.CalendarComposable
import com.example.dailychronicles.ui.composables.LoadingComposable
import com.example.dailychronicles.utils.Constant
import com.example.dailychronicles.viewmodels.HomeScreenViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val navController = LocalNavController.current
    val navigateToAddNote = {
        navController.navigate(route = AddNote(date = null))
    }
    val recentNotes = viewModel.recentNotes.collectAsStateWithLifecycle()
    val noteDates = viewModel.noteDates.collectAsStateWithLifecycle()
    val onDayPress = { date: LocalDate ->
        navController.navigate(route = ViewNotesOnDate(date = date.toString()))
    }
    val isLoading = viewModel.isLoading.collectAsState()
    fun viewAllNotes() {
        navController.navigate(route = AllNotes)
    }

    val launchedEffectKey = rememberSaveable{
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = launchedEffectKey.value) {
        viewModel.loadNotes()
    }
    HomeScreenContent(
        navigateToAddNote = navigateToAddNote,
        recentNotes = recentNotes.value,
        noteDates = noteDates.value,
        onDayPress = onDayPress,
        isLoading = isLoading.value,
        viewAllNotes = ::viewAllNotes,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeScreenContent(
    navigateToAddNote: () -> Unit,
    modifier: Modifier = Modifier,
    recentNotes: List<Note>,
    noteDates: List<LocalDate>,
    onDayPress: (LocalDate) -> Unit,
    isLoading: Boolean,
    viewAllNotes: () -> Unit,
) {
    if (isLoading){
        LoadingComposable()
        return
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column {
            Box(
                modifier = modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(10.dp)
            ) {
                CalendarComposable(noteDates, onDayPress = { date ->
                    onDayPress(date)
                })
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(top = 2.dp, start = 20.dp, end = 20.dp, bottom = 6.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Recent Notes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "View All >",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier.clickable {
                        viewAllNotes()
                    }
                )
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(130.dp),
                verticalItemSpacing = 6.dp,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = modifier
                    .padding(6.dp)
                    .fillMaxSize()
            ) {
                items(recentNotes.size) { index ->
                    NoteCard(note = recentNotes[index])
                }
            }
        }
        FloatingActionButton(
            onClick = navigateToAddNote,
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
}


@Composable
private fun NoteCard(note: Note) {
    val (id, title, content, dateAdded, backgroundColor) = note
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(android.graphics.Color.parseColor(backgroundColor)))
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = Color.Black
            )
            Text(
                text = content,
                fontSize = 16.sp,
                color = Color.Black,
//                maxLines = Random.nextInt(2, 8),
                maxLines = 7,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(dateAdded),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
private fun NoteCardPreview() {
    NoteCard(
        note = Note(
            title = "Title",
            content = "Content",
            backgroundColor = Constant.BgColorsList[0],
            dateAdded = LocalDate.now(),
        )
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        navigateToAddNote = {}, recentNotes = listOf(
            Note(
                title = "Title",
                content = "Content",
                backgroundColor = Constant.BgColorsList[0],
                dateAdded = LocalDate.now(),
            ),
            Note(
                title = "Title",
                content = "Content",
                backgroundColor = Constant.BgColorsList[1],
                dateAdded = LocalDate.now(),
            ),
            Note(
                title = "Title",
                content = "This is long content that should wrap to the next line",
                backgroundColor = Constant.BgColorsList[2],
                dateAdded = LocalDate.now(),
            ),
            Note(
                title = "Title",
                content = "Content",
                backgroundColor = Constant.BgColorsList[3],
                dateAdded = LocalDate.now(),
            ),
            Note(
                title = "Title",
                content = "Content",
                backgroundColor = Constant.BgColorsList[3],
                dateAdded = LocalDate.now(),
            ),
            Note(
                title = "Title",
                content = "Content",
                backgroundColor = Constant.BgColorsList[1],
                dateAdded = LocalDate.now(),
            ),
            Note(
                title = "Title",
                content = "This is long content that should wrap to the next line",
                backgroundColor = Constant.BgColorsList[2],
                dateAdded = LocalDate.now(),
            ),
        ),
        noteDates = listOf(
            LocalDate.now(),
            LocalDate.parse("2024-08-14"),
            LocalDate.parse("2024-08-12"),
        ),
        onDayPress = {},
        isLoading = false,
        viewAllNotes = {},
    )
}