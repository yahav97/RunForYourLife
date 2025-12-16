package com.example.obstacleracegame.utilities


import android.os.Looper
class GameTimer(private val wantedFunction: () -> Unit) {
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var isRunning = false
    private val delay = 500L

    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                wantedFunction()
                handler.postDelayed(this, delay)
            }
        }
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            handler.post(runnable)
        }
    }
    fun stop() {
        isRunning = false
        handler.removeCallbacks(runnable)
    }
}