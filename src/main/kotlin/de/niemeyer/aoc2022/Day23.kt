/**
 * Advent of Code 2022, Day 23: Unstable Diffusion
 * Problem Description: https://adventofcode.com/2022/day/23
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    val directionsOrg = listOf(
        CompassDirection.North to listOf(
            CompassDirection.NorthWest, CompassDirection.North, CompassDirection.NorthEast
        ),
        CompassDirection.South to listOf(
            CompassDirection.SouthEast, CompassDirection.South, CompassDirection.SouthWest
        ),
        CompassDirection.West to listOf(
            CompassDirection.NorthWest, CompassDirection.West, CompassDirection.SouthWest
        ),
        CompassDirection.East to listOf(
            CompassDirection.NorthEast, CompassDirection.East, CompassDirection.SouthEast
        )
    )

    val directions = ArrayDeque<Pair<CompassDirection, List<CompassDirection>>>()
    var elfes: MutableSet<Point2D> = mutableSetOf()

    fun round(): Boolean {
        val newElfes = mutableSetOf<Point2D>()
        val proposes = mutableMapOf<Point2D, MutableList<Point2D>>()
        elfes.forEach { elf ->
            if (elf.neighbors.count { it in elfes } == 0) {
                proposes.getOrPut(elf) { mutableListOf() }.add(elf) // elf has no neighbors, so it does not move
            } else {
                val candidate = directions.firstOrNull {
                    it.second.none { testDir ->
                        elf.move(testDir) in elfes
                    }
                }?.let { elf.move(it.first) } ?: elf
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

    fun part1(input: Set<Point2D>): Int {
        elfes = input.toMutableSet()
        directions.clear()
        directions.addAll(directionsOrg)
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

    fun part2(input: Set<Point2D>): Int {
        directions.clear()
        directions.addAll(directionsOrg)
        elfes = input.toMutableSet()

        return generateSequence(true) { round() }.takeWhile { it }.count()
    }

    val name = getClassName()
    val testInput = parsePoint2dSetBottomLeft(resourceAsList(fileName = "${name}_test"))
    val puzzleInput = parsePoint2dSetBottomLeft(resourceAsList(name))

    check(part1(testInput) == 110)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 3_917)

    check(part2(testInput) == 20)
    val puzzleResultPart2 = part2(puzzleInput)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 988)
}
