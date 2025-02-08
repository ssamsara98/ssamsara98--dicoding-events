package com.ssamsara98.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ssamsara98.dicodingevents.databinding.ActivityEventDetailBinding
import com.ssamsara98.dicodingevents.response.EventItem
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

        binding.root.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) {
                    detailEventViewModel.fetchEvent()
                    binding.root.isRefreshing = false
                }
            }
        }

        detailEventViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailEventViewModel.eventItem.observe(this) {
            setEventItem(it)
        }
        detailEventViewModel.snackBarTextFailed.observe(this) { snackBarTextFailed ->
            snackBarTextFailed.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding.root, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
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
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutMain.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setEventItem(eventItem: EventItem?) {
        if (eventItem == null) return

        Glide.with(this).load(eventItem.mediaCover).into(binding.ivMediaCover)

        val quota = "Quota: ${eventItem.quota}"
        val registrants = "Pendaftar: ${eventItem.registrants}"
        val quotaRemain = "Sisa Kuota: ${(eventItem.quota - eventItem.registrants)}"

        val time = "${eventItem.beginTime} - ${eventItem.endTime}"
        val owner = "Penyelenggara: ${eventItem.ownerName}"
        val location = "Lokasi: ${eventItem.cityName}"

        binding.tvName.text = eventItem.name
        binding.tvSummary.text = eventItem.summary
        binding.tvCategory.text = eventItem.category
        binding.tvQuota.text = quota
        binding.tvRegistrants.text = registrants
        binding.tvQuotaRemain.text = quotaRemain
        binding.tvTime.text = time
        binding.tvOwner.text = owner
        binding.tvLocation.text = location
        binding.tvDescription.text =
            Html.fromHtml(eventItem.description, Html.FROM_HTML_MODE_LEGACY)

        binding.btnRegister.setOnClickListener {
            val registerUrl = Intent(Intent.ACTION_VIEW, Uri.parse(eventItem.link))
            this.startActivity(registerUrl)
        }
    }
}