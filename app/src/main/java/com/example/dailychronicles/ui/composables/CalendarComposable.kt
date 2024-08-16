package com.example.dailychronicles.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarComposable(noteDates: List<LocalDate>, onDayPress: (LocalDate) -> Unit) {
    var currentMonth by rememberSaveable { mutableStateOf(YearMonth.now()) }
    val startMonth = rememberSaveable { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = rememberSaveable { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek =
        rememberSaveable { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalCalendar(
            state = state,
            dayContent = { calendarDay ->
                val containsThisDay = noteDates.contains(calendarDay.date)
                Day(day = calendarDay, showDot = containsThisDay, onDayPress = onDayPress)
            },
            monthHeader = { month ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                 currentMonth = currentMonth.minusMonths(1)
                            },
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = month.yearMonth.month.name + " " + month.yearMonth.year,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Icon(
                            Icons.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                currentMonth = currentMonth.plusMonths(1)
                            },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    DaysOfWeekTitle(daysOfWeek = daysOfWeek())
                }
            }
        )
    }
}


@Composable
fun Day(day: CalendarDay, showDot: Boolean = false, onDayPress: (LocalDate) -> Unit = {}) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if(day.date == LocalDate.now()) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .aspectRatio(1f) // This is important for square sizing!
            .clickable { onDayPress(day.date) }
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
        )
        if (showDot){
            Box(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter)
                ,
            )
        }
    }
}

@Preview
@Composable
fun DayPreview() {
    Day(CalendarDay(LocalDate.now(), DayPosition.MonthDate), showDot = true)
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Preview
@Composable
fun CalendarComposablePreview() {
    CalendarComposable(noteDates = listOf(LocalDate.now(), LocalDate.parse("2024-08-14")), onDayPress = {})
}