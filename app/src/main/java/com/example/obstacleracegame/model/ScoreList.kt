package com.example.obstacleracegame.model

@ConsistentCopyVisibility
data class ScoreList private constructor(
    val name: String,
    val allScores: List<Score>
) {
    class Builder(
        var name: String = "High Scores",
        var allScores: List<Score> = mutableListOf()
    ) {
        fun name(name: String) = apply { this.name = name }

        fun scores(scores: List<Score>) = apply {
            this.allScores = scores
        }

        fun build() = ScoreList(name, allScores)
    }
}