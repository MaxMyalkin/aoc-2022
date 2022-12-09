package day_2

class Task2GameStrategy : Game {
    override var finalScore = 0L
        private set

    private val playResultsMap = mapOf(
        "X" to GameResult.LOSS,
        "Y" to GameResult.DRAW,
        "Z" to GameResult.WIN,
    )

    override fun addRound(myStringElement: String, opponentStringElement: String) {
        val opponentElement = GameElement.findElement(opponentStringElement)
        val expectedResult = playResultsMap[myStringElement] ?: error("Can not find result for $myStringElement")

        val myElement = ruleSet
            .find { it.result == expectedResult && it.opponentElement == opponentElement }!!
            .myElement

        finalScore += myElement.score + expectedResult.score
    }
}