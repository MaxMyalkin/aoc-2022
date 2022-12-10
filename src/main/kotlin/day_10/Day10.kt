package day_10

import common.getInputFile
import common.getSampleFile
import java.io.File

private class Cpu {
    var x = 1
        private set
    var cycleCount = 0
        private set

    fun executeInstructions(
        instructions: List<Instruction>,
        beforeCycleUpdated: (cycle: Int, x: Int) -> Unit = { _, _ ->},
        afterCycleUpdated: (cycle: Int, x: Int) -> Unit = { _, _ ->}) {
        instructions.forEach {

            fun increaseCycle() {
                beforeCycleUpdated(cycleCount, x)
                cycleCount++
                afterCycleUpdated(cycleCount, x)
            }

            when (it) {
                is Instruction.Add -> {
                    repeat(2) { increaseCycle() }
                    x += it.value
                }

                Instruction.Noop -> increaseCycle()
            }
        }
    }
}

private sealed interface Instruction {
    object Noop : Instruction
    data class Add(val value: Int) : Instruction
}

private fun calculateSignalStrength(file: File): Int {
    val checkCycleCount = setOf(20, 60, 100, 140, 180, 220)
    val cpu = Cpu()
    var strength = 0

    val instructions = getInstructionList(file)
    cpu.executeInstructions(instructions, afterCycleUpdated = { cycle, x ->
        if (cycle in checkCycleCount) {
            strength += x * cycle
        }
    })
    return strength
}

private fun getInstructionList(file: File): List<Instruction> {
    return file.bufferedReader()
        .readLines()
        .map {
            it.toInstruction()
        }
}

private fun String.toInstruction(): Instruction {
    return when {
        this == "noop" -> Instruction.Noop
        this.startsWith("addx") -> {
            val value = split(" ")[1].toInt()
            Instruction.Add(value)
        }

        else -> error("Unreachable")
    }
}

private fun printImage(file: File) {
    val instructions = getInstructionList(file)
    val cpu = Cpu()
    val pixels = mutableListOf<Char>()
    cpu.executeInstructions(instructions, beforeCycleUpdated = { cycle, x ->
        val lineCycle = cycle % 40
        val cycleIntersectsSprite = lineCycle >= x - 1 && lineCycle <= x + 1
        val pixel = if (cycleIntersectsSprite) '#' else '.'
        pixels.add(pixel)
    })
    printPixelsOnCrt(pixels)
}

private fun printPixelsOnCrt(pixels: List<Char>) {
    pixels.chunked(40)
        .map { it.joinToString("") }
        .forEach {
            println(it)
        }
}

fun main() {

    val sample = getSampleFile(10)
    val input = getInputFile(10)

    check(calculateSignalStrength(sample) == 13140)
    println("Part1=${calculateSignalStrength(input)}")

    println("Sample image")
    printImage(sample)
    println("Part2 image")
    printImage(input)

}