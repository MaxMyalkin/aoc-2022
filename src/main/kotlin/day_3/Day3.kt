package day_3

import common.getInputFile
import common.getSampleFile
import java.io.File

private fun findCommonItem(backpackList: List<String>): Char? {
    if(backpackList.size < 2) return null

    val firstBackPack = backpackList.first()
    val othersBackPack = backpackList.subList(1, backpackList.size)

    return firstBackPack.find { mainBackPackItem ->
        othersBackPack.all { it.contains(mainBackPackItem) }
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

    val commonItem = findCommonItem(listOf(firstHalf, secondHalf)) ?: error("Items $backPackItems have to common item")
    return scoreItem(commonItem)
}

private fun part1(file: File): Int {
    return file.bufferedReader()
        .lines()
        .map { backPackItems ->
            if (backPackItems.length % 2 != 0) error("length incorrect")
            calculateScoreForBackPack(backPackItems)
        }
        .reduce { first, second -> first + second }
        .get()
}

private fun calculateElfGroupScore(group: List<String>): Int {
    val commonItem = findCommonItem(group) ?: error("Group $group have to common item")
    return scoreItem(commonItem)
}
private fun part2(file: File): Int {
    return file.bufferedReader()
        .lineSequence()
        .chunked(3)
        .map { elfGroup ->
            calculateElfGroupScore(elfGroup)
        }
        .reduce { first, second -> first + second }
}
fun main() {

    val sample = getSampleFile(3)
    val input = getInputFile(3)

    check(part1(sample) == 157)
    println("Part1 = ${part1(input)}")

    check(part2(sample) == 70)
    println("Part2 = ${part2(input)}")
}