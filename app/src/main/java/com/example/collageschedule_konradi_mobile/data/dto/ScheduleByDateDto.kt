package com.example.collageschedule_konradi_mobile.data.dto

data class ScheduleByDateDto(
    val lessonDate: String = "",
    val weekday: String = "",
    val lessons: List<LessonDto> = emptyList()
)