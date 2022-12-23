/**
 * Advent of Code 2022, Day 12: Hill Climbing Algorithm
 * Problem Description: https://adventofcode.com/2022/day/12
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    fun part1(input: Hightmap): Int =
        input.countMap[input.startPoint] ?: 0

    fun part2(input: Hightmap): Int =
        input.map.filterValues('a'::equals)
            .filterKeys { input.countMap.containsKey(it)}
            .keys.minOf { input.countMap.getValue(it) }

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    val testMap = Hightmap(testInput)
    check(part1(testMap) == 31)
    val puzzleMap = Hightmap(puzzleInput)
    println(part1(puzzleMap))
    check(part1(puzzleMap) == 504)

    check(part2(testMap) == 29)
    println(part2(puzzleMap))
    check(part2(puzzleMap) == 500)
}

class Hightmap(val input: List<String>) {
    var startPoint = Point2D.ORIGIN
    var endPoint = Point2D.ORIGIN
    val map: Map<Point2D, Char> = input.flatMapIndexed { row, line ->
        line.mapIndexed { col, elevation ->
            val point = Point2D(col, row)
            point to when (elevation) {
                'S' -> 'a'.also { startPoint = point }
                'E' -> 'z'.also { endPoint = point }
                else -> elevation
            }
        }
    }.toMap()

    val countMap = buildMap {
        var count = 0
        var candidates = setOf(endPoint)
        while (candidates.isNotEmpty()) {
            candidates = buildSet {
                candidates.forEach { candidate ->
                    if (putIfAbsent(candidate, count) != null) {
                        return@forEach
                    }
                    addAll(validNeighbors((candidate)))
                }
            }
            count++
        }
    }

    fun validNeighbors(candidate: Point2D): List<Point2D> {
        val candidateElevation = map.getOrDefault(candidate, Char.MAX_VALUE)

        return candidate.neighbors.filter { neighbor ->
            val neighborElevation = map.getOrDefault(neighbor, Char.MAX_VALUE)
            neighbor.x >= 0 && neighbor.y >= 0
                    && candidate.sharesAxisWith(neighbor)
                    && neighbor in map.keys
                    && candidateElevation - neighborElevation <= 1
        }
    }

}
