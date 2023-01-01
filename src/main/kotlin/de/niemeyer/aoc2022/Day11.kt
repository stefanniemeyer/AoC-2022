/**
 * Advent of Code 2022, Day 11: Monkey in the Middle
 * Problem Description: https://adventofcode.com/2022/day/11
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.utils.Resources.resourceAsText
import de.niemeyer.aoc.utils.getClassName
import de.niemeyer.aoc.utils.product

fun main() {
    fun solve(input: String, rounds: Int, adjust: (Long) -> Long): Long {
        val monkeys = Monkey.parse(input).toMutableList()
        repeat(rounds) {
            for (idx in 0 until monkeys.size) {
                val monkey = monkeys[idx]
                val newWorryLevels = monkey.items.map {
                    adjust(monkey.increaseLevel(it))
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

    fun part1(input: String): Long =
        solve(input, 20) { level -> level / 3 }

    fun part2(input: String): Long {
        val gcd = Monkey.parse(input).map { it.test.divider }.product()
        return solve(input, 10_000) { level -> level % gcd }
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test.txt").trim()
    val puzzleInput = resourceAsText(fileName = "${name}.txt").trim()

    check(part1(testInput) == 10_605L)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 117_640L)

    check(part2(testInput) == 2_713_310_158L)
    val puzzleResultPart2 = part2(puzzleInput)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 30_616_425_600L)
}

data class MonkeyTest(val divider: Long, val trueMonkey: Int, val falseMonkey: Int) {
    fun targetMoneky(level: Long) =
        if (level % divider == 0L) trueMonkey else falseMonkey
}

data class Monkey(
    val items: ArrayDeque<Long>,
    val increaseLevel: (Long) -> Long,
    val test: MonkeyTest,
    var itemCounter: Long = 0L
) {

    companion object {
        fun parse(input: String): List<Monkey> =
            input.split("\n\n")
                .map { of(it.lines()) }

        fun of(rules: List<String>): Monkey {
            val inpItems = rules[1].substringAfter(": ").split(", ").map { it.toLong() }
            val opElements = rules[2].split(" ")
            val inpOperation = opElements.dropLast(1).last()
            val inpOperand = opElements.last()
            val levelFun = when (inpOperation) {
                "+" -> { x: Long -> x + inpOperand.toLong() }
                "*" -> when (inpOperand) {
                    "old" -> { x: Long -> x * x }
                    else -> { x: Long -> x * inpOperand.toLong() }
                }

                else -> error("Operation '${inpOperation}' is not supported")
            }
            val divider = rules[3].substringAfter("divisible by ").toLong()
            val trueMonkey = rules[4].substringAfter("monkey ").toLong()
            val falseMonkey = rules[5].substringAfter("monkey ").toLong()

            return Monkey(
                items = ArrayDeque(inpItems),
                levelFun,
                MonkeyTest(divider, trueMonkey.toInt(), falseMonkey.toInt())
            )
        }
    }
}
