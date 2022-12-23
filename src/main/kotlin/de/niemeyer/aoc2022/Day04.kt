/**
 * Advent of Code 2022, Day 4: Camp Cleanup
 * Problem Description: https://adventofcode.com/2022/day/4
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

typealias Assignment = Pair<IntRange, IntRange>

fun String.toIntRange(): IntRange {
    val (from, to) = this.split("-").map(String::toInt)
    return from..to
}

infix fun IntRange.completelyOverlaps(other: IntRange): Boolean =
    first <= other.first && other.last <= last || other.first <= first && last <= other.last

fun parseInput(input: List<String>): List<Assignment> = input.map { line ->
    line.split(",")
}.map { it.first().toIntRange() to it.last().toIntRange() }

fun main() {
    fun part1(input: List<Assignment>): Int = input.map { (a, b) ->
        a completelyOverlaps b
    }.count { it }

    fun part2(input: List<Assignment>): Int = input.map { (a, b) ->
        a intersects b
    }.count { it }

    val name = getClassName()
    val testInput = parseInput(resourceAsList(fileName = "${name}_test"))
    val puzzleInput = parseInput(resourceAsList(name))

    check(part1(testInput) == 2)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 534)

    check(part2(testInput) == 4)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 841)
}

