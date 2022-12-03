package day_2

import common.getInputFile
import common.getSampleFile
import java.io.File

fun playGame(file: File, game: Game): Long {
    file.bufferedReader().forEachLine {
        val stringElements = it.split(" ")
        val opponentElement = stringElements[0]
        val myElement = stringElements[1]
        game.addRound(myStringElement = myElement, opponentStringElement = opponentElement)
    }

    return game.finalScore
}

fun main() {
    val sample = getSampleFile(2)
    val input = getInputFile(2)

    check(playGame(sample, Task1GameStrategy()) == 15L)
    println("Part1 = ${playGame(input, Task1GameStrategy())}")

    check(playGame(sample, Task2GameStrategy()) == 12L)
    println("Part2 = ${playGame(input, Task2GameStrategy())}") //11657
}