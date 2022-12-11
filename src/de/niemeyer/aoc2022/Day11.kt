/**
 * Advent of Code 2022, Day 11: Monkey in the Middle
 * Problem Description: https://adventofcode.com/2022/day/11
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsText

fun main() {
    fun solve(input: List<Monkey>, rounds: Int, adjust: (Long) -> Long): Long {
        val monkeys = input.toMutableList()
        repeat(rounds) {
            for (idx in 0 until monkeys.size) {
                val monkey = monkeys[idx]
                val newWorryLevels = monkey.items.map {
                    adjust(calc(it, monkey.operand, monkey.operation))
                }
                newWorryLevels.forEach {
                    val target = monkey.test.targetMoneky(it)
                    monkeys[target].items.add(it)
                }
                monkey.itemCounter += monkey.items.size
                monkey.items.removeIf { true }
                monkeys[idx] = monkey
            }
        }
        return monkeys.sortedBy { it.itemCounter }.takeLast(2).map { it.itemCounter }.product()
    }

    fun part1(input: List<Monkey>): Long =
        solve(input, 20) { level -> level / 3 }

    fun part2(input: List<Monkey>): Long {
        val gcd = input.map { it.test.divider }.product()
        return solve(input, 10_000) { level -> level % gcd }
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test").trim()
    val puzzleInput = resourceAsText(name).trim()

    val testMonkeys1 = Monkey.parse(testInput)
    val puzzleMonkeys1 = Monkey.parse(puzzleInput)

    check(part1(testMonkeys1) == 10_605L)
    val puzzleResultPart1 = part1(puzzleMonkeys1)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 117_640L)

    val testMonkeys2 = Monkey.parse(testInput)
    val puzzleMonkeys2 = Monkey.parse(puzzleInput)

    check(part2(testMonkeys2) == 2_713_310_158L)
    val puzzleResultPart2 = part2(puzzleMonkeys2)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 30_616_425_600L)
}

fun calc(a: Long, b: Long, operation: String): Long =
    when (operation) {
        "+" -> a + b
        "*" -> a * b
        "**" -> a * a
        else -> error("Operation '${operation}' is not supported")
    }

data class MonkeyTest(val divider: Long, val trueMonkey: Int, val falseMonkey: Int) {
    fun targetMoneky(level: Long) =
        if (level % divider == 0L) trueMonkey else falseMonkey
}

data class Monkey(
    val items: ArrayDeque<Long>,
    val operation: String,
    val operand: Long,
    val test: MonkeyTest,
    var itemCounter: Long = 0L
) {

    companion object {
        fun parse(input: String): List<Monkey> =
            input.split("\n\n")
                .map { of(it.lines()) }

        fun of(rules: List<String>): Monkey {
            val inpItems = rules[1].substringAfter(": ").split(", ").map { it.toLong() }
            val inpOpLine = rules[2]
            val inpOperation: String
            val inpOperand: Long
            if (inpOpLine.endsWith("old * old", true)) {
                inpOperation = "**"
                inpOperand = 1
            } else {
                val elements = inpOpLine.split(" ")
                inpOperation = elements.dropLast(1).last()
                inpOperand = elements.last().toLong()
            }
            val (divider, trueMonkey, falseMonkey) =
                rules.drop(3)
                    .map {
                        it.split(" ")
                            .last()
                            .toLong()
                    }

            return Monkey(
                items = ArrayDeque(inpItems),
                inpOperation,
                inpOperand,
                MonkeyTest(divider, trueMonkey.toInt(), falseMonkey.toInt())
            )
        }
    }
}
