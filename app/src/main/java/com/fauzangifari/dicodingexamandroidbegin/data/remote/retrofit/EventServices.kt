package com.fauzangifari.dicodingexamandroidbegin.data.remote.retrofit

import com.fauzangifari.dicodingexamandroidbegin.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET

interface EventServices {

    @GET("events")
    suspend fun getAllEvents(): EventResponse

    @GET("events?active=1")
    suspend fun getActiveEvents(): EventResponse

    @GET("events?active=0")
    suspend fun getInactiveEvents(): EventResponse

    @GET("events?active=1&limit=1")
    suspend fun getOneActiveEvent(): EventResponse

}