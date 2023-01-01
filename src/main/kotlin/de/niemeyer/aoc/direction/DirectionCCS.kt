@file:Suppress("unused")

package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D

sealed class DirectionCCS {
    val deltaUD = 1
    abstract val turnLeft: DirectionCCS
    abstract val turnRight: DirectionCCS
    abstract val offset: Point2D

    override fun toString(): String =
        when (this) {
            Up -> "U"
            Right -> "R"
            Down -> "D"
            Left -> "L"
        }

    object Up : DirectionCCS() {
        override val turnLeft = Left
        override val turnRight = Right
        override val offset = Point2D(0, deltaUD)
    }

    object Right : DirectionCCS() {
        override val turnLeft = Up
        override val turnRight = Down
        override val offset = Point2D(1, 0)
    }

    object Down : DirectionCCS() {
        override val turnLeft = Right
        override val turnRight = Left
        override val offset = Point2D(0, -deltaUD)
    }

    object Left : DirectionCCS() {
        override val turnLeft = Down
        override val turnRight = Up
        override val offset = Point2D(-1, 0)
    }

    companion object {
        val ARROWS = setOf('^', '>', 'v', '<')
    }
}

fun Char.toDirectionCCS(): DirectionCCS =
    toString().toDirectionCCS()

fun String.toDirectionCCS(): DirectionCCS =
    when (uppercase()) {
        in listOf("U", "UP") -> DirectionCCS.Up
        in listOf("R", "RIGHT") -> DirectionCCS.Right
        in listOf("D", "DOWN") -> DirectionCCS.Down
        in listOf("L", "LEFT") -> DirectionCCS.Left
        else -> throw IllegalArgumentException("No such direction '$this'")
    }

fun Char.arrowToDirectionCCS(): DirectionCCS =
    toString().arrowToDirectionCCS()

fun String.arrowToDirectionCCS(): DirectionCCS =
    when (this) {
        "^" -> DirectionCCS.Up
        ">" -> DirectionCCS.Right
        "v" -> DirectionCCS.Down
        "<" -> DirectionCCS.Left
        else -> throw IllegalArgumentException("No such arrow direction '$this'")
    }
