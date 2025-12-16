package com.example.obstacleracegame.logic

import com.example.obstacleracegame.utilities.Constants

class GameManager(private val lifeCount: Int = Constants.GameLogic.MAX_LIVES) {

    var dogStartIndex: Int = 1
        private set

    var crashes: Int = 0
        private set

    private val rows = Constants.GameLogic.ROWS
    private val cols = Constants.GameLogic.COLS
    private var gameTurns = 0
    val gameGrid = Array(rows) { IntArray(cols) }

    val isGameOver: Boolean
        get() = crashes == lifeCount

    fun moveDog(direction: Int) {
        val newDogIndex = dogStartIndex + direction
        if (newDogIndex in 0..2)
            dogStartIndex = newDogIndex
    }

    fun updateGame() {
        gameTurns++
        for (r in rows - 1 downTo 1) {
            for (c in 0 until cols) {
                gameGrid[r][c] =gameGrid[r - Constants.GameLogic.LIVES_OFFSET][c]
            }
        }
        for (c in 0 until cols) {
            gameGrid[0][c] = Constants.GameLogic.NO_GRIM
        }
        if (gameTurns % 2 == 0) {
            val randomCol = (0..2).random()
            gameGrid[0][randomCol] = Constants.GameLogic.YES_GRIM
        }
    }

    fun checkCrash(): Boolean {
        var isCrash = false
        if (gameGrid[rows - Constants.GameLogic.LIVES_OFFSET][dogStartIndex] == Constants.GameLogic.YES_GRIM) {
            crashes++
            isCrash = true
            gameGrid[rows - Constants.GameLogic.LIVES_OFFSET][dogStartIndex] = Constants.GameLogic.NO_GRIM
        }
        return isCrash
    }

    fun restartGame() {
        crashes = 0
        gameTurns = 0
        dogStartIndex = 1

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                gameGrid[r][c] = Constants.GameLogic.NO_GRIM
            }
        }
    }
}