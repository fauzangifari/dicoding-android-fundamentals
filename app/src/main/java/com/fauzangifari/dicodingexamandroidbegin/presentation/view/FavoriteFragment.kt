package com.fauzangifari.dicodingexamandroidbegin.presentation.view

import EventListAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzangifari.dicodingexamandroidbegin.databinding.FragmentFavoriteBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.FavoriteEventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var eventListAdapter: EventListAdapter
    private val favoriteEventViewModel: FavoriteEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventListAdapter = EventListAdapter()

        binding.favoriteEventsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventListAdapter
        }

        favoriteEventViewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            eventListAdapter.setDataFromDb(favoriteEvents)
        }

        favoriteEventViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}

