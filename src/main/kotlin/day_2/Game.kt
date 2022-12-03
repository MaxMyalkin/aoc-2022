package day_2

interface Game {
    val finalScore: Long
    fun addRound(myStringElement: String, opponentStringElement: String)
}