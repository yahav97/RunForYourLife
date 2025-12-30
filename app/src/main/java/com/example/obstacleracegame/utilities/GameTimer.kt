package com.example.obstacleracegame.utilities

import android.os.Handler
import android.os.Looper


class GameTimer(private val delay: Long, private val wantedFunction: () -> Unit) {

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

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