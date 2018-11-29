package com.example.rostrzep.googlebooksapp

import android.app.Application
import com.example.rostrzep.googlebooksapp.di.appModule
import com.example.rostrzep.googlebooksapp.di.networkModule
import com.example.rostrzep.googlebooksapp.di.presenterModule
import org.koin.android.ext.android.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin(this, listOf(
                appModule,
                presenterModule,
                networkModule))
    }
}