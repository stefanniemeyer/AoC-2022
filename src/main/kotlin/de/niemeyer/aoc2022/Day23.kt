/**
 * Advent of Code 2022, Day 23: Unstable Diffusion
 * Problem Description: https://adventofcode.com/2022/day/23
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.direction.CompassDirectionCCS
import de.niemeyer.aoc.points.*
import de.niemeyer.aoc.utils.Resources.resourceAsList
import de.niemeyer.aoc.utils.getClassName

fun main() {
    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsList(fileName = "${name}.txt")

    check(Day23(testInput).part1() == 110)
    val puzzleResultPart1 = Day23(puzzleInput).part1()
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 3_917)

    check(Day23(testInput).part2() == 20)
    val puzzleResultPart2 = Day23(puzzleInput).part2()
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 988)
}

class Day23(val input: List<String>) {
    var elfes = parsePoint2dSetBottomLeft(input).toMutableSet()
    val directions = ArrayDeque<CompassDirectionCCS>().apply {
        add(CompassDirectionCCS.North)
        add(CompassDirectionCCS.South)
        add(CompassDirectionCCS.West)
        add(CompassDirectionCCS.East)
    }

    fun round(): Boolean {
        val newElfes = mutableSetOf<Point2D>()
        val proposes = mutableMapOf<Point2D, MutableList<Point2D>>()
        elfes.forEach { elf ->
            if (elf.neighbors.count { it in elfes } == 0) {
                proposes.getOrPut(elf) { mutableListOf() }.add(elf) // elf has no neighbors, so it does not move
            } else {
                val candidate = directions.firstOrNull { direction ->
                    listOf(-45, 0, 45).none { testDir ->
                        elf.move(CompassDirectionCCS.fromDegree(direction.degree + testDir)) in elfes
                    }
                }?.let { elf.move(it) } ?: elf
                proposes.getOrPut(candidate) { mutableListOf() }.add(elf)
            }
        }

        var changes = false
        proposes.forEach { elf, candidates ->
            if (candidates.size == 1) {
                newElfes.add(elf)
                if (elf != candidates[0]) {
                    changes = true
                }
            } else {
                newElfes.addAll(candidates)
                changes = true
            }
        }

        elfes = newElfes
        directions.addLast(directions.removeFirst())

        return changes
    }

    fun part1(): Int {
        repeat(10) {
            round()
        }
        val xMin = elfes.minOf { it.x }
        val xMax = elfes.maxOf { it.x }
        val yMin = elfes.minOf { it.y }
        val yMax = elfes.maxOf { it.y }
        val emptyFields = (xMax - xMin + 1) * (yMax - yMin + 1) - elfes.size
        return emptyFields
    }

    fun part2(): Int =
        generateSequence(true) { round() }.takeWhile { it }.count()
}
