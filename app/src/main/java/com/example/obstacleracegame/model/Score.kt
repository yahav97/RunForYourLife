package com.example.obstacleracegame.model

@ConsistentCopyVisibility
data class Score private constructor(
    val score: Int,
    val lat: Double,
    val lon: Double,
    val timeStamp: Long
) {
    class Builder(
        var score: Int = 0,
        var lat: Double = 0.0,
        var lon: Double = 0.0,
        var timeStamp: Long = 0L
    ) {
        fun score(score: Int) = apply { this.score = score }
        fun lat(lat: Double) = apply { this.lat = lat }
        fun lon(lon: Double) = apply { this.lon = lon }
        fun timeStamp(timeStamp: Long) = apply { this.timeStamp = timeStamp }

        fun build() = Score(score, lat, lon, timeStamp)
    }
}