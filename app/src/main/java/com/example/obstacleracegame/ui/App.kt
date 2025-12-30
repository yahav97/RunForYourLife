package com.example.obstacleracegame.ui

import android.app.Application
import com.example.obstacleracegame.utilities.SharedPreferencesManager
import com.example.obstacleracegame.utilities.SignalManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        SignalManager.init(this)
    }
}