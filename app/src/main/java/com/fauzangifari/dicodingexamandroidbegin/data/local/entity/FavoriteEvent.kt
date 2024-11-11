package com.fauzangifari.dicodingexamandroidbegin.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "events")
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var name: String? = null,
    var summary: String? = null,
    var ownerName: String? = null,
    var cityName: String? = null,
    var beginTime: String? = null,
    var endTime: String? = null,
    var quota: Int? = null,
    var registrants: Int? = null,
    var description: String? = null,
    var link: String? = null,
    var mediaCover : String? = null,
) : Parcelable