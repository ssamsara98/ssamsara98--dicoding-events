package com.ssamsara98.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.databinding.FragmentHomeBinding
import com.ssamsara98.dicodingevents.response.EventItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.showEvents()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEvents() {
        val upcomingLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.layoutManager = upcomingLayoutManager

        val finishedLayoutManager =
            LinearLayoutManager(context)
        binding.rvFinishedEvents.layoutManager = finishedLayoutManager
        val finishedItemDecoration =
            DividerItemDecoration(context, finishedLayoutManager.orientation)
        binding.rvFinishedEvents.addItemDecoration(finishedItemDecoration)

        homeViewModel.isLoadingUpcoming.observe(viewLifecycleOwner) {
            showUpcomingLoading(it)
        }
        homeViewModel.upcomingSnackBarTextFailed.observe(viewLifecycleOwner) { snackBarTextFailed ->
            snackBarTextFailed.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding.root, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        homeViewModel.upcomingEventList.observe(viewLifecycleOwner) {
            setUpcomingEventList(it)
        }

        homeViewModel.isLoadingFinished.observe(viewLifecycleOwner) {
            showFinishedLoading(it)
        }
        homeViewModel.finishedSnackBarTextFailed.observe(viewLifecycleOwner) { snackBarTextFailed ->
            snackBarTextFailed.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding.root, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        homeViewModel.finishedEventList.observe(viewLifecycleOwner) {
            setFinishedEventList(it)
        }
    }

    private fun showUpcomingLoading(isLoading: Boolean) {
        binding.upcomingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUpcomingEventList(upcomingEventList: List<EventItem>) {
        val eventItemAdapter = UpcomingEventItemAdapter()

        eventItemAdapter.apply {
            this.submitList(upcomingEventList)
            this.setOnItemClickCallback(object :
                UpcomingEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: EventItem) {
                    val toEventDetailActivity =
                        HomeFragmentDirections.actionNavigationHomeToEventDetailActivity()
                    toEventDetailActivity.eventItem = data
                    view.findNavController().navigate(toEventDetailActivity)
                }
            })
        }

        binding.rvUpcomingEvents.adapter = eventItemAdapter
    }

    private fun showFinishedLoading(isLoading: Boolean) {
        binding.finishedProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setFinishedEventList(upcomingEventList: List<EventItem>) {
        val eventItemAdapter = FinishedEventItemAdapter()
        eventItemAdapter.submitList(upcomingEventList)
        eventItemAdapter.setOnItemClickCallback(object :
            FinishedEventItemAdapter.OnItemClickCallback {
            override fun onItemClicked(view: View, data: EventItem) {
                val toEventDetailActivity =
                    HomeFragmentDirections.actionNavigationHomeToEventDetailActivity()
                toEventDetailActivity.eventItem = data
                view.findNavController().navigate(toEventDetailActivity)
            }
        })
        binding.rvFinishedEvents.adapter = eventItemAdapter
    }
}