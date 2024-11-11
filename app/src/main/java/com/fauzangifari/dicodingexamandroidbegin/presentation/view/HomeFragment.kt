package com.fauzangifari.dicodingexamandroidbegin.presentation.view

import EventListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzangifari.dicodingexamandroidbegin.databinding.FragmentHomeBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activeEventsAdapter: EventListAdapter
    private lateinit var nonActiveEventsAdapter: EventListAdapter
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()

        observeViewModel()

        observeLoadingStatus()
    }

    private fun setupRecyclerViews() {
        activeEventsAdapter = EventListAdapter()
        nonActiveEventsAdapter = EventListAdapter()

        binding.upcomingEventsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = activeEventsAdapter
        }

        binding.finishedEventsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = nonActiveEventsAdapter
        }
    }

    private fun observeViewModel() {
        eventViewModel.nonActiveEvents.observe(viewLifecycleOwner) { nonActiveEvents ->
            nonActiveEventsAdapter.setDataFromApi(nonActiveEvents)
        }

        eventViewModel.activeEvents.observe(viewLifecycleOwner) { activeEvents ->
            activeEventsAdapter.setDataFromApi(activeEvents)
        }
    }

    private fun observeLoadingStatus() {
        eventViewModel.isActiveEventsLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarActive.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        eventViewModel.isNonActiveEventsLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarNonActive.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
