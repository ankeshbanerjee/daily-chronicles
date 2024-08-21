package com.example.dailychronicles.ui.screens.AllNotesScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.secondaryLight
import com.example.dailychronicles.data.db.models.Note
import com.example.dailychronicles.utils.Constant
import java.time.LocalDate

@Composable
fun NoteCard(
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
                    tint = secondaryLight,
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
                    tint = secondaryLight,
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