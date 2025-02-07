package com.ssamsara98.dicodingevents.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ssamsara98.dicodingevents.databinding.ActivityEventDetailBinding
import com.ssamsara98.dicodingevents.response.EventItem

class EventDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailBinding

    private val detailEventViewModel by viewModels<EventDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.actionBar?.hide()

        detailEventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailEventViewModel.eventItem.observe(this) {
            setEventItem(it)
        }

        val extras = intent.extras
        val args =
            if (extras != null) EventDetailActivityArgs.fromBundle(extras)
            else null
        val eventItem = args?.eventItem
        if (eventItem != null) {
            detailEventViewModel.changeEventItem(eventItem)
        }
    }

    // event detail
    private fun showLoading(isLoading: Boolean) {
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
        // binding.tvBeginTime.text = eventItem.beginTime
        // binding.tvEndTime.text = eventItem.endTime
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