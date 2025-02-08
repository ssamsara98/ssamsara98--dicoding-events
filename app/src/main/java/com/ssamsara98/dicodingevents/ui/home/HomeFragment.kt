package com.ssamsara98.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.databinding.FragmentHomeBinding
import com.ssamsara98.dicodingevents.response.EventItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        binding.root.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) {
                    homeViewModel.load()
                    binding.root.isRefreshing = false
                }
            }
        }

        homeViewModel.isLoadingUpcoming.observe(viewLifecycleOwner) {
            showUpcomingLoading(it)
        }
        homeViewModel.upcomingSnackBarTextFailed.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { content ->
                Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
            }
        }
        homeViewModel.upcomingEventList.observe(viewLifecycleOwner) {
            setUpcomingEventList(it)
        }

        homeViewModel.isLoadingFinished.observe(viewLifecycleOwner) {
            showFinishedLoading(it)
        }
        homeViewModel.finishedSnackBarTextFailed.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { content ->
                Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
            }
        }
        homeViewModel.finishedEventList.observe(viewLifecycleOwner) {
            setFinishedEventList(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showUpcomingLoading(isLoading: Boolean) {
        binding.upcomingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUpcomingEventList(upcomingEventList: List<EventItem>) {
        val upcomingLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.layoutManager = upcomingLayoutManager

        val upcomingEventItemAdapter = UpcomingEventItemAdapter()
        upcomingEventItemAdapter.apply {
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

        binding.rvUpcomingEvents.adapter = upcomingEventItemAdapter
    }

    private fun showFinishedLoading(isLoading: Boolean) {
        binding.finishedProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setFinishedEventList(finishedEventList: List<EventItem>) {
        val finishedLayoutManager = LinearLayoutManager(context)
        binding.rvFinishedEvents.layoutManager = finishedLayoutManager
        val finishedItemDecoration =
            DividerItemDecoration(context, finishedLayoutManager.orientation)
        binding.rvFinishedEvents.addItemDecoration(finishedItemDecoration)

        val finishedEventItemAdapter = FinishedEventItemAdapter()
        finishedEventItemAdapter.apply {
            this.submitList(finishedEventList)
            this.setOnItemClickCallback(object :
                FinishedEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: EventItem) {
                    val toEventDetailActivity =
                        HomeFragmentDirections.actionNavigationHomeToEventDetailActivity()
                    toEventDetailActivity.eventItem = data
                    view.findNavController().navigate(toEventDetailActivity)
                }
            })
        }

        binding.rvFinishedEvents.adapter = finishedEventItemAdapter
    }
}