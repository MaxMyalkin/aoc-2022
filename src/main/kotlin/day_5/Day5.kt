package day_5

import common.getInputFile
import common.getSampleFile
import java.io.File
import java.util.Stack


private fun readColumnCount(file: File): Int {
    return file.bufferedReader()
        .lineSequence()
        .map { it.split(" ").filter { it.isNotBlank() } }
        .find { it.getOrNull(0) == "1" }
        ?.lastOrNull()
        ?.toIntOrNull()
        ?: 0
}

private fun fillStacks(file: File, stacks: List<ArrayDeque<String>>) {
    file.bufferedReader()
        .lineSequence()
        .takeWhile { it.contains("[") }
        .map { it.chunked(4) }
        .forEach { letters ->
            letters.forEachIndexed { index, letter ->
                if (letter.isBlank()) return@forEachIndexed
                stacks[index].addFirst(letter.trim().removeSurrounding("[", "]"))
            }
        }
}

private data class Movement(
    val count: Int,
    val from: Int,
    val to: Int
)
private fun readMovements(file: File): List<Movement> {
    return file.bufferedReader()
        .lineSequence()
        .filter { it.startsWith("move") }
        .map { it.split(" ") }
        .map { Movement(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
        .toList()
}

private fun List<ArrayDeque<String>>.applyMovements(movements: List<Movement>) {
    movements.forEach { movement ->
        val fromIndex = movement.from.dec()
        val toIndex = movement.to.dec()
        (0 until movement.count).forEach {
            val letter = this[fromIndex].removeLastOrNull() ?: error("Incorrect operation")
            this[toIndex].addLast(letter)
        }
    }
}

private fun List<ArrayDeque<String>>.getTopLetters(): String {
    return mapNotNull { it.lastOrNull() }
        .joinToString(separator = "")
}
private fun part1(file: File): String {
    val columnCount = readColumnCount(file)
    if(columnCount <= 0) error("Check inputs")

    val stacks = List<ArrayDeque<String>>(columnCount) { ArrayDeque() }
    fillStacks(file, stacks)

    val movements = readMovements(file)
    stacks.applyMovements(movements)

    return stacks.getTopLetters()
}
fun main() {
    val sample = getSampleFile(5)
    val input = getInputFile(5)

    check(part1(sample) == "CMZ")
    println("Part1 = ${part1(input)}")


}