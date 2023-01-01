@file:Suppress("unused")

package de.niemeyer.aoc.points

import kotlin.math.absoluteValue

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

    infix fun sharesAxisWith(that: Point3D): Boolean = x == that.x || y == that.y || z == that.z

    val axisNeighbors: List<Point3D> by lazy {
        neighbors.filter { it.sharesAxisWith(this) }
    }

    operator fun plus(other: Point3D): Point3D = Point3D(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point3D): Point3D = Point3D(x - other.x, y - other.y, z - other.z)

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

    fun hexNeighbor(dir: String): Point3D {
        val dirUC = dir.uppercase()

        return if (dirUC in HEX_OFFSETS) HEX_OFFSETS.getValue(dirUC) + this
        else throw IllegalArgumentException("No dir: '$dirUC'")
    }

    companion object {
        val ORIGIN = Point3D(0, 0, 0)
        val HEX_OFFSETS = mapOf(
            "E" to Point3D(1, -1, 0),
            "W" to Point3D(-1, 1, 0),
            "NE" to Point3D(1, 0, -1),
            "NW" to Point3D(0, 1, -1),
            "SE" to Point3D(0, -1, 1),
            "SW" to Point3D(-1, 0, 1),
        )

        fun of(input: String, delimiter: String = ","): Point3D =
            input.split(delimiter).map { it.toInt() }.let { (x, y, z) -> Point3D(x, y, z) }
    }
}
