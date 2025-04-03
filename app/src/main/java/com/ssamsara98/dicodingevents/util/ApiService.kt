package com.ssamsara98.dicodingevents.util

import com.ssamsara98.dicodingevents.data.response.EventResponse
import com.ssamsara98.dicodingevents.data.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events")
    suspend fun getEventListAsync(
        @Query("active") active: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("q") q: String? = null,
    ): EventsResponse

    @GET("/events/{id}")
    suspend fun getEventByIdAsync(
        @Path("id") id: String
    ): EventResponse
}
