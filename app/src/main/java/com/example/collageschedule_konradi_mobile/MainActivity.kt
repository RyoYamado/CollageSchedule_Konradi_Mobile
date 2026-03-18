package com.example.collageschedule_konradi_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.example.collageschedule_konradi_mobile.data.repository.FavoritesRepository
import com.example.collageschedule_konradi_mobile.ui.favorites.FavoritesScreen
import com.example.collageschedule_konradi_mobile.ui.schedule.ScheduleScreen
import com.example.collageschedule_konradi_mobile.ui.theme.CollegeScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeScheduleTheme {
                CollegeScheduleApp()
            }
        }
    }
}

@Composable
fun CollegeScheduleApp() {
    val context = LocalContext.current
    val favoritesRepository = remember { FavoritesRepository(context) }

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    // Group selected from Favorites → switch to HOME tab
    var groupToOpen by remember { mutableStateOf("ИС-12") }

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = { Icon(it.icon, contentDescription = it.label) },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME ->
                ScheduleScreen(
                    initialGroup = groupToOpen,
                    favoritesRepository = favoritesRepository
                )

            AppDestinations.FAVORITES ->
                FavoritesScreen(
                    favoritesRepository = favoritesRepository,
                    onGroupSelected = { group ->
                        groupToOpen = group
                        currentDestination = AppDestinations.HOME
                    }
                )

            AppDestinations.PROFILE ->
                ProfilePlaceholder()
        }
    }
}

@Composable
fun ProfilePlaceholder() {
    Surface(modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.layout.Box(
            contentAlignment = androidx.compose.ui.Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Профиль студента",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Расписание", Icons.Default.Home),
    FAVORITES("Избранное", Icons.Default.Favorite),
    PROFILE("Профиль", Icons.Default.AccountBox),
}