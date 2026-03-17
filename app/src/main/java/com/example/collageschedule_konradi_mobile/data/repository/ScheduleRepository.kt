package com.example.collageschedule_konradi_mobile.data.repository
import com.example.collageschedule_konradi_mobile.data.api.ScheduleApi
import com.example.collageschedule_konradi_mobile.data.dto.ScheduleByDateDto
class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(group: String): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = group,
            start = "2026-03-16",
            end = "2026-03-25"
        )
    }
}