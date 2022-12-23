/*
 * Most of the code comes from Todd Ginsberg
 */

@file:Suppress("unused")

package de.niemeyer.aoc2022

import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.math.max

data class Point2D(val x: Int, val y: Int) : Point {
    override val neighbors: List<Point2D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).mapNotNull { dy ->
                Point2D(dx, dy).takeUnless { it == this }
            }
        }
    }

    val axisNeighbors: List<Point2D> by lazy {
        neighbors.filter { it.sharesAxisWith(this) }
    }

    infix fun sharesAxisWith(that: Point2D): Boolean =
        x == that.x || y == that.y

    operator fun plus(other: Point2D): Point2D =
        Point2D(x + other.x, y + other.y)

    operator fun times(by: Int): Point2D =
        Point2D(x * by, y * by)

    fun move(compassDirection: CompassDirection): Point2D =
        this.moveTimes(compassDirection, 1)

    fun move(direction: Direction): Point2D =
        this.moveTimes(direction, 1)

    fun moveTimes(compassDirection: CompassDirection, offset: Int): Point2D =
        when (compassDirection) {
            CompassDirection.North -> Point2D(x, y + offset)
            CompassDirection.East -> Point2D(x + offset, y)
            CompassDirection.South -> Point2D(x, y - offset)
            CompassDirection.West -> Point2D(x - offset, y)
        }

    fun moveTimes(direction: Direction, offset: Int): Point2D =
        when (direction) {
            Direction.Up -> Point2D(x, y + offset)
            Direction.Right -> Point2D(x + offset, y)
            Direction.Down -> Point2D(x, y - offset)
            Direction.Left -> Point2D(x - offset, y)
        }

    fun rotateLeft(): Point2D =
        Point2D(x = y * -1, y = x)

    fun rotateRight(): Point2D =
        Point2D(x = y, y = x * -1)

    infix fun lineTo(other: Point2D): List<Point2D> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign
        val steps = maxOf((x - other.x).absoluteValue, (y - other.y).absoluteValue)
        return (1..steps).scan(this) { last, _ ->
            Point2D(last.x + xDelta, last.y + yDelta)
        }
    }

    // calculate Manhattan distance between two points
    // https://de.wikipedia.org/wiki/Manhattan-Metrik
    infix fun manhattanDistanceTo(other: Point2D): Int =
        (x - other.x).absoluteValue + (y - other.y).absoluteValue

    // calculate Chebyshev's chessboard distance
    // https://en.wikipedia.org/wiki/Chebyshev_distance
    infix fun chebyshevDistanceTo(other: Point2D): Int =
        max((x - other.x).absoluteValue, (y - other.y).absoluteValue)

    override fun toString(): String = "(x=$x, y=$y)"

    companion object {
        val ORIGIN = Point2D(0, 0)

        fun of(input: String, delimiter: String = ","): Point2D =
            Point2D(input.substringBefore(delimiter).toInt(), input.substringAfter(delimiter).toInt())
    }
}

fun Map<Point2D, Boolean>.printExisting() {
    val points = keys.toList()
    val rows = points.maxOf { it.y }
    val columns = points.maxOf { it.x }

    for (y in 0..rows) {
        for (x in 0..columns) {
            if (this.containsKey(Point2D(x, y))) {
                print(if (this.getValue(Point2D(x, y))) '#' else '.')
            } else {
                print(" ")
            }
        }
        println()
    }
}

fun Map<Point2D, Boolean>.printWithDefault(default: Boolean = false) {
    val points = keys.toList()
    val rows = points.maxOf { it.y }
    val columns = points.maxOf { it.x }

    for (y in 0..rows) {
        for (x in 0..columns) {
            print(if (this.getOrDefault(Point2D(x, y), default)) '#' else '.')
        }
        println()
    }
}

fun Set<Point2D>.print() {
    val xMin = this.minOf { it.x }
    val xMax = this.maxOf { it.x }
    val yMin = this.minOf { it.y }
    val yMax = this.maxOf { it.y }

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            print(if (this.contains(Point2D(x, y))) '#' else '.')
        }
        println()
    }
}

interface Point {
    val neighbors: List<Point>
}

data class Point3D(val x: Int, val y: Int, val z: Int) : Point {
    override val neighbors: List<Point3D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).flatMap { dy ->
                (z - 1..z + 1).mapNotNull { dz ->
                    Point3D(dx, dy, dz).takeUnless { it == this }
                }
            }
        }
    }

    val hexNeighbors: List<Point3D> by lazy {
        HEX_OFFSETS.map { this + it.value }
    }

    infix fun sharesAxisWith(that: Point3D): Boolean =
        x == that.x || y == that.y || z == that.z

    val axisNeighbors: List<Point3D> by lazy {
        neighbors.filter { it.sharesAxisWith(this) }
    }

    operator fun plus(other: Point3D): Point3D =
        Point3D(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point3D): Point3D =
        Point3D(x - other.x, y - other.y, z - other.z)

    fun rotate(d: Int): Point3D {
        val c0 = d % 3
        val c0s = 1 - ((d / 3) % 2) * 2
        val c1 = (c0 + 1 + (d / 6) % 2) % 3
        val c1s = 1 - (d / 12) * 2
        val c2 = 3 - c0 - c1
        val c2s = c0s * c1s * (if (c1 == (c0 + 1) % 3) 1 else -1)
        val tp = listOf(this.x, this.y, this.z)
        return Point3D(tp[c0] * c0s, tp[c1] * c1s, tp[c2] * c2s)
    }

    infix fun distanceTo(other: Point3D): Int =
        (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue + (this.z - other.z).absoluteValue

    fun hexNeighbor(dir: String): Point3D =
        if (dir in HEX_OFFSETS) HEX_OFFSETS.getValue(dir) + this
        else throw IllegalArgumentException("No dir: $dir")

    companion object {
        val ORIGIN = Point3D(0, 0, 0)
        val HEX_OFFSETS = mapOf(
            "e" to Point3D(1, -1, 0),
            "w" to Point3D(-1, 1, 0),
            "ne" to Point3D(1, 0, -1),
            "nw" to Point3D(0, 1, -1),
            "se" to Point3D(0, -1, 1),
            "sw" to Point3D(-1, 0, 1),
        )

        fun of(input: String, delimiter: String = ","): Point3D =
            input.split(delimiter).map { it.toInt() }.let { (x, y, z) -> Point3D(x, y, z) }
    }
}

data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) : Point {
    override val neighbors: List<Point4D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).flatMap { dy ->
                (z - 1..z + 1).flatMap { dz ->
                    (w - 1..w + 1).mapNotNull { dw ->
                        Point4D(dx, dy, dz, dw).takeUnless { it == this }
                    }
                }
            }
        }
    }
}
