package day_1

import common.getInputFile
import common.getSampleFile
import java.io.File

fun part1(file: File): Long {
    var maxCalories = Long.MIN_VALUE
    var currentElfCalories = 0L

    fun changeElf() {
        if (currentElfCalories > maxCalories) {
            maxCalories = currentElfCalories
        }
        currentElfCalories = 0
    }

    file.bufferedReader().forEachLine { line ->
        line.toLongOrNull()?.let { calories ->
            currentElfCalories += calories
        } ?: changeElf()
    }
    changeElf()

    return maxCalories
}

fun part2(file: File): Long {
    val elfCaloriesList = mutableListOf<Long>()
    var currentElfCalories = 0L

    fun changeElf() {
        elfCaloriesList.add(currentElfCalories)
        currentElfCalories = 0
    }

    file.bufferedReader().forEachLine { line ->
        line.toLongOrNull()?.let { calories ->
            currentElfCalories += calories
        } ?: changeElf()
    }
    changeElf()

    return elfCaloriesList.apply { sortDescending() }.take(3).sum()
}

fun main() {
    val sample = getSampleFile(1)
    val input = getInputFile(1)

    check(part1(sample) == 24000L)
    println("Part 1 = ${part1(input)}")

    check(part2(sample) == 45000L)
    println("Part 2 = ${part2(input)}")

}