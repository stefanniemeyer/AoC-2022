package de.niemeyer.aoc2022

/**
 * Advent of Code 2022, Day 6: Tuning Trouble
 * Problem Description: https://adventofcode.com/2022/day/6
 */

import de.niemeyer.aoc.utils.Resources.resourceAsText
import de.niemeyer.aoc.utils.getClassName

fun main() {
    fun detectDistinctChars(input: String, distChars: Int): Int =
        input.windowed(distChars, 1).indexOfFirst { it.toSet().size == distChars } + distChars

    fun part1(input: String): Int =
        detectDistinctChars(input, 4)

    fun part2(input: String): Int =
        detectDistinctChars(input, 14)

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsText(fileName = "${name}.txt")

    check(part1(testInput) == 7)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 1_965)

    check(part2(testInput) == 19)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 2_773)
}
