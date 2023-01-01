/**
 * Advent of Code 2022, Day 18: Boiling Boulders
 * Problem Description: https://adventofcode.com/2022/day/18
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.points.Point3D
import de.niemeyer.aoc.utils.Resources.resourceAsList
import de.niemeyer.aoc.utils.getClassName

fun main() {
    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test.txt").map { Point3D.of(it) }.toSet()
    val puzzleInput = resourceAsList(fileName = "${name}.txt").map { Point3D.of(it) }.toSet()

    check(Pond(testInput).solvePart1() == 64)
    val puzzleResultPart1 = Pond(puzzleInput).solvePart1()
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 3_498)

    check(Pond(testInput).solvePart2() == 58)
    val puzzleResultPart2 = Pond(puzzleInput).solvePart2()
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 2_008)
}

class Pond(private val lava: Set<Point3D>) {
    private val cache = mutableMapOf<Point3D, Boolean>()
    private val maxPoint = Point3D(lava.maxOf { it.x }, lava.maxOf { it.y }, lava.maxOf { it.z })
    private val minPoint = Point3D(lava.minOf { it.x }, lava.minOf { it.y }, lava.minOf { it.z })

    fun solvePart1(): Int =
        lava.sumOf { validNeighbors(it).count() }

    fun solvePart2(): Int =
        lava.sumOf {
            validNeighbors(it).count { cand -> !cand.isAir() }
        }

    private fun Point3D.isAir(): Boolean {
        cache[this]?.let { return it }
        val outside = ArrayDeque(listOf(this))
        val visited = mutableSetOf<Point3D>()
        while (!outside.isEmpty()) {
            val p = outside.removeFirst()
            if (p in visited) {
                continue
            }
            visited.add(p)
            if (p in cache) {
                return addToCache(visited, cache.getValue(p))   // Point is in cache
            } else if (p.isOutsidePod()) {
                return addToCache(visited, isAir = false)       // Point is outside pond and cannot be air though
            } else {
                outside.addAll(validNeighbors(p))               // check valid neighbors
            }
        }
        return addToCache(visited, isAir = true)
    }

    private fun addToCache(points: Iterable<Point3D>, isAir: Boolean): Boolean {
        points.forEach { cache[it] = isAir }
        return isAir
    }

    private fun Point3D.isOutsidePod(): Boolean =
        x < minPoint.x || y < minPoint.y || z < minPoint.z || x > maxPoint.x || y > maxPoint.y || z > maxPoint.z

    private fun validNeighbors(p: Point3D): List<Point3D> =
        listOf(
            Point3D(p.x - 1, p.y, p.z),
            Point3D(p.x + 1, p.y, p.z),
            Point3D(p.x, p.y - 1, p.z),
            Point3D(p.x, p.y + 1, p.z),
            Point3D(p.x, p.y, p.z - 1),
            Point3D(p.x, p.y, p.z + 1),
        ).filter { it !in lava }
}

