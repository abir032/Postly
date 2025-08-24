package com.example.postly

import android.app.Application
import android.util.Log
import com.example.postly.data.local.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MainApplication", "!1000: OnCreate")
    }
}