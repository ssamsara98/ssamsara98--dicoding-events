package com.ssamsara98.dicodingevents.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ssamsara98.dicodingevents.R

class DailyReminderWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    companion object {
        // private val TAG = DailyReminderWorker::class.java.simpleName
        const val WORK_NAME = "daily_reminder"
        const val CHANNEL_ID = "channel_daily_reminder"
        const val CHANNEL_NAME = "Dicoding Events Daily Reminder"
        const val NOTIFICATION_ID = 1
    }

    private var resultStatus: Result? = null

    override suspend fun doWork(): Result {
        val api = ApiConfig.getApiService()

        try {
            val response = api.getEventList(-1, 1)
            resultStatus = response.let {
                val event = it.listEvents[0]
                showNotification(event.name, event.beginTime)
                Result.success()
            }
        } catch (e: Exception) {
            showNotification("Get Current Event Failed", e.message.toString())
            resultStatus = Result.failure()
        }

        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            notification.setChannelId(CHANNEL_ID)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}