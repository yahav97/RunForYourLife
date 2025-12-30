package com.example.obstacleracegame.logic

import com.example.obstacleracegame.utilities.Constants

class GameManager(private val lifeCount: Int = Constants.GameLogic.MAX_LIVES) {

    var score: Int = 0
        private set

    var dogIndex: Int = 2
        private set

    var crashes: Int = 0
        private set

    val lives: Int
        get() = lifeCount - crashes

    private val rows = Constants.GameLogic.ROWS
    private val cols = Constants.GameLogic.COLS

    val gameGrid = Array(rows) { IntArray(cols) }

    private var gameTurns = 0

    val isGameOver: Boolean
        get() = crashes >= lifeCount


    fun moveDog(direction: Int) {
        val newDogIndex = dogIndex + direction
        if (newDogIndex in 0 until cols) {
            dogIndex = newDogIndex
        }
    }

    fun updateGame() {
        gameTurns++
        score += 10
        for (r in rows - 1 downTo 1) {
            for (c in 0 until cols) {
                gameGrid[r][c] = gameGrid[r - 1][c]
            }
        }
        for (c in 0 until cols) {
            gameGrid[0][c] = Constants.GameLogic.EMPTY
        }
        if (gameTurns % 2 == 0) {
            val randomCol = (0 until cols).random()

            val randomType = (1..10).random()
            if (randomType <= 2) {
                gameGrid[0][randomCol] = Constants.GameLogic.BONE
            } else {
                gameGrid[0][randomCol] = Constants.GameLogic.GRIM
            }
        }
    }


    fun checkCrash(): Boolean {
        var isCrash = false
        if (gameGrid[rows - 1][dogIndex] == Constants.GameLogic.GRIM) {
            crashes++
            isCrash = true
            gameGrid[rows - 1][dogIndex] = Constants.GameLogic.EMPTY
        }
        return isCrash
    }

    fun checkBoneCollection(): Boolean {
        var isCollected = false
        if (gameGrid[rows - 1][dogIndex] == Constants.GameLogic.BONE) {
            score += 50
            isCollected = true
            gameGrid[rows - 1][dogIndex] = Constants.GameLogic.EMPTY
        }
        return isCollected
    }

    fun restartGame() {
        crashes = 0
        score = 0
        gameTurns = 0
        dogIndex = 2
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                gameGrid[r][c] = Constants.GameLogic.EMPTY
            }
        }
    }
}