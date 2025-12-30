package com.example.obstacleracegame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obstacleracegame.R
import com.example.obstacleracegame.interfaces.Callback_TopScoresClicked
import com.example.obstacleracegame.utilities.ScoreAdapter
import com.example.obstacleracegame.utilities.SharedPreferencesManager

class HighScoreFragment : Fragment() {

    private lateinit var highScore_LST_scores: RecyclerView

    var topScoresCallback: Callback_TopScoresClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(v)
        initViews()
        return v
    }

    private fun findViews(v: View) {
        highScore_LST_scores = v.findViewById(R.id.highScore_LST_scores)
    }

    private fun initViews() {
        val scoreListWrapper = SharedPreferencesManager.getInstance().loadScoreList()

        val scoreAdapter = ScoreAdapter(scoreListWrapper.allScores, topScoresCallback)

        highScore_LST_scores.layoutManager = LinearLayoutManager(context)
        highScore_LST_scores.adapter = scoreAdapter
    }
}