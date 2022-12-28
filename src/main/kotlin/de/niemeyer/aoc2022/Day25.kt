/**
 * Advent of Code 2022, Day 25: Full of Hot Air
 * Problem Description: https://adventofcode.com/2022/day/26
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    fun Char.toSnafuDigit(): Int =
        when {
            this == '-' -> -1
            this == '=' -> -2
            else -> digitToInt()
        }

    fun String.toSnafu(): Long =
        fold(0) { acc, c ->
            (acc * 5) + c.toSnafuDigit()
        }

    fun Long.toSnafu(): String =
        generateSequence(this) { (it + 2) / 5 }
            .takeWhile { it != 0L }
            .map { "012=-"[(it % 5).toInt()] }
            .joinToString("")
            .reversed()

    fun part1(input: List<String>): String =
        input.sumOf { it.toSnafu() }.toSnafu()

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    check(part1(testInput) == "2=-1=0")
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == "2011-=2=-1020-1===-1")
}
