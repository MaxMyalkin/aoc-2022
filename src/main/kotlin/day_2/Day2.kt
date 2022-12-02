package day_2

import common.getInputFile
import common.getSampleFile
import java.io.File

enum class GameElement(
    val myPlay: String,
    val opponentPlay: String,
    val score: Int
) {
    ROCK(myPlay = "X", opponentPlay = "A", score = 1),
    PAPER(myPlay = "Y", opponentPlay = "B", score = 2),
    SCISSORS(myPlay = "Z", opponentPlay = "C", score = 3);
    companion object {
        fun findElementByMyPlay(myPlay: String): GameElement {
            return GameElement.values().find { it.myPlay == myPlay } ?: error("Cannot find element for '$myPlay'")
        }

        fun findElementByOpponentPlay(opponentPlay: String): GameElement {
            return GameElement.values().find { it.opponentPlay == opponentPlay } ?: error("Cannot find element for '$opponentPlay'")
        }
    }

}

enum class GameResult(val score: Int) {
    WIN(score = 6),
    DRAW(score = 3),
    LOSS(score = 0)
}

class Game {
    var finalScore = 0L
        private set
    fun addRound(myElement: GameElement, opponentElement: GameElement) {
        val result = when(myElement) {
            GameElement.ROCK -> when(opponentElement) {
                GameElement.ROCK -> GameResult.DRAW
                GameElement.PAPER -> GameResult.LOSS
                GameElement.SCISSORS -> GameResult.WIN
            }
            GameElement.PAPER -> when(opponentElement) {
                GameElement.ROCK -> GameResult.WIN
                GameElement.PAPER -> GameResult.DRAW
                GameElement.SCISSORS -> GameResult.LOSS
            }
            GameElement.SCISSORS -> when(opponentElement) {
                GameElement.ROCK -> GameResult.LOSS
                GameElement.PAPER -> GameResult.WIN
                GameElement.SCISSORS -> GameResult.DRAW
            }
        }

        finalScore += result.score + myElement.score
    }
}
fun part1(file: File): Long {
    val game = Game()
    file.bufferedReader().forEachLine {
        val stringElements = it.split(" ")
        val opponentElement = GameElement.findElementByOpponentPlay(stringElements[0])
        val myElement = GameElement.findElementByMyPlay(stringElements[1])
        game.addRound(myElement, opponentElement)
    }

    return game.finalScore
}

fun main() {
    val sample = getSampleFile(2)
    val input = getInputFile(2)

    check(part1(sample) == 15L)
    println("Part1 = ${part1(input)}")
}