package com.example.pacgamesandroid.main

import android.app.Application
import com.example.pacShopsandroid.models.ShopMemStore
import com.example.pacgamesandroid.adapters.GameAdapter
import com.example.pacgamesandroid.models.GameMemStore
import com.example.pacgamesandroid.models.GameModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val games = GameMemStore()


//    val shops = ShopMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Game started")
    }
}