package day_1.task_2

import common.getInputFile
import common.getSampleFile

fun main() {
    val elfCaloriesList = mutableListOf<Long>()
    var currentElfCalories = 0L

    fun changeElf() {
        elfCaloriesList.add(currentElfCalories)
        currentElfCalories = 0
    }

    getInputFile(dayNumber = 1, taskNumber = 1).bufferedReader().forEachLine { line ->
        line.toLongOrNull()?.let { calories ->
            currentElfCalories += calories
        } ?: changeElf()
    }
    changeElf()

    val top3Sum = elfCaloriesList.apply { sortDescending() }.take(3).sum()

    println("Top 3 calories sum = $top3Sum")
}