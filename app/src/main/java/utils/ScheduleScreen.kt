package com.example.collegeschedule.ui.schedule // Оставляем как в методичке

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import utils.getWeekDateRange


// ВАЖНО: Импортируем твой ScheduleList из пакета со словом theme
import com.example.collegeschedule.ui.theme.schedule.ScheduleList


@Composable
fun ScheduleScreen(modifier: Modifier) {
    // Переменная должна называться точно так же, как в блоке else (schedule)
    var schedule by remember {
        mutableStateOf<List<ScheduleByDateDto>>(emptyList())
    }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val range = getWeekDateRange()
        val start = range.first
        val end = range.second
        try {
            schedule = RetrofitInstance.api.getSchedule(
                "ИС-12",
                start,
                end
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    when {
        loading -> CircularProgressIndicator()
        error != null -> Text("Ошибка: $error")
        else -> {
            // Теперь студия увидит эту функцию благодаря импорту выше
            ScheduleList(data = schedule)
        }
    }
}