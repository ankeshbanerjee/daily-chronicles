package com.example.dailychronicles.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailychronicles.room_db.models.Note
import com.example.dailychronicles.utils.Constant
import com.example.dailychronicles.utils.Constant.Companion.BgColorsList
import java.time.LocalDate

@Composable
fun BottomSheetContent(
    note: Note,
    onSubmit: (note: Note) -> Unit,
    modifier: Modifier = Modifier
) {

    var title by rememberSaveable {
        mutableStateOf(note.title)
    }

    var content by rememberSaveable {
        mutableStateOf(note.content)
    }

    var bgColorIndex by rememberSaveable {
        mutableStateOf(BgColorsList.indexOf(note.backgroundColor))
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 20.dp)
            .padding(bottom = 14.dp)
    ) {
        Text(
            text = "Edit Note",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = modifier.height(12.dp))
        Text(
            text = "Title",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = {
                Text(
                    text = "Enter title",
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 14.sp
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = "Content",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = modifier.height(8.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = {
                Text(
                    text = "Enter content",
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 14.sp
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = "Background Color",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = modifier.height(8.dp))
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
                        .clickable { bgColorIndex = index }
                        .border(if (index == bgColorIndex) 2.dp else 0.dp, Color.Black, CircleShape)
                        .background(
                            color = Color(
                                android.graphics.Color.parseColor(BgColorsList[index])
                            )
                        )
                )
            }
        }
        Spacer(modifier = modifier.height(12.dp))
        Button(onClick = {
            val updatedNote = Note(
                id = note.id,
                title = title,
                content = content,
                dateAdded = note.dateAdded,
                backgroundColor = BgColorsList[bgColorIndex]
            )
            onSubmit(updatedNote)
        }, modifier = modifier.fillMaxWidth()) {
            Text(text = "Save")
        }
    }
}

@Preview
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        Note(
            title = "",
            content = "",
            dateAdded = LocalDate.now(),
            backgroundColor = Constant.Companion.BgColors.LightBlue
        ),
        onSubmit = {}
    )
}