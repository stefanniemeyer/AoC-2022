/**
 * Advent of Code 2022, Day 15: Beacon Exclusion Zone
 * Problem Description: https://adventofcode.com/2022/day/15
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.points.Point2D
import de.niemeyer.aoc.utils.Resources.resourceAsList
import de.niemeyer.aoc.utils.getClassName
import de.niemeyer.aoc.utils.intersects
import de.niemeyer.aoc.utils.union
import kotlin.math.absoluteValue

val inputSensorLineRegex =
    """Sensor at x=([-]?\d+), y=([-]?\d+): closest beacon is at x=([-]?\d+), y=([-]?\d+)""".toRegex()

fun String.toSensorBeacon(): Pair<Point2D, Point2D> {
    val (sensorX, sensorY, beaconX, beaconY) = inputSensorLineRegex
        .matchEntire(this)
        ?.destructured
        ?: throw IllegalArgumentException("Incorrect move input line $this")
    return Point2D(sensorX.toInt(), sensorY.toInt()) to Point2D(beaconX.toInt(), beaconY.toInt())
}

fun main() {
    val sensors = mutableMapOf<Point2D, Int>()
    val beacons = mutableSetOf<Point2D>()

    fun parseInput(input: List<String>) {
        sensors.clear()
        beacons.clear()
        input.map { it.toSensorBeacon() }
            .forEach { (sensor, beacon) ->
                sensors[sensor] = sensor manhattanDistanceTo beacon
                beacons += beacon
            }
    }

    fun part1(input: List<String>, row: Int): Int {
        parseInput(input)
        val sensorCandidates = sensors.filter { (it.key.y - row).absoluteValue <= it.value }
        val numBeaconsInRos = beacons.filter { it.y == row }.size
        val beaconFree = mutableSetOf<Int>()
        sensorCandidates.forEach { (sensor, distance) ->
            val xdist = distance - (sensor.y - row).absoluteValue
            (sensor.x - xdist..sensor.x + xdist).forEach { beaconFree += it }
        }
        return beaconFree.size - numBeaconsInRos
    }

    fun part2(input: List<String>, maxCoord: Int): Long {
        parseInput(input)
        for (y in 0..maxCoord) {
            val sensorCandidates = sensors.filter { (it.key.y - y).absoluteValue <= it.value }
            val sensorRanges =
                sensorCandidates.map { sensor ->
                    val xdist = sensor.value - (sensor.key.y - y).absoluteValue
                    maxOf(0, sensor.key.x - xdist)..minOf(maxCoord, sensor.key.x + xdist)
                }.sortedBy { it.first }
            val allIntersect = sensorRanges.reduce { acc, range ->
                val (union, distinct) = unionOrMissing(acc, range)
                if (distinct != IntRange.EMPTY) {
                    return@part2 distinct.first.toLong() * 4_000_000L + y
                }
                union
            }
            if (allIntersect.first != 0 || allIntersect.last != maxCoord) {
                error("something strange happened")
            }
        }
        return 0L
    }

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsList(fileName = "${name}.txt")

    check(part1(testInput, 10) == 26)
    val puzzleResultPart1 = part1(puzzleInput, 2_000_000)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 4_827_924)

    check(part2(testInput, 20) == 56_000_011L)
    val puzzleResultPart2 = part2(puzzleInput, 4_000_000)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 12_977_110_973_564L)
}

fun unionOrMissing(a: IntRange, b: IntRange): Pair<IntRange, IntRange> {
    if (a intersects b) {
        return (a union b) to IntRange.EMPTY
    }
    if (a.first < b.first) {
        // 0..10 12..20
        return IntRange.EMPTY to ((a.last + 1) until b.first)
    }
    // 12..20 0..10
    return IntRange.EMPTY to ((b.last + 1) until a.first)
}
