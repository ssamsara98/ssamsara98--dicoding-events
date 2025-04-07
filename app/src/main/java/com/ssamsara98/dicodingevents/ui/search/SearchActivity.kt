package com.ssamsara98.dicodingevents.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.databinding.ActivitySearchBinding
import com.ssamsara98.dicodingevents.ui.detail.EventDetailActivity
import com.ssamsara98.dicodingevents.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    // private val searchViewModel by viewModels<SearchViewModel> { ViewModelFactory.getInstance(this) }
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnRefreshListener {
            binding.root.isRefreshing = false
        }

        supportActionBar?.apply {
            title = "Search Page"
            setDisplayHomeAsUpEnabled(true)
        }

        val args = intent.extras?.let { SearchActivityArgs.fromBundle(it) }

        searchViewModel.apply {
            lifecycleScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) {
                    args?.let { this@apply.fetchSearch(it.q) }
                }
            }

            this.eventList.observe(this@SearchActivity) {
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
                args?.let { this.setQuery(it.q, false) }
                this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean = true
                    override fun onQueryTextSubmit(q: String?): Boolean {
                        if (q == null || q == "") return false
                        lifecycleScope.launch(Dispatchers.Default) {
                            withContext(Dispatchers.Main) {
                                this@apply.fetchSearch(q)
                            }
                        }
                        return true
                    }
                })
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setEventList(eventItemList: List<EventItem>?) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)

        val searchEventItemAdapter = SearchEventItemAdapter()
        searchEventItemAdapter.apply {
            this.submitList(eventItemList)
            this.setOnItemClickCallback(object : SearchEventItemAdapter.OnItemClickCallback {
                override fun onItemClicked(view: View, data: EventItem) {
                    val intent = Intent(this@SearchActivity, EventDetailActivity::class.java)
                    intent.putExtra(EventDetailActivity.EVENT_ITEM, data)
                    this@SearchActivity.startActivity(intent)
                }
            })
        }

        binding.rvEvents.adapter = searchEventItemAdapter
    }
}
