package com.example.obstacleracegame.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.obstacleracegame.R
import com.example.obstacleracegame.utilities.Constants
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_BTN_sensors: MaterialButton
    private lateinit var menu_BTN_fast: MaterialButton
    private lateinit var menu_BTN_slow: MaterialButton
    private lateinit var menu_BTN_highScores: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_sensors = findViewById(R.id.menu_BTN_sensors)
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast)
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow)
        menu_BTN_highScores = findViewById(R.id.menu_BTN_highScores)
    }

    private fun initViews() {
        menu_BTN_sensors.setOnClickListener {
            startGame(useSensors = true, delay = Constants.DELAY.MID)
        }
        menu_BTN_fast.setOnClickListener {
            startGame(useSensors = false, delay = Constants.DELAY.FAST)
        }
        menu_BTN_slow.setOnClickListener {
            startGame(useSensors = false, delay = Constants.DELAY.SLOW)
        }
        menu_BTN_highScores.setOnClickListener {
            showHighScores()
        }

    }

    private fun startGame(useSensors: Boolean, delay: Long) {
        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra(Constants.KEYS.KEY_SENSORS, useSensors)
        intent.putExtra(Constants.KEYS.KEY_DELAY, delay)
        startActivity(intent)
    }

    private fun showHighScores() {
        val intent = Intent(this, TopScoresActivity::class.java)
        startActivity(intent)
    }
}