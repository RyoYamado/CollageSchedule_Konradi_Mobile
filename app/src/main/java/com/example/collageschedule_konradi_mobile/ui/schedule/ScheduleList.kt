package com.example.collageschedule_konradi_mobile.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collageschedule_konradi_mobile.data.dto.LessonGroupPart
import com.example.collageschedule_konradi_mobile.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    if (data.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Нет данных за выбранный период",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(data) { day ->
            DaySection(day)
        }
    }
}

@Composable
private fun DaySection(day: ScheduleByDateDto) {
    Column {
        // Day header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = day.weekday,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = formatDate(day.lessonDate),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        if (day.lessons.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Выходной / нет занятий",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                day.lessons.forEach { lesson ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Lesson number accent bar
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                            ) {
                                // Lesson number + time
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${lesson.lessonNumber} пара",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = lesson.time,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Spacer(Modifier.height(6.dp))

                                // Group parts
                                val parts = lesson.groupParts.entries
                                    .filter { it.value != null }

                                if (parts.isEmpty()) {
                                    // Fallback to top-level lesson fields
                                    LessonContent(
                                        subject = lesson.subject,
                                        teacher = lesson.teacher,
                                        classroom = lesson.classroom,
                                        building = lesson.building,
                                        label = null
                                    )
                                } else {
                                    parts.forEach { (part, info) ->
                                        if (info != null) {
                                            if (part != LessonGroupPart.FULL) {
                                                Text(
                                                    text = when (part) {
                                                        LessonGroupPart.SUB1 -> "Подгруппа 1"
                                                        LessonGroupPart.SUB2 -> "Подгруппа 2"
                                                        else -> ""
                                                    },
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier.padding(bottom = 2.dp)
                                                )
                                            }
                                            LessonContent(
                                                subject = info.subject,
                                                teacher = info.teacher,
                                                classroom = info.classroom,
                                                building = info.building,
                                                label = null
                                            )
                                            if (parts.last().key != part) {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(vertical = 6.dp),
                                                    thickness = 0.5.dp,
                                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonContent(
    subject: String,
    teacher: String,
    classroom: String,
    building: String,
    label: String?
) {
    if (label != null) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
    }
    Text(
        text = subject,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = teacher,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
        text = "$building · ауд. $classroom",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline
    )
}

private fun formatDate(iso: String): String {
    // "2026-03-16" → "16 марта 2026"
    val months = listOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    return try {
        val parts = iso.split("-")
        val day = parts[2].trimStart('0')
        val month = months[parts[1].toInt() - 1]
        val year = parts[0]
        "$day $month $year"
    } catch (e: Exception) {
        iso
    }
}