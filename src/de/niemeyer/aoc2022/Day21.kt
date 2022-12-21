/**
 * Advent of Code 2022, Day 21: Monkey Math
 * Problem Description: https://adventofcode.com/2022/day/21
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    abstract class MonkeyMath(val name: String) {
        abstract fun calc(): Long
    }

    var monkeys: Map<String, MonkeyMath> = mapOf()

    class MonkeyYell(name: String, val number: Long) : MonkeyMath(name) {
        override fun calc(): Long = number
    }

    class MonkeyCalc(name: String, val left: String, val right: String, val func: (a: Long, b: Long) -> Long) :
        MonkeyMath(name) {
        override fun calc(): Long = func(monkeys.getValue(left).calc(), monkeys.getValue(right).calc())
    }

    fun createMonkey(input: String): MonkeyMath {
        val monkeyName = input.substringBefore(":")
        val params = input.substringAfter(": ").split(" ")
        if (params.size == 1) {
            return MonkeyYell(monkeyName, params[0].toLong())
        }
        val left = params.first()
        val right = params.last()

        when (params[1]) {
            "+" -> return MonkeyCalc(monkeyName, left, right, Long::plus)
            "-" -> return MonkeyCalc(monkeyName, left, right, Long::minus)
            "*" -> return MonkeyCalc(monkeyName, left, right, Long::times)
            "/" -> return MonkeyCalc(monkeyName, left, right, Long::div)
            else -> error("unknown operator: ${params[1]}")
        }
    }

    fun parse(input: List<String>): Map<String, MonkeyMath> =
        input.map { createMonkey(it) }.associateBy { it.name }

    fun part1(input: List<String>): Long {
        monkeys = parse(input)
        return monkeys.getValue("root").calc()
    }

    fun part2(monkeys: List<String>): Long =
        TODO()

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    check(part1(testInput) == 152L)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 84_244_467_642_604L)

//    check(part2(testInput) == 0)
//    val puzzleResultPart2 = part2(puzzleInput)
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)
}
