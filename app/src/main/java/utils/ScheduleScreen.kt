package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.theme.schedule.ScheduleList
import utils.getWeekDateRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val range = getWeekDateRange()
        try {
            schedule = RetrofitInstance.api.getSchedule("ИС-12", range.first, range.second)
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расписание ИС-12", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO: Поиск */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                error != null -> Text("Ошибка: $error", modifier = Modifier.padding(16.dp))
                else -> ScheduleList(data = schedule)
            }
        }
    }
}