@file:Suppress("unused")

package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D

/*
 * This file supports Compass Directions in a Screen Coordinate System
 */
sealed class CompassDirectionScreen {
    val deltaNS = -1
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

    object North : CompassDirectionScreen() {
        override val degree = 0
        override val offset = Point2D(0, deltaNS)
    }

    object NorthEast : CompassDirectionScreen() {
        override val degree = 45
        override val offset = Point2D(1, deltaNS)
    }

    object East : CompassDirectionScreen() {
        override val degree = 90
        override val offset = Point2D(1, 0)
    }

    object SouthEast : CompassDirectionScreen() {
        override val degree = 135
        override val offset = Point2D(1, -deltaNS)
    }

    object South : CompassDirectionScreen() {
        override val degree = 180
        override val offset = Point2D(0, -deltaNS)
    }

    object SouthWest : CompassDirectionScreen() {
        override val degree = 225
        override val offset = Point2D(-1, -deltaNS)
    }

    object West : CompassDirectionScreen() {
        override val degree = 270
        override val offset = Point2D(-1, 0)
    }

    object NorthWest : CompassDirectionScreen() {
        override val degree = 315
        override val offset = Point2D(-1, deltaNS)
    }

    companion object {
        fun fromDegree(degree: Int): CompassDirectionScreen =
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

fun Char.toCompassDirectionScreen(): CompassDirectionScreen =
    toString().toCompassDirectionScreen()

fun String.toCompassDirectionScreen(): CompassDirectionScreen =
    when (uppercase()) {
        "N" -> CompassDirectionScreen.North
        "NE" -> CompassDirectionScreen.NorthEast
        "E" -> CompassDirectionScreen.East
        "SE" -> CompassDirectionScreen.SouthEast
        "S" -> CompassDirectionScreen.South
        "SW" -> CompassDirectionScreen.SouthWest
        "W" -> CompassDirectionScreen.West
        "NW" -> CompassDirectionScreen.NorthWest
        else -> throw IllegalArgumentException("No such compass direction $this")
    }
