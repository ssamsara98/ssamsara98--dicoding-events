package com.ssamsara98.dicodingevents.ui.upcoming

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
import com.ssamsara98.dicodingevents.databinding.FragmentUpcomingBinding
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Resource
import com.ssamsara98.dicodingevents.util.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val upcomingViewModel: UpcomingViewModel? by viewModels {
        activity?.let { ViewModelFactory.getInstance(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        upcomingViewModel?.apply {
            binding.root.setOnRefreshListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        this@apply.load()
                        binding.root.isRefreshing = false
                    }
                }
            }

            this.eventList.observe(viewLifecycleOwner) {
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

    private fun setEventList(upcomingEventItemList: List<EventItem>?) {
        val layoutManager = LinearLayoutManager(context)
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)

        val upcomingEventItemAdapter = UpcomingEventItemAdapter()
        upcomingEventItemAdapter.apply {
            this.submitList(upcomingEventItemList)
            this.setOnItemClickCallback(object :
                UpcomingEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: EventItem) {
                    val toEventDetailActivity =
                        UpcomingFragmentDirections.actionNavigationUpcomingToEventDetailActivity()
                    toEventDetailActivity.eventItem = data
                    view.findNavController().navigate(toEventDetailActivity)
                }
            })
        }

        binding.rvEvents.adapter = upcomingEventItemAdapter
    }
}