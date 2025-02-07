package com.ssamsara98.dicodingevents

import com.ssamsara98.dicodingevents.response.EventsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/events")
    fun getEventList(
        @Query("active") active: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("query") query: String? = null,
    ): Call<EventsResponse>

    @GET("/events/{id}")
    fun getEventById(
        @Path("id") id: String
    ): Call<EventsResponse>

    // @FormUrlEncoded
    // @Headers("Authorization: token 12345")
    // @POST("review")
    // fun postReview(
    //     @Field("id") id: String,
    //     @Field("name") name: String,
    //     @Field("review") review: String
    // ): Call<T>
}
