package com.fauzangifari.dicodingexamandroidbegin.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fauzangifari.dicodingexamandroidbegin.R
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent
import com.fauzangifari.dicodingexamandroidbegin.data.model.ThemePreference
import com.fauzangifari.dicodingexamandroidbegin.data.model.dataStore
import com.fauzangifari.dicodingexamandroidbegin.data.remote.response.ListEventsItem
import com.fauzangifari.dicodingexamandroidbegin.databinding.ActivityDetailBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.FavoriteEventViewModel
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.ThemeViewModel
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.ThemeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: FavoriteEventViewModel by viewModels()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val event: Parcelable? = intent.parcelable("event")

        when (event) {
            is ListEventsItem -> {
                setupUI(event)
                checkIfFavorite(event.id)
            }

            is FavoriteEvent -> {
                setupUI(event)
                checkIfFavorite(event.id)
            }

            else -> {
                Toast.makeText(this, "Event tidak tersedia", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val pref = ThemePreference.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    private fun setupUI(event: ListEventsItem) {
        with(binding) {
            titleDetails.text = event.name
            summaryDetails.text = event.summary
            ownerNameDetails.text = event.ownerName
            cityNameDetails.text = event.cityName
            beginTimeDetails.text = event.beginTime
            endTimeDetails.text = event.endTime

            val remainingQuota = (event.quota ?: 0) - (event.registrants ?: 0)
            quotaDetails.text = remainingQuota.toString()

            descriptionDetails.text = event.description?.let {
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            linkButton.setOnClickListener {
                event.link?.let { link ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                } ?: run {
                    Toast.makeText(this@DetailActivity, "Link tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }

            event.mediaCover?.let { mediaCoverUrl ->
                Glide.with(this@DetailActivity)
                    .load(mediaCoverUrl)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(mediaCoverDetails)
            }

            favoriteIconDetails.setOnClickListener {
                toggleFavorite(event)
            }
        }
    }

    private fun setupUI(event: FavoriteEvent) {
        with(binding) {
            titleDetails.text = event.name
            summaryDetails.text = event.summary
            ownerNameDetails.text = event.ownerName
            cityNameDetails.text = event.cityName
            beginTimeDetails.text = event.beginTime
            endTimeDetails.text = event.endTime

            val remainingQuota = (event.quota ?: 0) - (event.registrants ?: 0)
            quotaDetails.text = remainingQuota.toString()

            descriptionDetails.text = event.description?.let {
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            event.mediaCover?.let { mediaCoverUrl ->
                Glide.with(this@DetailActivity)
                    .load(mediaCoverUrl)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(mediaCoverDetails)
            }

            favoriteIconDetails.setOnClickListener {
                toggleFavorite(event)
            }
        }
    }

    private fun toggleFavorite(event: ListEventsItem) {
        isFavorite = !isFavorite
        if (isFavorite) {
            event.id?.let {
                viewModel.insertOrUpdateFavorite(
                    it, event.name, event.mediaCover, event.summary,
                    event.ownerName, event.cityName, event.beginTime, event.endTime,
                    event.quota, event.registrants, event.description, event.link
                )
            }
        } else {
            viewModel.deleteFavorite(event.id)
        }
        updateFavoriteIcon()
        Toast.makeText(this, if (isFavorite) "Favorited" else "Removed from favorites", Toast.LENGTH_SHORT).show()
    }

    private fun toggleFavorite(event: FavoriteEvent) {
        isFavorite = !isFavorite
        if (isFavorite) {
            Toast.makeText(this, "Event sudah ada dalam favorit", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.deleteFavorite(event.id)
        }
        updateFavoriteIcon()
        Toast.makeText(this, if (isFavorite) "Favorited" else "Removed from favorites", Toast.LENGTH_SHORT).show()
    }

    private fun checkIfFavorite(eventId: Int?) {
        lifecycleScope.launch {
            isFavorite = eventId?.let { viewModel.isFavorite(it) } == true
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        binding.favoriteIconDetails.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_24
            else R.drawable.ic_favorite_border_24
        )
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

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

