package day_11

import common.getInputFile
import common.getSampleFile
import java.io.File

typealias WorryLevel = Long

private data class Test(
    val divisibleCondition: WorryLevel,
    private val positiveTarget: Int,
    private val negativeTarget: Int
) {
    fun getNextThrowPosition(value: WorryLevel): Int {
        return if(value % divisibleCondition == 0L) positiveTarget else negativeTarget
    }
}

private data class Operation(
    private val operator: Operator,
    private val firstOperand: Operand,
    private val secondOperand: Operand
) {

    fun execute(old: WorryLevel): WorryLevel {
        return operator.apply(firstOperand.getOperandValue(old), secondOperand.getOperandValue(old))
    }

    enum class Operator(val sign: String) {
        PLUS("+") {
            override fun apply(firstOperand: WorryLevel, secondOperand: WorryLevel): WorryLevel {
                return firstOperand + secondOperand
            }
        },
        MULTIPLY("*") {
            override fun apply(firstOperand: WorryLevel, secondOperand: WorryLevel): WorryLevel {
                return firstOperand * secondOperand
            }
        };

        abstract fun apply(firstOperand: WorryLevel, secondOperand: WorryLevel): WorryLevel
    }

    interface Operand {
        object Old: Operand {
            override fun getOperandValue(old: WorryLevel): WorryLevel = old
        }

        data class Value(val value: WorryLevel): Operand {
            override fun getOperandValue(old: WorryLevel): WorryLevel = value
        }

        abstract fun getOperandValue(old: WorryLevel): WorryLevel
        companion object {
            fun getByString(operand: String): Operand {
                return when (operand) {
                    "old" -> Old
                    else -> Value(operand.toLong())
                }
            }
        }
    }
}
private class Monkey(
    val index: Int,
    private val operation: Operation,
    val test: Test,
    items: List<WorryLevel>
) {
    private val items: ArrayDeque<WorryLevel> = ArrayDeque(items)
    var inspectedItemCount = 0L
        private set
    fun acceptItem(item: WorryLevel) {
        items.addLast(item)
    }

    context(CalmDownStrategy)
    fun throwItems(monkeys: Map<Int, Monkey>) {
        while (items.isNotEmpty()) {
            inspectedItemCount++
            val item = items.removeFirstOrNull() ?: return

            val result = operation.execute(item)
            val splitterResult = divideWorryLevel(result)
            val nextPosition = test.getNextThrowPosition(splitterResult)
            monkeys[nextPosition]!!.acceptItem(splitterResult)
        }
    }
}

interface CalmDownStrategy {
    fun divideWorryLevel(worryLevel: WorryLevel): WorryLevel
}

class DivideWorryLevelStrategy(
    private val divider: WorryLevel
): CalmDownStrategy {
    override fun divideWorryLevel(worryLevel: WorryLevel): WorryLevel {
        return worryLevel / divider
    }
}

class CommonModulusWorryLevelStrategy(
    private val commonModule: WorryLevel
): CalmDownStrategy {
    override fun divideWorryLevel(worryLevel: WorryLevel): WorryLevel {
        return worryLevel % commonModule
    }

}

context(CalmDownStrategy)
private fun getMostActiveMonkeys(monkeys: List<Monkey>, roundCount: Int): Long {
    val monkeysMap = monkeys.associateBy { it.index }.toSortedMap()
    (0 until roundCount).forEach {
        val startTime = System.currentTimeMillis()
        monkeysMap.forEach { i, monkey ->
            monkey.throwItems(monkeysMap)
        }
        val round = it + 1
        if(round == 1) {
            printMonkeysInspectedState(round, monkeysMap)
        }
        if(round == 20) {
            printMonkeysInspectedState(round, monkeysMap)
        }
        if(round % 1000 == 0) {
            printMonkeysInspectedState(round, monkeysMap)
        }

//        println("Roundtime $round ${System.currentTimeMillis() - startTime}")
    }

    return monkeysMap
        .values
        .map { it.inspectedItemCount }
        .sortedDescending()
        .take(2)
        .reduce { acc, i -> acc * i }
}

private fun printMonkeysInspectedState(round: Int, map: Map<Int, Monkey>) {
    println("== After round $round ==")
    map.forEach { i, monkey ->
        println("Monkey $i inspected items ${monkey.inspectedItemCount} times.")
    }
}

private fun File.parseMonkeys(): List<Monkey> {
    val monkeyList = mutableListOf<Monkey>()

    var index = 0
    var startingItems = listOf<WorryLevel>()
    var operation: Operation? = null
    var testDivisible: WorryLevel = 0
    var positiveThrow = 0
    var negativeThrow = 0

    fun createMonkey() {
        val monkey = Monkey(
            index = index,
            operation = operation!!,
            test = Test(
                divisibleCondition = testDivisible,
                positiveTarget = positiveThrow,
                negativeTarget = negativeThrow
            ),
            startingItems
        )
        monkeyList.add(monkey)
    }

    fun String.parseMonkeyParam() {
        when {
            startsWith("Monkey ") -> {
                index = removePrefix("Monkey ").removeSuffix(":").toInt()
            }
            startsWith("  Starting items: ") -> {
                startingItems = removePrefix("  Starting items: ").split(", ").map { it.toLong() }
            }
            startsWith("  Operation: new = ") -> {
                operation = removePrefix("  Operation: new = ").split(" ").let { operationElements ->
                    val firstOperand = Operation.Operand.getByString(operationElements[0])
                    val operator = Operation.Operator.values().first { it.sign == operationElements[1] }
                    val secondOperand = Operation.Operand.getByString(operationElements[2])
                    Operation(operator = operator, firstOperand = firstOperand, secondOperand = secondOperand)
                }
            }
            startsWith("  Test: divisible by ") -> {
                testDivisible = removePrefix("  Test: divisible by ").toLong()
            }
            startsWith("    If true: throw to monkey ") -> {
                positiveThrow = removePrefix("    If true: throw to monkey ").toInt()
            }
            startsWith("    If false: throw to monkey ") -> {
                negativeThrow = removePrefix("    If false: throw to monkey ").toInt()
            }
        }
    }

    bufferedReader()
        .forEachLine {
            if (it.isBlank()) createMonkey()
            else it.parseMonkeyParam()
        }
    createMonkey()
    return monkeyList
}

fun main() {

    val sampleFile = getSampleFile(11)
    val inputFile = getInputFile(11)

    val part1RoundCount = 20
    with(DivideWorryLevelStrategy(3)) {
        check(getMostActiveMonkeys(sampleFile.parseMonkeys(), part1RoundCount) == 10605L)
        println("Part1 = ${getMostActiveMonkeys(inputFile.parseMonkeys(), part1RoundCount)}")
    }

    val sampleMonkeys = sampleFile.parseMonkeys()
    val inputMonkeys = inputFile.parseMonkeys()
    val commonModuleSample = sampleMonkeys.map { it.test.divisibleCondition }.reduce { acc, i -> acc * i }
    val commonModuleInput = inputMonkeys.map { it.test.divisibleCondition }.reduce { acc, i -> acc * i }
    val part2RoundCount = 10000

    with(CommonModulusWorryLevelStrategy(commonModuleSample)) {
        check(getMostActiveMonkeys(sampleMonkeys, part2RoundCount) == 2713310158)
    }
    with(CommonModulusWorryLevelStrategy(commonModuleInput)) {
        println("Part2 = ${getMostActiveMonkeys(inputMonkeys, part2RoundCount)}")
    }

}