package com.example.dailychronicles.ui.screens.HomeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailychronicles.data.db.models.Note
import com.example.dailychronicles.utils.Constant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteCard(note: Note) {
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