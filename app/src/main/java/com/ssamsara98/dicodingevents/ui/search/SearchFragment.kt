package com.ssamsara98.dicodingevents.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.databinding.FragmentSearchBinding
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Resource

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.root.setOnRefreshListener {
            binding.root.isRefreshing = false
        }

        searchViewModel.eventList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    setEventList(it.data)
                }

                is Resource.Error -> {
                    showLoading(false)
                    it.error.getContentIfNotHandled()?.let { content ->
                        Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(binding.svQuery) {
            this.isSubmitButtonEnabled = true

            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean = true
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query == null) return false
                    searchViewModel.fetchSearch(query)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setEventList(eventItemList: List<EventItem>?) {
        val layoutManager = LinearLayoutManager(context)
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)

        val searchEventItemAdapter = SearchEventItemAdapter()
        searchEventItemAdapter.apply {
            this.submitList(eventItemList)
            this.setOnItemClickCallback(object : SearchEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: EventItem) {
                    val toEventDetailActivity =
                        SearchFragmentDirections.actionNavigationSearchToEventDetailActivity()
                    toEventDetailActivity.eventItem = data
                    view.findNavController().navigate(toEventDetailActivity)
                }
            })
        }

        binding.rvEvents.adapter = searchEventItemAdapter
    }
}