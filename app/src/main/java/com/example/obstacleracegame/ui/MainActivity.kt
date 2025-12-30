package com.example.obstacleracegame.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obstacleracegame.R
import com.example.obstacleracegame.interfaces.TiltCallback
import com.example.obstacleracegame.logic.GameManager
import com.example.obstacleracegame.model.ScoreList
import com.example.obstacleracegame.utilities.Constants
import com.example.obstacleracegame.utilities.GameTimer
import com.example.obstacleracegame.utilities.SharedPreferencesManager
import com.example.obstacleracegame.utilities.SignalManager
import com.example.obstacleracegame.utilities.SingleSoundPlayer
import com.example.obstacleracegame.utilities.TiltDetector
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    private lateinit var main_buttons_layout: LinearLayoutCompat
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton
    private lateinit var main_LBL_score: MaterialTextView
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_grims: Array<Array<AppCompatImageView>>
    private lateinit var main_IMG_dogs: Array<AppCompatImageView>
    private lateinit var gameManager: GameManager
    private lateinit var gameTimer: GameTimer
    private lateinit var tiltDetector: TiltDetector
    private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient
    private var useSensors: Boolean = false
    private var delay: Long = 0L

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        delay = intent.getLongExtra(Constants.KEYS.KEY_DELAY, Constants.DELAY.FAST)
        useSensors = intent.getBooleanExtra(Constants.KEYS.KEY_SENSORS, false)

        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()

        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        SignalManager.init(this)
        initViews()
        initTiltDetector()

        gameTimer = GameTimer(delay) {
            updateGameTick()
        }
    }

    private fun findViews() {
        main_buttons_layout = findViewById(R.id.main_buttons)
        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_LBL_score = findViewById(R.id.main_LBL_score)

        main_IMG_dogs = arrayOf(
            findViewById(R.id.main_IMG_dog0),
            findViewById(R.id.main_IMG_dog1),
            findViewById(R.id.main_IMG_dog2),
            findViewById(R.id.main_IMG_dog3),
            findViewById(R.id.main_IMG_dog4)
        )

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )


        main_IMG_grims = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_grim00),
                findViewById(R.id.main_IMG_grim01),
                findViewById(R.id.main_IMG_grim02),
                findViewById(R.id.main_IMG_grim03),
                findViewById(R.id.main_IMG_grim04)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim10),
                findViewById(R.id.main_IMG_grim11),
                findViewById(R.id.main_IMG_grim12),
                findViewById(R.id.main_IMG_grim13),
                findViewById(R.id.main_IMG_grim14)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim20),
                findViewById(R.id.main_IMG_grim21),
                findViewById(R.id.main_IMG_grim22),
                findViewById(R.id.main_IMG_grim23),
                findViewById(R.id.main_IMG_grim24)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim30),
                findViewById(R.id.main_IMG_grim31),
                findViewById(R.id.main_IMG_grim32),
                findViewById(R.id.main_IMG_grim33),
                findViewById(R.id.main_IMG_grim34)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim40),
                findViewById(R.id.main_IMG_grim41),
                findViewById(R.id.main_IMG_grim42),
                findViewById(R.id.main_IMG_grim43),
                findViewById(R.id.main_IMG_grim44)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim50),
                findViewById(R.id.main_IMG_grim51),
                findViewById(R.id.main_IMG_grim52),
                findViewById(R.id.main_IMG_grim53),
                findViewById(R.id.main_IMG_grim54)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim60),
                findViewById(R.id.main_IMG_grim61),
                findViewById(R.id.main_IMG_grim62),
                findViewById(R.id.main_IMG_grim63),
                findViewById(R.id.main_IMG_grim64)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_grim70),
                findViewById(R.id.main_IMG_grim71),
                findViewById(R.id.main_IMG_grim72),
                findViewById(R.id.main_IMG_grim73),
                findViewById(R.id.main_IMG_grim74)
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        if (useSensors) {
            main_buttons_layout.visibility = View.INVISIBLE
        } else {
            main_buttons_layout.visibility = View.VISIBLE
            main_BTN_left.setOnClickListener { moveDog(-1) }
            main_BTN_right.setOnClickListener { moveDog(1) }
        }

        main_LBL_score.text = "000"
        refreshUI()
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX(x: Float) {
                    if (useSensors) {
                        if (x > 2.0) {
                            moveDog(-1)
                        } else if (x < -2.0) {
                            moveDog(1)
                        }
                    }
                }

                override fun tiltY(y: Float) {}
            }
        )
    }


    private fun moveDog(direction: Int) {
        gameManager.moveDog(direction)
        refreshUI()
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    private fun updateGameTick() {
        if (gameManager.checkCrash()) {
            SignalManager.getInstance().vibrate()
            SignalManager.getInstance().toast("Life Lost!")
            val soundPlayer = SingleSoundPlayer(this)
            soundPlayer.playSound(R.raw.crash)
            updateLivesUI()
        }

        if (gameManager.checkBoneCollection()) {
            SignalManager.getInstance().toast("Bone Collected!")
            val soundPlayer = SingleSoundPlayer(this)
            soundPlayer.playSound(R.raw.collect)
        }

        gameManager.updateGame()
        refreshUI()
        main_LBL_score.text = gameManager.score.toString().padStart(3, '0')

        if (gameManager.isGameOver) {
            SignalManager.getInstance().toast("Game Over!")
            gameTimer.stop()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    val finalLat = location?.latitude ?: 0.0
                    val finalLon = location?.longitude ?: 0.0
                    saveScoreToDB(finalLat, finalLon)
                }
            } else {
                saveScoreToDB(0.0, 0.0)
            }
            // ---------------------------------------------------

            mHandler.postDelayed({
                gameManager.restartGame()
                refreshUI()
                updateLivesUI()
                gameTimer.start()
            }, 3000)
        }
    }

    private fun saveScoreToDB(lat: Double, lon: Double) {
        val scoreListWrapper = SharedPreferencesManager.getInstance().loadScoreList()
        val allScores = scoreListWrapper.allScores.toMutableList()

        val newScore = com.example.obstacleracegame.model.Score.Builder()
            .score(gameManager.score)
            .lat(lat)
            .lon(lon)
            .timeStamp(System.currentTimeMillis())
            .build()

        allScores.add(newScore)
        allScores.sortByDescending { it.score }

        if (allScores.size > 10) {
            allScores.removeAt(allScores.lastIndex)
        }

        val updatedScoreList = ScoreList.Builder()
            .name("Top 10")
            .scores(allScores)
            .build()

        SharedPreferencesManager.getInstance().saveScoreList(updatedScoreList)
    }

    override fun onResume() {
        super.onResume()
        if (!gameManager.isGameOver) {
            gameTimer.start()
        }
        if (useSensors) tiltDetector.start()
    }

    override fun onPause() {
        super.onPause()
        gameTimer.stop()
        if (useSensors) tiltDetector.stop()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun refreshUI() {
        for (i in main_IMG_dogs.indices) {
            val dog = main_IMG_dogs[i]
            dog.setImageResource(R.drawable.dog)

            if (i == gameManager.dogIndex) {
                dog.visibility = View.VISIBLE
            } else {
                dog.visibility = View.INVISIBLE
            }
        }
        for (r in 0 until Constants.GameLogic.ROWS) {
            for (c in 0 until Constants.GameLogic.COLS) {
                val img = main_IMG_grims[r][c]
                val itemType = gameManager.gameGrid[r][c]

                when (itemType) {
                    Constants.GameLogic.GRIM -> {
                        img.setImageResource(R.drawable.death_grim)
                        img.visibility = View.VISIBLE
                        img.scaleX = 1.0f
                        img.scaleY = 1.0f
                    }
                    Constants.GameLogic.BONE -> {
                        img.setImageResource(R.drawable.bone)
                        img.visibility = View.VISIBLE
                        img.scaleX = 0.7f
                        img.scaleY = 0.7f
                    }
                    else -> {
                        img.visibility = View.INVISIBLE
                        img.scaleX = 1.0f
                        img.scaleY = 1.0f
                    }
                }
            }
        }
    }

    private fun updateLivesUI() {
        val lostLives = Constants.GameLogic.MAX_LIVES - gameManager.lives

        for (i in 0 until lostLives) {
            val index = main_IMG_hearts.size - 1 - i
            if (index >= 0) {
                main_IMG_hearts[index].visibility = View.INVISIBLE
            }
        }

        if (gameManager.lives == Constants.GameLogic.MAX_LIVES ) {
            for (heart in main_IMG_hearts) {
                heart.visibility = View.VISIBLE
            }
        }
    }
}