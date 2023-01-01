/**
 * Advent of Code 2022, Day 3: Rucksack Reorganization
 * Problem Description: https://adventofcode.com/2022/day/3
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.utils.Resources.resourceAsList
import de.niemeyer.aoc.utils.getClassName

fun main() {
    fun part1(input: List<String>): Int =
        input.map { rucksack ->
            val compartmentItems = rucksack.chunked(rucksack.length / 2).map { it.toCharArray().toSet() }
            val inter = compartmentItems[0].intersect(compartmentItems[1])
            inter.first().priority()
        }.sum()

    fun part2(input: List<String>): Int =
        input.windowed(3, 3).map { rucksacks ->
            val rucksackItems = rucksacks.map { it.toCharArray().toSet() }
            val inter = rucksackItems.reduce { acc, set -> acc.intersect(set) }
            inter.first().priority()
        }.sum()

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsList(fileName = "${name}.txt")

    check(part1(testInput) == 157)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 7_817)

    check(part2(testInput) == 70)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 2_444)
}

fun Char.priority(): Int = when (this) {
    in 'a'..'z' -> this - 'a' + 1
    else -> this - 'A' + 27
}
