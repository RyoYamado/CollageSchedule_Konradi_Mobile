package com.example.collageschedule_konradi_mobile.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Класс для кастомных цветов темы
@Stable
class CollegeScheduleColors(
    val statusBarColor: Color = Color.Transparent,
    val navigationBarColor: Color = Color.Transparent
)

// Локальная композиция для кастомных цветов
val LocalCollegeScheduleColors = staticCompositionLocalOf {
    CollegeScheduleColors()
}

// Расширение для MaterialTheme
object CollegeScheduleTheme {
    val colors: CollegeScheduleColors
        @Composable
        get() = LocalCollegeScheduleColors.current
}