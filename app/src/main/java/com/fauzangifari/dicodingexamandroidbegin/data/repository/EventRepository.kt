package com.fauzangifari.dicodingexamandroidbegin.data.repository

import com.fauzangifari.dicodingexamandroidbegin.data.remote.retrofit.EventServices

class EventRepository(private val apiService: EventServices) {
    suspend fun getActiveEvents() = apiService.getActiveEvents()
    suspend fun getInactiveEvents() = apiService.getInactiveEvents()
}