package com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent
import com.fauzangifari.dicodingexamandroidbegin.data.repository.FavoriteEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteEventViewModel @Inject constructor(
    private val repository: FavoriteEventRepository
): ViewModel() {

    val favoriteEvents: LiveData<List<FavoriteEvent>> = repository.getAllFavoriteEvents()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun insertOrUpdateFavorite(
        eventId: Int,
        eventName: String?,
        eventMediaCover: String?,
        eventSummary: String?,
        eventOwner: String?,
        eventCity: String?,
        eventBeginTime: String?,
        eventEndTime: String?,
        eventQuota: Int?,
        eventRegistrants: Int?,
        eventDescription: String?,
        eventLink: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val favoriteEvent = FavoriteEvent(
                eventId, eventName, eventSummary, eventOwner, eventCity,
                eventBeginTime, eventEndTime, eventQuota, eventRegistrants,
                eventDescription, eventLink, eventMediaCover
            )
            repository.insert(favoriteEvent)
            _isLoading.value = false
        }
    }

    suspend fun isFavorite(eventId: Int): Boolean {
        return repository.getFavoriteEventById(eventId) != null
    }

    fun deleteFavorite(eventId: Int?) {
        viewModelScope.launch {
            _isLoading.value = true
            eventId?.let {
                repository.delete(FavoriteEvent(it))
            }
            _isLoading.value = false
        }
    }
}

