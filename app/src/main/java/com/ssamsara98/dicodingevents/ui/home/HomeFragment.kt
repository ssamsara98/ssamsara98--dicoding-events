package com.ssamsara98.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.databinding.FragmentHomeBinding
import com.ssamsara98.dicodingevents.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val homeViewModel: HomeViewModel? by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel?.apply {
            binding.root.setOnRefreshListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        load()
                        binding.root.isRefreshing = false
                    }
                }
            }

            upcomingEventList.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        showUpcomingLoading(true)
                    }

                    is Resource.Success -> {
                        showUpcomingLoading(false)
                        setUpcomingEventList(it.data)
                    }

                    is Resource.Error -> {
                        showUpcomingLoading(false)
                        it.error.getContentIfNotHandled()?.let { content ->
                            Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            finishedEventList.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        showFinishedLoading(true)
                    }

                    is Resource.Success -> {
                        showFinishedLoading(false)
                        setFinishedEventList(it.data)
                    }

                    is Resource.Error -> {
                        showFinishedLoading(false)
                        it.error.getContentIfNotHandled()?.let { content ->
                            Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        with(binding.svQuery) {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean = true
                override fun onQueryTextSubmit(q: String?): Boolean {
                    if (q == null || q == "") return false
                    val toSearchActivity =
                        HomeFragmentDirections.actionNavigationHomeToSearchActivity(q)
                    view?.findNavController()?.navigate(toSearchActivity)
                    return true
                }
            })
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

    private fun setUpcomingEventList(upcomingEventList: List<EventItem>?) {
        val upcomingLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.layoutManager = upcomingLayoutManager

        val upcomingEventItemAdapter = UpcomingEventItemAdapter()
        upcomingEventItemAdapter.apply {
            submitList(upcomingEventList)
            setOnItemClickCallback(object :
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

    private fun setFinishedEventList(finishedEventList: List<EventItem>?) {
        val finishedLayoutManager = LinearLayoutManager(context)
        binding.rvFinishedEvents.layoutManager = finishedLayoutManager
        val finishedItemDecoration =
            DividerItemDecoration(context, finishedLayoutManager.orientation)
        binding.rvFinishedEvents.addItemDecoration(finishedItemDecoration)

        val finishedEventItemAdapter = FinishedEventItemAdapter()
        finishedEventItemAdapter.apply {
            submitList(finishedEventList)
            setOnItemClickCallback(object :
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