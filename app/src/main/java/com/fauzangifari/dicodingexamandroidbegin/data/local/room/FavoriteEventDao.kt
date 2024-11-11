package com.fauzangifari.dicodingexamandroidbegin.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent

@Dao
interface FavoriteEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    suspend fun getFavoriteEvent(eventId: Int): FavoriteEvent?

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun delete(eventId: Int)

    @Query("SELECT * FROM events")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>
}
