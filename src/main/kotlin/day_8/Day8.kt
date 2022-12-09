package day_8

import common.getInputFile
import common.getSampleFile
import java.io.File

private fun createMatrixFromFile(
    file: File
): List<List<Int>> {
    return file.bufferedReader()
        .readLines()
        .map {
            it.toCharArray().map { it.digitToInt() }
        }
}

private fun calculateVisibleItemCount(
    file: File
): Int {
    val matrix = createMatrixFromFile(file)
    return matrix.mapIndexed { i, heights ->
        heights.mapIndexed { j, height ->
            checkIfItemVisible(matrix, i, j)
        }.count { it }
    }.sum()
}

private fun checkIfItemVisible(
    matrix: List<List<Int>>,
    i: Int,
    j: Int
): Boolean {
    val element = matrix[i][j]
    val row = matrix[i]
    val column = matrix.map { it[j] }
    val isInvisibleFromLeft = row.filterIndexed { index, height ->
        index < j && height >= element
    }.isNotEmpty()
    val isInvisibleFromRight = row.filterIndexed { index, height ->
        index > j && height >= element
    }.isNotEmpty()

    val isInvisibleFromTop = column.filterIndexed { index, height ->
        index < i && height >= element
    }.isNotEmpty()

    val isInvisibleFromBottom = column.filterIndexed { index, height ->
        index > i && height >= element
    }.isNotEmpty()
    return !(isInvisibleFromLeft && isInvisibleFromRight && isInvisibleFromTop && isInvisibleFromBottom)
}

private fun calculateScenicScore(
    matrix: List<List<Int>>,
    i: Int,
    j: Int
): Int {
    val element = matrix[i][j]
    val row = matrix[i]
    val column = matrix.map { it[j] }

    fun <T> List<T>.countOfFirst(predicate: (T) -> Boolean): Int {
        var count = 0
        forEach {
            if (predicate(it)) {
                count++
            } else {
                return ++count
            }
        }
        return count
    }

    val leftScenicScore = row.subList(0, j).reversed().takeIf { it.isNotEmpty() }?.countOfFirst { it < element } ?: 0
    val rightScenicScore = row.subList(j + 1, row.size).takeIf { it.isNotEmpty() }?.countOfFirst { it < element } ?: 0
    val topScenicScore = column.subList(0, i).reversed().takeIf { it.isNotEmpty() }?.countOfFirst { it < element } ?: 0
    val bottomScenicScore =
        column.subList(i + 1, row.size).takeIf { it.isNotEmpty() }?.countOfFirst { it < element } ?: 0
    return leftScenicScore * rightScenicScore * topScenicScore * bottomScenicScore
}

private fun calculateBestScenicScore(
    file: File
): Int {
    val matrix = createMatrixFromFile(file)
    return matrix.mapIndexed { i, heights ->
        heights.mapIndexed { j, height ->
            calculateScenicScore(matrix, i, j)
        }.max()
    }.max()
}

fun main() {
    val sample = getSampleFile(8)
    val input = getInputFile(8)

    check(calculateVisibleItemCount(sample) == 21)
    println("Part1 = ${calculateVisibleItemCount(input)}")

    check(calculateBestScenicScore(sample) == 8)
    println("Part2 = ${calculateBestScenicScore(input)}")
}