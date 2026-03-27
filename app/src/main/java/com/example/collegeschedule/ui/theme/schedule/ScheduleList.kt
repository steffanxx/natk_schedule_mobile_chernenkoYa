package com.example.collegeschedule.ui.theme.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collegeschedule.data.dto.LessonDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data) { day ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = day.weekday.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = day.lessonDate,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (day.lessons.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text("Пар нет, можно отдыхать!", modifier = Modifier.padding(16.dp))
                }
            } else {
                day.lessons.forEach { lesson ->
                    LessonCard(lesson)
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: LessonDto) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = lesson.time.split("-")[0], fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = lesson.lessonNumber.toString(), color = MaterialTheme.colorScheme.primary)
                }
                Text(text = lesson.time.split("-")[1], color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                lesson.groupParts.forEach { (part, info) ->
                    if (info != null) {
                        Text(
                            text = info.subject,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text(text = " ${info.teacher}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text(text = " ${info.building}, каб. ${info.classroom}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}