enum class GameElement(
    val play: String,
    val score: Int
) {
    ROCK(play = "A", score = 1),
    PAPER(play = "B", score = 2),
    SCISSORS(play = "C", score = 3);

    companion object {

        fun findElement(play: String): GameElement {
            return GameElement.values().find { it.play == play } ?: error("Cannot find element for '$play'")
        }
    }

}