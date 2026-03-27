package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme
import utils.PrefsManager
import utils.ScheduleScreen

enum class AppDestinations(val label: String, val icon: ImageVector) {
    HOME("Главная", Icons.Default.Home),
    FAVORITES("Избранное", Icons.Default.Favorite),
    PROFILE("Профиль", Icons.Default.AccountBox)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CollegeScheduleTheme { CollegeScheduleApp() } }
    }
}

@Composable
fun CollegeScheduleApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val context = LocalContext.current
    val prefs = remember { PrefsManager(context) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { dest ->
                item(
                    icon = { Icon(dest.icon, null) },
                    label = { Text(dest.label) },
                    selected = dest == currentDestination,
                    onClick = { currentDestination = dest }
                )
            }
        }
    ) {
        Surface(Modifier.fillMaxSize()) {
            when (currentDestination) {
                AppDestinations.HOME -> ScheduleScreen()
                AppDestinations.FAVORITES -> FavoritesList(prefs) { group ->
                    prefs.saveGroup(group)
                    currentDestination = AppDestinations.HOME
                }
                AppDestinations.PROFILE -> Box(contentAlignment = Alignment.Center) { Text("Профиль") }
            }
        }
    }
}

@Composable
fun FavoritesList(prefs: PrefsManager, onSelect: (String) -> Unit) {
    val list = prefs.getFavorites().toList()
    if (list.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Список избранного пуст")
        }
    } else {
        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            item { Text("Ваши группы", style = MaterialTheme.typography.headlineMedium) }
            items(list) { group ->
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onSelect(group) }
                ) {
                    ListItem(
                        headlineContent = { Text(group) },
                        leadingContent = { Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700)) }
                    )
                }
            }
        }
    }
}