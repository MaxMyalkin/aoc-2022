package day_2

import GameElement

class Task1GameStrategy : Game {
    override var finalScore = 0L
        private set

    private val myPlayMap = mapOf(
        "X" to GameElement.ROCK,
        "Y" to GameElement.PAPER,
        "Z" to GameElement.SCISSORS,
    )

    override fun addRound(myStringElement: String, opponentStringElement: String) {

        val opponentElement = GameElement.findElement(opponentStringElement)
        val myElement = findElementByMyPlay(myStringElement)

        val result = ruleSet
            .find { it.myElement == myElement && it.opponentElement == opponentElement }!!
            .result

        finalScore += result.score + myElement.score
    }

    private fun findElementByMyPlay(myPlay: String): GameElement {
        return myPlayMap[myPlay] ?: error("Cannot find element for '$myPlay'")
    }
}