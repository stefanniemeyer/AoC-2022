/**
 * Advent of Code 2022, Day 6: Tuning Trouble
 * Problem Description: https://adventofcode.com/2022/day/6
 */

import Resources.resourceAsText

fun main() {
    fun detectDistinctChars(input: String, distChars: Int): Int =
        input.windowed(distChars, 1).indexOfFirst { it.toSet().size == distChars } + distChars

    fun part1(input: String): Int =
        detectDistinctChars(input, 4)

    fun part2(input: String): Int =
        detectDistinctChars(input, 14)

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsText(fileName = "${name}_test")
    val puzzleInput = resourceAsText(name)

    check(part1(testInput) == 7)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 1_965)

    check(part2(testInput) == 19)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 2_773)
}
