package com.fauzangifari.dicodingexamandroidbegin.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent

@Database(entities = [FavoriteEvent::class], version = 1)
abstract class FavoriteEventDatabase : RoomDatabase() {

    abstract val favoriteEventDao: FavoriteEventDao

    companion object {
        const val DATABASE_NAME = "favorite_event"
    }
}