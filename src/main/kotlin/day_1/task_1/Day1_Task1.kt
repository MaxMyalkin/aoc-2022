package day_1.task_1

import common.getInputFile
import common.getSampleFile
import java.io.File

fun main() {
    var maxCalories = Long.MIN_VALUE
    var currentElfCalories = 0L

    fun changeElf() {
        if (currentElfCalories > maxCalories) {
            maxCalories = currentElfCalories
        }
        currentElfCalories = 0
    }

    getInputFile(dayNumber = 1, taskNumber = 1).bufferedReader().forEachLine { line ->
        line.toLongOrNull()?.let { calories ->
            currentElfCalories += calories
        } ?: changeElf()
    }

    println("Max calories = $maxCalories")
}