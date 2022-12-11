/**
 * Advent of Code 2022, Day 11:
 * Problem Description: https://adventofcode.com/2022/day/11
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsText

fun main() {
    fun part1(input: List<Monkey>, rounds: Int): Long {
        val monkeys = input.toMutableList()
        repeat(rounds) {
            for (idx in 0 until monkeys.size) {
                val monkey = monkeys[idx]
//                println(" Monkey ${idx}:")
                val newWorryLevels = monkey.items.map {
                    calc(it, monkey.operand, monkey.operation) / 3
                }
//                println(newWorryLevels)
                newWorryLevels.forEach {
                    val target = monkey.test.targetMoneky(it)
                    monkeys[target].items.add(it)
//                    println("   $it to monkey ${monkey.test.targetMoneky(it)}")
                }
                monkey.itemCounter += monkey.items.size
                monkey.items.removeIf { true }
                monkeys[idx] = monkey
            }
        }
        return monkeys.sortedBy { it.itemCounter }.takeLast(2).map { it.itemCounter }.product()
    }

    fun part2(input: List<Monkey>, rounds: Int): Long {
        val monkeys = input.toMutableList()
        val gcd = monkeys.map { it.test.divider }.product()
        repeat(rounds) {
            for (idx in 0 until monkeys.size) {
                val monkey = monkeys[idx]
//                println(" Monkey ${idx}:")
                val newWorryLevels = monkey.items.map {
                    calc(it, monkey.operand, monkey.operation) % gcd
                }
//                println(newWorryLevels)
                newWorryLevels.forEach {
                    val target = monkey.test.targetMoneky(it)
                    monkeys[target].items.add(it)
//                    println("   $it to monkey ${monkey.test.targetMoneky(it)}")
                }
                monkey.itemCounter += monkey.items.size
                monkey.items.removeIf { true }
                monkeys[idx] = monkey
            }
        }
        return monkeys.sortedBy { it.itemCounter }.takeLast(2).map { it.itemCounter }.product()
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test").trim()
    val puzzleInput = resourceAsText(name).trim()

    val testMonkeys1 = testInput
        .split("\n\n")
        .map { Monkey.of(it.lines()) }
    val puzzleMonkeys1 = puzzleInput
        .split("\n\n")
        .map { Monkey.of(it.lines()) }
    val roundsPart1 = 20

    check(part1(testMonkeys1, roundsPart1) == 10_605L)
    val puzzleResultPart1 = part1(puzzleMonkeys1, roundsPart1)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 117_640L)

    val testMonkeys2 = testInput
        .split("\n\n")
        .map { Monkey.of(it.lines()) }
    val puzzleMonkeys2 = puzzleInput
        .split("\n\n")
        .map { Monkey.of(it.lines()) }
    val roundsPart2 = 10_000

    check(part2(testMonkeys2, roundsPart2) == 2_713_310_158L)
    val puzzleResultPart2 = part2(puzzleMonkeys2, roundsPart2)
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
