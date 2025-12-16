package com.example.obstacleracegame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import com.example.obstacleracegame.logic.GameManager
import com.example.obstacleracegame.utilities.Constants.GameLogic.COLS
import com.example.obstacleracegame.utilities.Constants.GameLogic.LIVES_OFFSET
import com.example.obstacleracegame.utilities.Constants.GameLogic.ROWS
import com.example.obstacleracegame.utilities.Constants.GameLogic.YES_GRIM
import com.example.obstacleracegame.utilities.GameTimer
import com.example.obstacleracegame.utilities.SignalManager
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right:MaterialButton
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_grims: Array<Array<AppCompatImageView>>
    private lateinit var main_IMG_dogs: Array<AppCompatImageView>
    private lateinit var gameManager: GameManager
    private lateinit var gameTimer: GameTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        SignalManager.init(this)
        initViews()
        gameTimer = GameTimer {
            updateGameTick()
        }

    }

    private fun findViews() {
        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_IMG_grims = arrayOf(
            arrayOf(findViewById(R.id.main_IMG_grim00),
                findViewById(R.id.main_IMG_grim10),
                findViewById(R.id.main_IMG_grim20)),

            arrayOf(findViewById(R.id.main_IMG_grim01),
                findViewById(R.id.main_IMG_grim11),
                findViewById(R.id.main_IMG_grim21)),

            arrayOf(findViewById(R.id.main_IMG_grim02),
                findViewById(R.id.main_IMG_grim12),
                findViewById(R.id.main_IMG_grim22)),

            arrayOf(findViewById(R.id.main_IMG_grim03),
                findViewById(R.id.main_IMG_grim13),
                findViewById(R.id.main_IMG_grim23)),

            arrayOf(findViewById(R.id.main_IMG_grim04),
                findViewById(R.id.main_IMG_grim14),
                findViewById(R.id.main_IMG_grim24))
        )
        main_IMG_dogs = arrayOf(
            findViewById(R.id.main_IMG_dog0),
            findViewById(R.id.main_IMG_dog1),
            findViewById(R.id.main_IMG_dog2)
        )
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
    }

    private fun initViews() {
        main_BTN_left.setOnClickListener { moveDog(-1) }
        main_BTN_right.setOnClickListener { moveDog(1) }
        refreshUI()
    }
    private fun moveDog(direction: Int) {
        gameManager.moveDog(direction)
        refreshUI()
    }

    private fun updateGameTick() {
        if (gameManager.checkCrash()) {
            SignalManager.getInstance().vibrate()
            SignalManager.getInstance().toast("Life Lost !")
            updateLivesUI()
        }
        gameManager.updateGame()
        refreshUI()

        if (gameManager.isGameOver) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show()
            gameTimer.stop()

            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                gameManager.restartGame()
                for (heart in main_IMG_hearts) {
                    heart.visibility = View.VISIBLE
                }
                gameTimer.start()
            }, 5300)
        }
    }

    override fun onResume() {
        super.onResume()
        gameTimer.start()
    }

    override fun onPause() {
        super.onPause()
        gameTimer.stop()
    }
    private fun refreshUI() {
        for (i in main_IMG_dogs.indices) {
            val dog = main_IMG_dogs[i]

            dog.setImageResource(R.drawable.dog)
            if (i == gameManager.dogStartIndex) {
                dog.visibility = View.VISIBLE
            } else {
                dog.visibility = View.INVISIBLE
            }
        }
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                if (gameManager.gameGrid[r][c] == YES_GRIM) {
                    main_IMG_grims[r][c].visibility = View.VISIBLE
                } else {
                    main_IMG_grims[r][c].visibility = View.INVISIBLE
                }
            }
        }
    }
    private fun updateLivesUI() {
        if (gameManager.crashes != 0) {
            val heartIndex = gameManager.crashes - LIVES_OFFSET
            if (heartIndex >= 0 && heartIndex < main_IMG_hearts.size) {
                main_IMG_hearts[heartIndex].visibility = View.INVISIBLE
            }
        }
    }
}