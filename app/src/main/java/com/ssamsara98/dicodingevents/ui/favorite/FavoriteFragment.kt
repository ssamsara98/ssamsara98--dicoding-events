package com.ssamsara98.dicodingevents.ui.favorite

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
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.databinding.FragmentFavoriteBinding
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingFragmentDirections
import com.ssamsara98.dicodingevents.util.Resource
import com.ssamsara98.dicodingevents.util.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    // private val favoriteViewModel: FavoriteViewModel? by viewModels {
    //     activity?.let { ViewModelFactory.getInstance(it) }!!
    // }
    private val favoriteViewModel: FavoriteViewModel? by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // return inflater.inflate(R.layout.fragment_favorite, container, false)
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root

        favoriteViewModel?.apply {
            this.getFavoriteEventList().observe(viewLifecycleOwner) {
                if (it != null) {
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
            binding.root.setOnRefreshListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        this@apply.getFavoriteEventList()
                        binding.root.isRefreshing = false
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

    private fun favoriteEventEntityToEventItem(data: FavoriteEventEntity): EventItem = EventItem(
        data.id,
        data.name,
        data.summary,
        data.description,
        data.imageLogo,
        data.mediaCover,
        data.category,
        data.ownerName,
        data.cityName,
        data.quota,
        data.registrants,
        data.beginTime,
        data.endTime,
        data.link
    )

    private fun setEventList(favoriteEventEntityList: List<FavoriteEventEntity>?) {
        val layoutManager = LinearLayoutManager(context)
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)

        val upcomingEventItemAdapter = FavoriteEventItemAdapter().apply {
            this.submitList(favoriteEventEntityList)
            this.setOnItemClickCallback(object : FavoriteEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: FavoriteEventEntity) {
                    val toEventDetailActivity =
                        UpcomingFragmentDirections.actionNavigationUpcomingToEventDetailActivity()
                    val eventItem = favoriteEventEntityToEventItem(data)
                    toEventDetailActivity.eventItem = eventItem
                    view.findNavController().navigate(toEventDetailActivity)
                }
            })
        }

        binding.rvEvents.adapter = upcomingEventItemAdapter
    }
}