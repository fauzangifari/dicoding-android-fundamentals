package com.fauzangifari.dicodingexamandroidbegin.presentation.view

import EventListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzangifari.dicodingexamandroidbegin.databinding.FragmentFinishedBinding
import com.fauzangifari.dicodingexamandroidbegin.presentation.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishedFragment : Fragment() {
    private lateinit var binding: FragmentFinishedBinding
    private lateinit var eventListAdapter: EventListAdapter
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventListAdapter = EventListAdapter()
        binding.finishedEventsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = eventListAdapter
        }

        eventViewModel.nonActiveEvents.observe(viewLifecycleOwner) { nonActiveEvents ->
            eventListAdapter.setDataFromApi(nonActiveEvents)
        }

        eventViewModel.isNonActiveEventsLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

    }
}