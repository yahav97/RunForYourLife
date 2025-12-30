package com.example.obstacleracegame.utilities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obstacleracegame.R
import com.example.obstacleracegame.interfaces.Callback_TopScoresClicked
import com.example.obstacleracegame.model.Score

class ScoreAdapter(
    private val scores: List<Score>,
    private val callback: Callback_TopScoresClicked?
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_score_item, parent, false)
        return ScoreViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.lblPlace.text = "${position + 1}"
        holder.lblScore.text = "${score.score}"

        holder.itemView.setOnClickListener {
            callback?.topScoresItemClicked(score.lat, score.lon)
        }
    }

    override fun getItemCount() = scores.size

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblPlace: TextView = itemView.findViewById(R.id.score_LBL_place)
        val lblScore: TextView = itemView.findViewById(R.id.score_LBL_score)
    }
}