package com.ssamsara98.dicodingevents.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.databinding.FragmentFinishedBinding
import com.ssamsara98.dicodingevents.response.EventItem

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val finishedViewModel by viewModels<FinishedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.showEvents()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEvents() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvEvents.layoutManager = layoutManager

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        finishedViewModel.snackBarTextFailed.observe(viewLifecycleOwner) { snackBarTextFailed ->
            snackBarTextFailed.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding.root, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }
        finishedViewModel.eventListFinished.observe(viewLifecycleOwner) {
            setEventList(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setEventList(finishedEventItemList: List<EventItem>?) {
        val finishedEventItemAdapter = FinishedEventItemAdapter()

        finishedEventItemAdapter.apply {
            this.submitList(finishedEventItemList)
            this.setOnItemClickCallback(object : FinishedEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: EventItem) {
                    val toEventDetailActivity =
                        FinishedFragmentDirections.actionNavigationFinishedToEventDetailActivity()
                    toEventDetailActivity.eventItem = data
                    view.findNavController().navigate(toEventDetailActivity)
                }
            })
        }

        binding.rvEvents.adapter = finishedEventItemAdapter
    }
}