package utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.ui.theme.schedule.ScheduleList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefsManager = remember { PrefsManager(context) }

    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf(prefsManager.getSavedGroup()) }

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredGroups = remember(searchQuery, groups) {
        groups.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        try { groups = RetrofitInstance.api.getGroups() } catch (e: Exception) { }
    }

    LaunchedEffect(selectedGroup) {
        loading = true
        val range = getWeekDateRange()
        try {
            schedule = RetrofitInstance.api.getSchedule(selectedGroup, range.first, range.second)
            error = null
        } catch (e: Exception) {
            error = e.message
        } finally { loading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text("Группа: $selectedGroup ▼", fontWeight = FontWeight.Bold)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false; searchQuery = "" },
                            modifier = Modifier.width(250.dp).heightIn(max = 400.dp)
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Поиск...") },
                                modifier = Modifier.padding(8.dp).fillMaxWidth()
                            )
                            filteredGroups.forEach { name ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedGroup = name
                                        prefsManager.saveGroup(name)
                                        expanded = false
                                        searchQuery = ""
                                    }
                                )
                            }
                        }
                    }
                },
                actions = {
                    val isFav = prefsManager.getFavorites().contains(selectedGroup)
                    IconButton(onClick = {
                        prefsManager.toggleFavorite(selectedGroup)
                        // Форсируем перерисовку иконки (хак для простоты)
                        expanded = !expanded; expanded = !expanded
                    }) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFav) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            if (loading) CircularProgressIndicator(Modifier.align(Alignment.Center))
            else ScheduleList(data = schedule)
        }
    }
}