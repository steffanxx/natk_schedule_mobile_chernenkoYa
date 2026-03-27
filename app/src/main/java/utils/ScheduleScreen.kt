package utils // Убедись, что этот пакет соответствует расположению файла (судя по скриншоту, он в папке utils)

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.ui.theme.schedule.ScheduleList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf("ИС-12") }

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Загрузка списка групп
    LaunchedEffect(Unit) {
        try {
            val fetchedGroups = RetrofitInstance.api.getGroups()
            groups = fetchedGroups
        } catch (e: Exception) {
            println("Ошибка загрузки групп: ${e.message}")
        }
    }

    // Загрузка расписания
    LaunchedEffect(selectedGroup) {
        loading = true
        val range = getWeekDateRange()
        try {
            val result = RetrofitInstance.api.getSchedule(selectedGroup, range.first, range.second)
            schedule = result
            error = null
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Scaffold(
        modifier = modifier, // Используем переданный модификатор
        topBar = {
            TopAppBar(
                title = {
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(
                                text = "Группа: $selectedGroup ▼",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            groups.forEach { groupName ->
                                DropdownMenuItem(
                                    text = { Text(groupName) },
                                    onClick = {
                                        selectedGroup = groupName
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                },
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