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

typealias MoveOperation = (fromStack: ArrayDeque<String>, toStack: ArrayDeque<String>, count: Int) -> Unit

private fun readMovements(file: File): List<Movement> {
    return file.bufferedReader()
        .lineSequence()
        .filter { it.startsWith("move") }
        .map { it.split(" ") }
        .map { Movement(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
        .toList()
}

private fun List<ArrayDeque<String>>.applyMovements(
    movements: List<Movement>,
    moveOperation: MoveOperation
) {
    movements.forEach { movement ->
        val fromIndex = movement.from.dec()
        val toIndex = movement.to.dec()
        moveOperation(this[fromIndex], this[toIndex], movement.count)
    }
}

private fun reverseOrderMove(fromStack: ArrayDeque<String>, toStack: ArrayDeque<String>, count: Int) {
    (0 until count).forEach {
        val letter = fromStack.removeLastOrNull() ?: error("Incorrect operation")
        toStack.addLast(letter)
    }
}

private fun defaultOrderMove(fromStack: ArrayDeque<String>, toStack: ArrayDeque<String>, count: Int) {
    val lastCrates = fromStack.takeLast(count)
    val remainingCrates = fromStack.dropLast(count)
    fromStack.clear()
    fromStack.addAll(remainingCrates)
    toStack.addAll(lastCrates)
}

private fun List<ArrayDeque<String>>.getTopLetters(): String {
    return mapNotNull { it.lastOrNull() }
        .joinToString(separator = "")
}

private fun moveCrates(
    file: File,
    moveOperation: MoveOperation
): String {
    val columnCount = readColumnCount(file)
    if (columnCount <= 0) error("Check inputs")

    val stacks = List<ArrayDeque<String>>(columnCount) { ArrayDeque() }
    fillStacks(file, stacks)

    val movements = readMovements(file)
    stacks.applyMovements(movements, moveOperation)

    return stacks.getTopLetters()
}

fun main() {
    val sample = getSampleFile(5)
    val input = getInputFile(5)

    check(moveCrates(sample, ::reverseOrderMove) == "CMZ")
    println("Part1 = ${moveCrates(input, ::reverseOrderMove)}")

    check(moveCrates(sample, ::defaultOrderMove) == "MCD")
    println("Part2 = ${moveCrates(input, ::defaultOrderMove)}")

}