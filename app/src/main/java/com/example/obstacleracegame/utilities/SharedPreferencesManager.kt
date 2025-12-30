package com.example.obstacleracegame.utilities

import android.content.Context
import androidx.core.content.edit
import com.example.obstacleracegame.model.ScoreList
import com.google.gson.Gson

class SharedPreferencesManager private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "OBSTACLE_RACE_DB",
        Context.MODE_PRIVATE
    )

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance ?: throw IllegalStateException(
                "SharedPreferencesManager must be initialized by calling init(context) before use."
            )
        }
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }


    fun saveScoreList(scoreList: ScoreList) {
        val gson = Gson()
        val jsonString = gson.toJson(scoreList)
        putString("SCORES_KEY", jsonString)
    }

    fun loadScoreList(): ScoreList {
        val jsonString = getString("SCORES_KEY", "")
        if (jsonString.isEmpty()) {
            return ScoreList.Builder().build()
        }
        val gson = Gson()
        return gson.fromJson(jsonString, ScoreList::class.java)
    }
}