/**
 * Advent of Code 2022, Day 18: Boiling Boulders
 * Problem Description: https://adventofcode.com/2022/day/18
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    fun part1(input: Set<Point3D>): Int {
        var sole = 0
        input.forEach {
            val ad = validNeighbors(it).count { it in input }
            sole += 6 - ad
        }
        return sole
    }

//    fun part2(input: Set<Point3D>): Int =
//        TODO()

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test").map { Point3D.of(it) }.toSet()
    val puzzleInput = resourceAsList(name).map { Point3D.of(it) }.toSet()

    check(part1(testInput) == 64)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 3_498)

//    check(part2(testInput) == 0)
//    val puzzleResultPart2 = part2(puzzleInput) 
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)
}

fun validNeighbors(p: Point3D): List<Point3D> =
    listOf(
        Point3D(p.x - 1, p.y, p.z),
        Point3D(p.x + 1, p.y, p.z),
        Point3D(p.x, p.y - 1, p.z),
        Point3D(p.x, p.y + 1, p.z),
        Point3D(p.x, p.y, p.z - 1),
        Point3D(p.x, p.y, p.z + 1),
    )
