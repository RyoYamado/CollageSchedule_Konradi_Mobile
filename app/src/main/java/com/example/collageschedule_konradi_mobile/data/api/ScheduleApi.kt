package com.example.collageschedule_konradi_mobile.data.api

import com.example.collageschedule_konradi_mobile.data.dto.ScheduleByDateDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("api/schedule/group/{groupName}")
    suspend fun getSchedule(
        @Path("groupName") groupName: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<ScheduleByDateDto>
}