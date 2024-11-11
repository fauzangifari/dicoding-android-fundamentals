package com.fauzangifari.dicodingexamandroidbegin.presentation.view

import EventListAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzangifari.dicodingexamandroidbegin.R
import com.fauzangifari.dicodingexamandroidbegin.data.model.ThemePreference
import com.fauzangifari.dicodingexamandroidbegin.data.model.dataStore
import com.fauzangifari.dicodingexamandroidbegin.databinding.ActivityResultSearchBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.EventViewModel
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.ThemeViewModel
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.ThemeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var eventListAdapter: EventListAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventListAdapter = EventListAdapter()
        binding.resultSearchRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = eventListAdapter
        }

        eventViewModel.filteredEvents.observe(this) { filteredEvents ->
            eventListAdapter.setDataFromApi(filteredEvents)

            val resultCount = eventViewModel.filteredEvents.value?.size ?: 0
            binding.resultSearchCount.text = "Result: $resultCount Found"
        }

        observeLoadingStatus()

        val query = intent.getStringExtra("query") ?: ""
        eventViewModel.allEvents.observe(this) { allEvents ->
            if (allEvents.isNotEmpty()) {
                eventViewModel.filterEvents(query)
            }
        }

        val pref = ThemePreference.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun observeLoadingStatus() {
        eventViewModel.isAllEventsLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}