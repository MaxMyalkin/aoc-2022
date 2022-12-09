package day_9

import common.getInputFile
import common.getResourcePath
import common.getSampleFile
import java.io.File
import kotlin.math.abs

private enum class Direction(val symbol: String) {
    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R")
}

private data class Movement(
    val direction: Direction,
    val count: Int
)

private data class Position(
    val x: Int,
    val y: Int
) {
    fun moveTo(direction: Direction): Position {
        return when (direction) {
            Direction.UP -> copy(y = y + 1)
            Direction.DOWN -> copy(y = y - 1)
            Direction.LEFT -> copy(x = x - 1)
            Direction.RIGHT -> copy(x = x + 1)
        }
    }
}

private fun calculateTailPositionCount(
    file: File,
    knotCount: Int
): Int {
    val movements = file.parseMovements()

    val positionEntryCount = mutableMapOf<Position, Int>()
    val knots = MutableList(knotCount) { Position(0, 0) }

    positionEntryCount.visitPosition(knots.last())
    movements.forEach { movement ->
        var remainingCount = movement.count
        while (remainingCount > 0) {
            knots[0] = knots[0].moveTo(movement.direction)
            knots.subList(1, knots.size).forEachIndexed { index, position ->
                knots[index + 1] = calculateNewKnotPosition(knots[index], knots[index + 1])
            }
            positionEntryCount.visitPosition(knots.last())
            remainingCount--
        }
    }
    return positionEntryCount.keys.size
}

private fun File.parseMovements(): List<Movement> {
    return bufferedReader()
        .readLines()
        .map {
            it.split(" ").let {
                val directionSymbol = it[0]
                val direction = Direction.values().find { it.symbol == directionSymbol }!!
                val count = it[1].toInt()
                Movement(direction, count)
            }
        }
        .toList()
}

private fun MutableMap<Position, Int>.visitPosition(position: Position) {
    val currentVisits = getOrPut(position, defaultValue = { 0 })
    put(position, currentVisits + 1)
}

private fun calculateNewKnotPosition(firstKnotPosition: Position, lastKnotPosition: Position): Position {
    fun calculateAxisResult(headAxisPosition: Int, tailAxisPosition: Int): Int {
        val axisDiff = headAxisPosition - tailAxisPosition
        val axisMovement = axisDiff
            .takeIf { abs(it) > 1 }
            ?.let { diff ->
                val isNegativeDirection = diff < 0
                if (isNegativeDirection) diff + 1 else diff - 1
            } ?: 0
        return tailAxisPosition + axisMovement
    }

    val shouldMoveTail = abs(firstKnotPosition.x - lastKnotPosition.x) > 1 ||
            abs(firstKnotPosition.y - lastKnotPosition.y) > 1
    if (!shouldMoveTail) return lastKnotPosition

    val onDiagonal = firstKnotPosition.y != lastKnotPosition.y && firstKnotPosition.x != lastKnotPosition.x

    val xDiff = firstKnotPosition.x - lastKnotPosition.x
    val yDiff = firstKnotPosition.y - lastKnotPosition.y

    var resultX = calculateAxisResult(firstKnotPosition.x, lastKnotPosition.x)
    var resultY = calculateAxisResult(firstKnotPosition.y, lastKnotPosition.y)

    when {
        onDiagonal && abs(yDiff) > abs(xDiff) -> {
            resultX = firstKnotPosition.x
        }

        onDiagonal && abs(yDiff) < abs(xDiff) -> {
            resultY = firstKnotPosition.y
        }
    }
    return Position(x = resultX, y = resultY)
}

fun main() {
    val sample = getSampleFile(9)
    val sample2 = File(getResourcePath(9, "sample2.txt"))
    val input = getInputFile(9)
    val part1KnotCount = 2
    val part2KnotCount = 10

    check(calculateTailPositionCount(sample, part1KnotCount) == 13)
    println("Part1 = ${calculateTailPositionCount(input, part1KnotCount)}")
    check(calculateTailPositionCount(sample2, part2KnotCount) == 36)
    println("Part2 = ${calculateTailPositionCount(input, part2KnotCount)}")
}
