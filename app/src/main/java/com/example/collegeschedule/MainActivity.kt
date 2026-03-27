package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme
import utils.ScheduleScreen // Если ScheduleScreen в папке utils

// 1. Описываем пункты меню
enum class AppDestinations(val label: String, val icon: ImageVector) {
    HOME("Главная", Icons.Default.Home),
    FAVORITES("Избранное", Icons.Default.Favorite),
    PROFILE("Профиль", Icons.Default.AccountBox)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Ошибка 'Unresolved reference CollegeScheduleTheme' уйдет после этого импорта
            CollegeScheduleTheme {
                CollegeScheduleApp()
            }
        }
    }
}

@Composable
fun CollegeScheduleApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        // Ошибки 'Unresolved reference Box' и 'align' уйдут благодаря импортам выше
        Surface(modifier = Modifier.fillMaxSize()) {
            when (currentDestination) {
                AppDestinations.HOME -> {
                    ScheduleScreen()
                }
                AppDestinations.FAVORITES -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Тут будет список избранных групп")
                    }
                }
                AppDestinations.PROFILE -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Экран профиля")
                    }
                }
            }
        }
    }
}