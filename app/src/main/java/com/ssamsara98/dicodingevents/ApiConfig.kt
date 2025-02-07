package com.ssamsara98.dicodingevents;

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            // val loggingInterceptor = if(BuildConfig.DEBUG) {
            //     HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            // } else {
            //     HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            // }
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            // val retrofit = Retrofit.Builder().baseUrl("https://restaurant-api.dicoding.dev/")
            val retrofit = Retrofit.Builder().baseUrl("https://event-api.dicoding.dev")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
