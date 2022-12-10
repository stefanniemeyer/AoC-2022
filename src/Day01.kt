/**
 * Advent of Code 2022, Day 1: Calorie Counting
 * Problem Description: https://adventofcode.com/2022/day/1
 */

import Resources.resourceAsText

fun main() {
    fun part1(input: List<Int>): Int =
        input.first()

    fun part2(input: List<Int>): Int =
        input.take(3).sum()

    fun readInput(fileName: String): List<Int> =
        resourceAsText(fileName).split("\n\n").map {
            it.trim().lines().sumOf(String::toInt)
        }.sortedDescending()

    val name = getClassName()
    val testInput = readInput(fileName = "${name}_test")
    val puzzleInput = readInput(fileName = name)

    check(part1(testInput) == 24_000)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 70_764)

    check(part2(testInput) == 45_000)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 203_905)
}
