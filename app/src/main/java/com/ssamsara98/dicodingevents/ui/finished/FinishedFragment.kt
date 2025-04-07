package com.ssamsara98.dicodingevents.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.databinding.FragmentFinishedBinding
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Resource
import com.ssamsara98.dicodingevents.util.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    // private val finishedViewModel: FinishedViewModel? by viewModels {
    //     activity?.let { ViewModelFactory.getInstance(it) }!!
    // }
    private val finishedViewModel: FinishedViewModel? by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root


        finishedViewModel?.apply {
            binding.root.setOnRefreshListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        this@apply.load()
                        binding.root.isRefreshing = false
                    }
                }
            }

            this.eventListFinished.observe(/* owner = */ viewLifecycleOwner) {
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

    private fun setEventList(finishedEventItemList: List<EventItem>?) {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvEvents.layoutManager = layoutManager

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