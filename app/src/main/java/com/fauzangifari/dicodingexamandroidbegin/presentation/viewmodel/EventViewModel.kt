package com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.dicodingexamandroidbegin.data.remote.response.ListEventsItem
import com.fauzangifari.dicodingexamandroidbegin.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _activeEvents = MutableLiveData<List<ListEventsItem>>()
    val activeEvents: LiveData<List<ListEventsItem>> = _activeEvents

    private val _nonActiveEvents = MutableLiveData<List<ListEventsItem>>()
    val nonActiveEvents: LiveData<List<ListEventsItem>> = _nonActiveEvents

    private val _allEvents = MutableLiveData<List<ListEventsItem>>()
    val allEvents: LiveData<List<ListEventsItem>> = _allEvents

    private val _oneEvents = MutableLiveData<List<ListEventsItem>>()
    val oneEvents: LiveData<List<ListEventsItem>> = _oneEvents

    private val _filteredEvents = MutableLiveData<List<ListEventsItem>>()
    val filteredEvents: LiveData<List<ListEventsItem>> = _filteredEvents

    private val _isAllEventsLoading = MutableLiveData<Boolean>()
    val isAllEventsLoading: LiveData<Boolean> =  _isAllEventsLoading

    private val _isActiveEventsLoading = MutableLiveData<Boolean>()
    val isActiveEventsLoading: LiveData<Boolean> = _isActiveEventsLoading

    private val _isNonActiveEventsLoading = MutableLiveData<Boolean>()
    val isNonActiveEventsLoading: LiveData<Boolean> = _isNonActiveEventsLoading

    init {
        getEventActive()
        getEventNonActive()
    }

    private fun getEventActive() {
        _isActiveEventsLoading.value = true
        viewModelScope.launch {
            try {
                val response = eventRepository.getActiveEvents()
                _activeEvents.value = response.listEvents?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isActiveEventsLoading.value = false
            }
        }
    }

    private fun getEventNonActive() {
        _isNonActiveEventsLoading.value = true
        viewModelScope.launch {
            try {
                val response = eventRepository.getInactiveEvents()
                _nonActiveEvents.value = response.listEvents?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {

            } finally {
                _isNonActiveEventsLoading.value = false
            }
        }
    }

    fun filterEvents(query: String) {
        val allEvents = _allEvents.value ?: return

        val filteredList = allEvents.filter { event ->
            val containsQuery = event.name?.contains(query, ignoreCase = true) ?: false
            containsQuery
        }

        _filteredEvents.value = filteredList
    }
}

