package day_4

import common.getInputFile
import common.getSampleFile
import java.io.File

private fun createRange(strRange: String): IntRange {
    val splitted = strRange.split("-")
    if(splitted.size != 2) error("Incorrect input")
    return IntRange(splitted[0].toInt(), splitted[1].toInt())
}

private fun isRangeContainsEachOther(first: IntRange, second: IntRange): Boolean {
    return first.contains(second.first) && first.contains(second.last) ||
            second.contains(first.first) && second.contains(first.last)
}
private fun part1(file: File): Int {
    return file.bufferedReader()
        .readLines()
        .map {
            val ranges = it.split(",")
            if(ranges.size != 2) error("Incorrect input")
            val firstElfRange = createRange(ranges[0])
            val secondElfRange = createRange(ranges[1])
            isRangeContainsEachOther(firstElfRange, secondElfRange)
        }
        .count { it }
}

fun main() {
    val input = getInputFile(4)
    val sample = getSampleFile(4)

    check(part1(sample) == 2)
    println("Part1 = ${part1(input)}")
}