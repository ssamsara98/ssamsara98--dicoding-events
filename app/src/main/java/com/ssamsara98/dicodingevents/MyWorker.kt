package com.ssamsara98.dicodingevents

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ssamsara98.dicodingevents.data.response.EventsResponse
import cz.msebera.android.httpclient.Header

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private val TAG = MyWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getCurrentEvent()
    }

    private fun getCurrentEvent(): Result {
        Log.d(TAG, "getCurrentEvent: Mulai.....")
        Looper.prepare()
        val client = SyncHttpClient()
        // val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$APP_ID"
        val url = "${BuildConfig.BASE_URL}/events?active=-1&limit=1"
        Log.d(TAG, "getCurrentEvent: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header?>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val jsonAdapter = moshi.adapter(EventsResponse::class.java)
                    val response = jsonAdapter.fromJson(result)

                    response?.let {
                        // val currentWeather = it.weatherList[0].main
                        // val description = it.weatherList[0].description
                        // val tempInKelvin = it.main.temperature
                        //
                        // val tempInCelsius = tempInKelvin - 273
                        // val temperature: String = DecimalFormat("##.##").format(tempInCelsius)
                        // val title = "Current Weather in $city"
                        // val message = "$currentWeather, $description with $temperature celsius"

                        // showNotification(title, message)

                        val event = it.listEvents[0]
                        showNotification(event.name, event.beginTime)
                    }
                    Log.d(TAG, "onSuccess: Selesai.....")
                    resultStatus = Result.success()
                } catch (e: Exception) {
                    showNotification("Get Current Event Not Success", e.message)
                    Log.d(TAG, "onSuccess: Gagal.....")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header?>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d(TAG, "onFailure: Gagal.....")
                // ketika proses gagal, maka jobFinished diset dengan parameter true. Yang artinya job perlu di reschedule
                showNotification("Get Current Event Failed", error.message)
                resultStatus = Result.failure()
            }
        })
        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}