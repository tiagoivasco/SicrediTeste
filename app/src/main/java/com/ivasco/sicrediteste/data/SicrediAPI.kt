package com.ivasco.sicrediteste.data

import com.ivasco.sicrediteste.model.CheckIn
import com.ivasco.sicrediteste.model.Events
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SicrediAPI {
    @GET("api/events")
    suspend fun getEvents(): ArrayList<Events>

    @POST("api/checkin")
    suspend fun checkInEvent(@Body checkInEvent: CheckIn): Response<*>
}