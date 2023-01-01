@file:Suppress("unused")

package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D

/*
 * This file supports Compass Directions in a Cartesian Coordinate System (CCS)
 */
sealed class CompassDirectionCCS {
    val deltaNS = 1
    abstract val degree: Int
    abstract val offset: Point2D
    val turnLeft by lazy { fromDegree(degree - 90) }
    val turnRight by lazy { fromDegree(degree + 90) }
    val turnHalfLeft by lazy { fromDegree(degree - 45) }
    val turnHalfRight by lazy { fromDegree(degree + 45) }

    override fun toString(): String =
        when (this) {
            is North -> "N"
            is NorthEast -> "NE"
            is East -> "E"
            is SouthEast -> "SE"
            is South -> "S"
            is SouthWest -> "SW"
            is West -> "W"
            is NorthWest -> "NW"
        }

    object North : CompassDirectionCCS() {
        override val degree = 0
        override val offset = Point2D(0, deltaNS)
    }

    object NorthEast : CompassDirectionCCS() {
        override val degree = 45
        override val offset = Point2D(1, deltaNS)
    }

    object East : CompassDirectionCCS() {
        override val degree = 90
        override val offset = Point2D(1, 0)
    }

    object SouthEast : CompassDirectionCCS() {
        override val degree = 135
        override val offset = Point2D(1, -deltaNS)
    }

    object South : CompassDirectionCCS() {
        override val degree = 180
        override val offset = Point2D(0, -deltaNS)
    }

    object SouthWest : CompassDirectionCCS() {
        override val degree = 225
        override val offset = Point2D(-1, -deltaNS)
    }

    object West : CompassDirectionCCS() {
        override val degree = 270
        override val offset = Point2D(-1, 0)
    }

    object NorthWest : CompassDirectionCCS() {
        override val degree = 315
        override val offset = Point2D(-1, deltaNS)
    }

    companion object {
        fun fromDegree(degree: Int): CompassDirectionCCS =
            when (Math.floorMod(degree, 360)) {
                0 -> North
                45 -> NorthEast
                90 -> East
                135 -> SouthEast
                180 -> South
                225 -> SouthWest
                270 -> West
                315 -> NorthWest
                else -> throw IllegalArgumentException("No such compass direction $degree")
            }
    }
}

fun Char.toCompassDirectionCCS(): CompassDirectionCCS =
    toString().toCompassDirectionCCS()

fun String.toCompassDirectionCCS(): CompassDirectionCCS =
    when (uppercase()) {
        "N" -> CompassDirectionCCS.North
        "NE" -> CompassDirectionCCS.NorthEast
        "E" -> CompassDirectionCCS.East
        "SE" -> CompassDirectionCCS.SouthEast
        "S" -> CompassDirectionCCS.South
        "SW" -> CompassDirectionCCS.SouthWest
        "W" -> CompassDirectionCCS.West
        "NW" -> CompassDirectionCCS.NorthWest
        else -> throw IllegalArgumentException("No such compass direction $this")
    }
