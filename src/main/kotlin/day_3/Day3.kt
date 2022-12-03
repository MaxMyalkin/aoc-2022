package day_3

import common.getInputFile
import common.getSampleFile
import java.io.File

private fun findCommonItem(first: String, second: String): Char? {
    return first.find { firstChar ->
        second.contains(firstChar)
    }
}

private fun scoreItem(item: Char): Int {
    val startScore = 1
    val startCapitalScore = 27
    return if(item.isUpperCase()) {
        startCapitalScore + item.code - 'A'.code
    } else {
        startScore + item.code - 'a'.code
    }
}
private fun calculateScoreForBackPack(backPackItems: String): Int {
    val centerIndex = backPackItems.length / 2
    val firstHalf = backPackItems.substring(startIndex = 0, endIndex = centerIndex)
    val secondHalf = backPackItems.substring(startIndex = centerIndex)

    val commonItem = findCommonItem(firstHalf, secondHalf) ?: error("Items $backPackItems have to common item")
    return scoreItem(commonItem)
}

fun part1(file: File): Int {
    return file.bufferedReader()
        .lines()
        .map { backPackItems ->
            if (backPackItems.length % 2 != 0) error("length incorrect")
            calculateScoreForBackPack(backPackItems)
        }
        .reduce { first, second -> first + second }
        .get()
}

fun main() {

    val sample = getSampleFile(3)
    val input = getInputFile(3)

    check(part1(sample) == 157)
    println("Part1 = ${part1(input)}")

}