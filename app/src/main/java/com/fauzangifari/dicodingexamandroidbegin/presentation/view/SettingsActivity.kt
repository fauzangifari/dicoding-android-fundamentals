package com.fauzangifari.dicodingexamandroidbegin.presentation.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.fauzangifari.dicodingexamandroidbegin.R
import com.fauzangifari.dicodingexamandroidbegin.data.model.ThemePreference
import com.fauzangifari.dicodingexamandroidbegin.data.model.dataStore
import com.fauzangifari.dicodingexamandroidbegin.databinding.ActivitySettingsBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.EventViewModel
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.ThemeViewModel
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.ThemeViewModelFactory
import com.fauzangifari.dicodingexamandroidbegin.util.EventReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(ThemePreference.getInstance(application.dataStore)))[ThemeViewModel::class.java]

        setupThemeSwitch()
        setupNotificationSwitch()
    }

    private fun setupThemeSwitch() {
        val switchTheme = binding.switchTheme

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) {
                switchTheme.isChecked = true
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                switchTheme.isChecked = false
                AppCompatDelegate.MODE_NIGHT_NO
            })
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setupNotificationSwitch() {
        val switchNotification = binding.switchNotification
        switchNotification.isChecked = sharedPreferences.getBoolean("notification_enabled", false)

        switchNotification.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                handleNotificationEnabled()
            } else {
                handleNotificationDisabled()
            }
        }
    }

    private fun handleNotificationEnabled() {
        eventViewModel.oneEvents.observe(this) { oneEvents ->
            if (oneEvents.isNotEmpty()) {
                val eventTitle = oneEvents[0].name ?: "Event"
                val eventTime = oneEvents[0].beginTime ?: "00:00"
                scheduleReminderEvent(eventTitle, eventTime)
                sharedPreferences.edit().putBoolean("notification_enabled", true).apply()
                Toast.makeText(this, "Reminder Notification Active!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No upcoming events available!", Toast.LENGTH_SHORT).show()
                binding.switchNotification.isChecked = false
            }
        }
    }

    private fun handleNotificationDisabled() {
        WorkManager.getInstance(applicationContext).cancelAllWork()
        sharedPreferences.edit().putBoolean("notification_enabled", false).apply()
        Toast.makeText(this, "Reminder Notification Non Active!", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleReminderEvent(eventName: String, eventTime: String) {
        val data = Data.Builder()
            .putString("event_name", eventName)
            .putString("event_time", eventTime)
            .build()

        val reminderRequest: WorkRequest = PeriodicWorkRequestBuilder<EventReminderWorker>(1, TimeUnit.DAYS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(reminderRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
