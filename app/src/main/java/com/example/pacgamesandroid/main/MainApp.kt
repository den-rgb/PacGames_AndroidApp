package com.example.pacgamesandroid.main

import android.app.Application
import com.example.pacgamesandroid.models.GameModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val games = ArrayList<GameModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Game started")
    }
}