package com.ssamsara98.dicodingevents.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssamsara98.dicodingevents.databinding.FragmentUpcomingBinding
import com.ssamsara98.dicodingevents.response.EventItem

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.showEvents()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Dicoding Events
    private fun showEvents() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        upcomingViewModel.eventList.observe(viewLifecycleOwner) { event ->
            setEventList(event)
        }
    }

    private fun setEventList(upcomingEventItemList: List<EventItem>) {
        val upcomingEventItemAdapter = UpcomingEventItemAdapter()
        upcomingEventItemAdapter.submitList(upcomingEventItemList)
        upcomingEventItemAdapter.setOnItemClickCallback(object : UpcomingEventItemAdapter.OnItemClickCallback {
            override fun onItemClicked(view: View, data: EventItem) {
                val toEventDetailActivity =
                    UpcomingFragmentDirections.actionNavigationUpcomingToEventDetailActivity()
                toEventDetailActivity.eventItem = data
                view.findNavController().navigate(toEventDetailActivity)
            }
        })
        binding.rvEvents.adapter = upcomingEventItemAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}