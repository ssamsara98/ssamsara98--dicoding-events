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
import com.ssamsara98.dicodingevents.databinding.ActivityEventDetailBinding
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailBinding

    private val detailEventViewModel by viewModels<EventDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Event Detail"
            subtitle = "this page is showing the detail of some event"
            setDisplayHomeAsUpEnabled(true)
        }

        val extras = intent.extras
        val args =
            if (extras != null) EventDetailActivityArgs.fromBundle(extras)
            else null
        val eventItem = args?.eventItem
        if (eventItem != null) {
            lifecycleScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) {
                    detailEventViewModel.changeEventItem(eventItem)
                }
            }

            binding.swipeRefresh.setOnRefreshListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        detailEventViewModel.fetchEvent(eventItem.id.toString())
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }

        detailEventViewModel.eventItem.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    setEventItem(it.data)
                }

                is Resource.Error -> {
                    it.error.getContentIfNotHandled()?.let { content ->
                        Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            layoutMain.visibility = if (isLoading) View.GONE else View.VISIBLE
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

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
            tvDescription.text =
                Html.fromHtml(eventItem.description, Html.FROM_HTML_MODE_LEGACY)

            btnRegister.setOnClickListener {
                val registerUrl = Intent(Intent.ACTION_VIEW, eventItem.link.toUri())
                this@EventDetailActivity.startActivity(registerUrl)
            }
        }
    }
}