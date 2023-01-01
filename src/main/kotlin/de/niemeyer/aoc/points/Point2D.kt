@file:Suppress("unused")

package de.niemeyer.aoc.points

import de.niemeyer.aoc.direction.CompassDirectionCCS
import de.niemeyer.aoc.direction.DirectionCCS
import kotlin.math.*

interface Point {
    val neighbors: List<Point>
}

data class Point2D(val x: Int, val y: Int) : Point {
    override val neighbors: List<Point2D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).mapNotNull { dy ->
                Point2D(dx, dy).takeUnless { it == this }
            }
        }
    }

    val axisNeighbors: List<Point2D> by lazy {
        listOf(Point2D(0, 1), Point2D(1, 0),Point2D(0, -1),Point2D(-1, 0)).map { this + it }
    }

    infix fun sharesAxisWith(that: Point2D): Boolean = x == that.x || y == that.y

    operator fun plus(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)

    operator fun times(by: Int): Point2D = Point2D(x * by, y * by)

    fun move(compassDirectionCCS: CompassDirectionCCS): Point2D = this.moveTimes(compassDirectionCCS, 1)

    fun move(directionCCS: DirectionCCS): Point2D = this.moveTimes(directionCCS, 1)

    fun moveTimes(compassDirectionCCS: CompassDirectionCCS, offset: Int): Point2D =
        this + compassDirectionCCS.offset * offset

    fun moveTimes(directionCCS: DirectionCCS, offset: Int): Point2D =
        this + directionCCS.offset * offset

    fun rotateLeft(): Point2D = rotate(90)

    fun rotateRight(): Point2D = rotate(270)

    fun rotate(d: Int): Point2D {
        val cosD = cos(Math.toRadians(d.toDouble())).toInt()
        val sinD = sin(Math.toRadians(d.toDouble())).toInt()

        val x1 = x * cosD - y * sinD
        val y1 = x * sinD + y * cosD
        return Point2D(x1, y1)
    }

    fun reflection(d: Int): Point2D {
        val cos2D = cos(Math.toRadians(2.0 * d)).toInt()
        val sin2D = sin(Math.toRadians(2.0 * d)).toInt()

        val x1 = x * cos2D + y * sin2D
        val y1 = x * sin2D - y * cos2D
        return Point2D(x1, y1)
    }

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
    infix fun manhattanDistanceTo(other: Point2D): Int = (x - other.x).absoluteValue + (y - other.y).absoluteValue

    // calculate Chebyshev's chessboard distance
    // https://en.wikipedia.org/wiki/Chebyshev_distance
    infix fun chebyshevDistanceTo(other: Point2D): Int = max((x - other.x).absoluteValue, (y - other.y).absoluteValue)

    override fun toString(): String = "(x=$x, y=$y)"

    companion object {
        val ORIGIN = Point2D(0, 0)

        fun of(input: String, delimiter: String = ","): Point2D =
            Point2D(input.substringBefore(delimiter).toInt(), input.substringAfter(delimiter).toInt())
    }
}

