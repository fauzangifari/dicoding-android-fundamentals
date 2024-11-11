package com.fauzangifari.dicodingexamandroidbegin.data.repository

import androidx.lifecycle.LiveData
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent
import com.fauzangifari.dicodingexamandroidbegin.data.local.room.FavoriteEventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteEventRepository(private val favoriteEventDao: FavoriteEventDao) {

    suspend fun insert(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.insertOrUpdate(favoriteEvent)
        }
    }

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.delete(favoriteEvent.id)
        }
    }

    suspend fun getFavoriteEventById(id: Int): FavoriteEvent? {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.getFavoriteEvent(id)
        }
    }

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return favoriteEventDao.getAllFavoriteEvents()
    }

}