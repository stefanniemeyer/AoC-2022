/**
 * Advent of Code 2022, Day 21: Monkey Math
 * Problem Description: https://adventofcode.com/2022/day/21
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    val root = "root"
    val human = "humn"

    abstract class MonkeyMath(val name: String) {
        abstract fun calc(): Long
        abstract fun restoreResult(result: Long): Long
        abstract fun containsName(searchName: String): Boolean
        fun containsHuman() = containsName(human)
        fun isHuman() = name == human
    }

    var monkeys: Map<String, MonkeyMath> = mapOf()

    class MonkeyYell(name: String, val number: Long) : MonkeyMath(name) {
        override fun calc(): Long = number
        override fun restoreResult(result: Long): Long =
            error("don't call restoreResult on MonkeyYell")

        override fun containsName(searchName: String): Boolean =
            name == searchName
    }

    class MonkeyCalc(
        name: String,
        val left: String,
        val right: String,
        val func: (a: Long, b: Long) -> Long,
        val inverseFunc: (a: Long, b: Long) -> Long,
        val commutation: Boolean
    ) :
        MonkeyMath(name) {
        val leftMonkey by lazy { monkeys.getValue(left) }
        val rightMonkey by lazy { monkeys.getValue(right) }
        override fun calc(): Long = func(monkeys.getValue(left).calc(), monkeys.getValue(right).calc())
        override fun restoreResult(result: Long): Long {
            return if (leftMonkey.isHuman()) {
                val rightResult = rightMonkey.calc()
                val newResult = inverseFunc(result, rightResult)
                newResult
            } else if (rightMonkey.isHuman()) {
                val leftResult = leftMonkey.calc()
                var newResult: Long
                if (commutation) {
                    newResult = inverseFunc(result, leftResult)
                } else {
                    newResult = func(leftResult, result)
                }
                newResult
            } else if (leftMonkey.containsHuman()) {
                val rightResult = rightMonkey.calc()
                val newResult = inverseFunc(result, rightResult)
                val leftResult = leftMonkey.restoreResult(newResult)
                leftResult
            } else if (rightMonkey.containsHuman()) {
                val leftResult = leftMonkey.calc()
                val newResult: Long
                if (commutation) {
                    newResult = inverseFunc(result, leftResult)
                } else {
                    newResult = func(leftResult, result)
                }
                val rightResult = rightMonkey.restoreResult(newResult)
                rightResult
            } else {
                result
            }
        }

        override fun containsName(searchName: String): Boolean =
            name == searchName || monkeys.getValue(left).containsName(searchName) || monkeys.getValue(right)
                .containsName(searchName)
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
            "+" -> return MonkeyCalc(monkeyName, left, right, Long::plus, Long::minus, commutation = true)
            "-" -> return MonkeyCalc(monkeyName, left, right, Long::minus, Long::plus, commutation = false)
            "*" -> return MonkeyCalc(monkeyName, left, right, Long::times, Long::div, commutation = true)
            "/" -> return MonkeyCalc(monkeyName, left, right, Long::div, Long::times, commutation = false)
            else -> error("unknown operator: ${params[1]}")
        }
    }

    fun parse(input: List<String>): Map<String, MonkeyMath> =
        input.map { createMonkey(it) }.associateBy { it.name }

    fun part1(input: List<String>): Long {
        monkeys = parse(input)
        return monkeys.getValue(root).calc()
    }

    fun part2(input: List<String>): Long {
        monkeys = parse(input)
        val rootMonkey = monkeys.getValue(root)
        if (rootMonkey is MonkeyCalc) {
            val leftMonkey = rootMonkey.leftMonkey
            val rightMonkey = rootMonkey.rightMonkey
            val noHumanResult: Long
            val humanResult: Long
            val humanLeft = leftMonkey.containsHuman()
            val humanRight = rightMonkey.containsHuman()
            check(humanLeft || humanRight) { "'$human' not found" }
            check(!(humanLeft && humanRight)) { "two '$human' found" }

            if (humanLeft) {    // calculate the tree w/o the human
                noHumanResult = rightMonkey.calc()
                humanResult = leftMonkey.restoreResult(noHumanResult)
            } else {
                noHumanResult = leftMonkey.calc()
                humanResult = rightMonkey.restoreResult(noHumanResult)
            }
            return humanResult
        }
        error("root monkey is not a MonkeyCalc")
    }

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    check(part1(testInput) == 152L)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 84_244_467_642_604L)

    check(part2(testInput) == 301L)
    val puzzleResultPart2 = part2(puzzleInput)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 3_759_569_926_192L)
}
