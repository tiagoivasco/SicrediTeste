package com.ivasco.sicrediteste.view

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivasco.sicrediteste.MainActivity
import com.ivasco.sicrediteste.R
import com.ivasco.sicrediteste.databinding.FragmentHomeBinding
import com.ivasco.sicrediteste.model.Events
import com.ivasco.sicrediteste.view.adapter.EventsAdapter
import com.ivasco.sicrediteste.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel = HomeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        homeViewModel.getEvents()
        eventsResponse()

        val main = activity as MainActivity
        main.setToolbarTitle("Lista Eventos")
    }

    private fun eventsResponse() {
        homeViewModel.eventsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ArrayList<Events> -> setupRecyclerView(response)
            }
        })
    }

    private fun setupRecyclerView(events: ArrayList<Events>) {
        binding.rviewMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rviewMain.adapter = EventsAdapter(events)
    }
}