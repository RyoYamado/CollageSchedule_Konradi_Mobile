package com.example.collageschedule_konradi_mobile.ui.schedule

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collageschedule_konradi_mobile.data.dto.ScheduleByDateDto
import com.example.collageschedule_konradi_mobile.data.network.RetrofitInstance
import com.example.collageschedule_konradi_mobile.data.repository.FavoritesRepository
import com.example.collageschedule_konradi_mobile.ui.components.GroupSearchBar
import com.example.collageschedule_konradi_mobile.utils.getWeekDateRange

private sealed interface ScheduleState {
    object Loading : ScheduleState
    data class Error(val message: String) : ScheduleState
    data class Success(val data: List<ScheduleByDateDto>) : ScheduleState
}

@Composable
fun ScheduleScreen(
    initialGroup: String = "ИС-12",
    favoritesRepository: FavoritesRepository? = null
) {
    var selectedGroup by remember { mutableStateOf(initialGroup) }
    var state by remember { mutableStateOf<ScheduleState>(ScheduleState.Loading) }

    val favoritesState = favoritesRepository?.favorites?.collectAsState()
    val isFav = favoritesState?.value?.contains(selectedGroup) ?: false

    LaunchedEffect(selectedGroup) {
        state = ScheduleState.Loading
        val (start, end) = getWeekDateRange()
        try {
            val result = RetrofitInstance.api.getSchedule(selectedGroup, start, end)
            state = ScheduleState.Success(result)
        } catch (e: Exception) {
            state = ScheduleState.Error(e.message ?: "Неизвестная ошибка")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        Surface(
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GroupSearchBar(
                    currentGroup = selectedGroup,
                    onGroupSelected = { selectedGroup = it },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                if (favoritesRepository != null) {
                    IconButton(
                        onClick = { favoritesRepository.toggleFavorite(selectedGroup) }
                    ) {
                        Crossfade(targetState = isFav, label = "favorite") { fav ->
                            Icon(
                                imageVector = if (fav) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = if (fav) "Убрать из избранного"
                                else "Добавить в избранное",
                                tint = if (fav) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Use a simple String key for AnimatedContent to avoid hashCode on data objects
        val contentKey = when (state) {
            is ScheduleState.Loading -> "loading"
            is ScheduleState.Error -> "error"
            is ScheduleState.Success -> "success"
        }

        AnimatedContent(
            targetState = contentKey,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(150))
            },
            label = "schedule_content"
        ) { key ->
            when (key) {
                "loading" -> LoadingView()
                "error" -> ErrorView((state as? ScheduleState.Error)?.message ?: "")
                else -> ScheduleList((state as? ScheduleState.Success)?.data ?: emptyList())
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text(
                "Загрузка расписания…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Ошибка загрузки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}