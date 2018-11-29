package com.example.rostrzep.googlebooksapp.di

import android.content.Context
import android.util.Log

import com.example.rostrzep.googlebooksapp.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

import okhttp3.OkHttpClient
import org.koin.dsl.module.module

val appModule = module {
    single { createPicasso(get()) }
    single { createGson() }
}

fun createPicasso(context: Context): Picasso {
    val okHttpClient = OkHttpClient.Builder()

    return Picasso.Builder(context)
            .indicatorsEnabled(BuildConfig.DEBUG)
            .loggingEnabled(BuildConfig.DEBUG)
            .listener { picasso, uri, exception ->
                if (BuildConfig.DEBUG) {
                    Log.e("picasso", uri.toString(), exception)
                }
            }
            .downloader(OkHttp3Downloader(okHttpClient.build()))
            .build()
}

fun createGson(): Gson {
    return GsonBuilder()
            .create()
}
