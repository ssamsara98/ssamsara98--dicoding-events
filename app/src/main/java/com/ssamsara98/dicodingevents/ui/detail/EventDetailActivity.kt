package com.ssamsara98.dicodingevents.ui.detail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.R
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.databinding.ActivityEventDetailBinding
import com.ssamsara98.dicodingevents.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity() {
    companion object {
        const val EVENT_ITEM = "EVENT_ITEM"
    }

    private lateinit var binding: ActivityEventDetailBinding

    private val eventDetailViewModel by viewModels<EventDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Event Detail"
            subtitle = "this page is showing the detail of some event"
            setDisplayHomeAsUpEnabled(true)
        }

        val args = intent.extras?.let { EventDetailActivityArgs.fromBundle(it) }

        val eventItem = args?.eventItem ?: intent.extras?.getParcelable<EventItem>(EVENT_ITEM)
        if (eventItem != null) {
            lifecycleScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) {
                    eventDetailViewModel.changeEventItem(eventItem)
                }
            }

            binding.swipeRefresh.setOnRefreshListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        eventDetailViewModel.fetchEvent(eventItem.id.toString())
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }

        eventDetailViewModel.eventItem.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    setEventItem(it.data)
                }

                is Resource.Error -> {
                    showLoading(false)
                    it.error.getContentIfNotHandled()?.let { content ->
                        Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        eventDetailViewModel.isFavorite.observe(this) {
            if (it == true) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            layoutMain.visibility = if (isLoading) View.GONE else View.VISIBLE
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun eventItemToFavoriteEventEntity(data: EventItem): FavoriteEventEntity =
        FavoriteEventEntity(
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

    private fun setEventItem(eventItem: EventItem?) {
        if (eventItem == null) return

        val quota = "Quota: ${eventItem.quota}"
        val registrants = "Pendaftar: ${eventItem.registrants}"
        val quotaRemain = "Sisa Kuota: ${(eventItem.quota - eventItem.registrants)}"

        val time = "${eventItem.beginTime} - ${eventItem.endTime}"
        val owner = "Penyelenggara: ${eventItem.ownerName}"
        val location = "Lokasi: ${eventItem.cityName}"

        with(binding) {
            Glide.with(this@EventDetailActivity).load(eventItem.mediaCover).into(ivMediaCover)
            tvName.text = eventItem.name
            tvSummary.text = eventItem.summary
            tvCategory.text = eventItem.category
            tvQuota.text = quota
            tvRegistrants.text = registrants
            tvQuotaRemain.text = quotaRemain
            tvTime.text = time
            tvOwner.text = owner
            tvLocation.text = location
            tvDescription.text = Html.fromHtml(eventItem.description, Html.FROM_HTML_MODE_LEGACY)

            btnRegister.setOnClickListener {
                val registerUrl = Intent(Intent.ACTION_VIEW, eventItem.link.toUri())
                this@EventDetailActivity.startActivity(registerUrl)
            }

            fabFavorite.setOnClickListener {
                val favoriteEventEntity = eventItemToFavoriteEventEntity(eventItem)
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        eventDetailViewModel.toggleBookmark(
                            favoriteEventEntity,
                            this@EventDetailActivity
                        )
                    }
                }
            }
        }
    }
}