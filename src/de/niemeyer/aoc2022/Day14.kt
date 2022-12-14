/**
 * Advent of Code 2022, Day 14:
 * Problem Description: https://adventofcode.com/2022/day/14
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

typealias ReservoirMap = Map<Point2D, Char>

fun main() {
    val sand = Point2D(500, 0)
    var air = '.'
    val moveDown = Point2D(0, 1)
    val moveLeft = Point2D(-1, 1)
    val moveRight = Point2D(1, 1)

    fun parse(input: List<String>): Map<Point2D, Char> =
        input.map { line ->
            line.split("->")
                .map { Point2D.of(it.trim()) }
                .zipWithNext()
                .map { (a, b) ->
                    (a lineTo b).map { rock -> rock to '#' }
                }
        }.flatten().flatten().toMap()

    fun getElement(map: MutableMap<Point2D, Char>, point: Point2D, floor: Int): Char {
        if (point.y == floor) {
            return '#' // floor
        }
        return map.getOrDefault(point, air)
    }

    fun solve(reservoir: ReservoirMap, infiniteFloor: Boolean = false): Int {
        val map = reservoir.toMutableMap()
        val abyssBeginn = map.keys.maxBy { it.y }.y
        val floor = if (infiniteFloor) abyssBeginn + 2 else -1
        var currentPos = sand
        var units = 0
        var loops = 0

        while (currentPos.y <= abyssBeginn || infiniteFloor) {
            val down = currentPos + moveDown

            if (getElement(map, down, floor) == air) {
                currentPos = down
            } else {
                val left = currentPos + moveLeft
                if (getElement(map, left, floor) == air) {
                    currentPos = left
                } else {
                    val right = currentPos + moveRight
                    if (getElement(map, right, floor) == air) {
                        currentPos = right
                    } else {
                        map[currentPos] = 'o'
                        units++
                        if (currentPos == sand) {
                            break
                        }
                        currentPos = sand
                    }
                }
            }
            loops++
        }

        return units
    }

    fun part1(map: ReservoirMap): Int =
        solve(map)

    fun part2(map: ReservoirMap): Int =
        solve(map, infiniteFloor = true)

    val name = getClassName()
    val testInput = parse(resourceAsList(fileName = "${name}_test")).toMap()
    val puzzleInput = parse(resourceAsList(name)).toMap()

    check(part1(testInput) == 24)
    val testResultPart1 = part1(puzzleInput)
    println(testResultPart1)
    check(testResultPart1 == 1001)

    check(part2(testInput) == 93)
    val testResultPart2 = part2(puzzleInput)
    println(testResultPart2)
    check(testResultPart2 == 27_976)
}

