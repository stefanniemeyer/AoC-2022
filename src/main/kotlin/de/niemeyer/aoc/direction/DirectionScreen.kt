@file:Suppress("unused")

package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D

sealed class DirectionScreen {
    val deltaUD = -1
    abstract val turnLeft: DirectionScreen
    abstract val turnRight: DirectionScreen
    abstract val offset: Point2D

    override fun toString(): String =
        when (this) {
            Up -> "U"
            Right -> "R"
            Down -> "D"
            Left -> "L"
        }

    object Up : DirectionScreen() {
        override val turnLeft = Left
        override val turnRight = Right
        override val offset = Point2D(0, deltaUD)
    }

    object Right : DirectionScreen() {
        override val turnLeft = Up
        override val turnRight = Down
        override val offset = Point2D(1, 0)
    }

    object Down : DirectionScreen() {
        override val turnLeft = Right
        override val turnRight = Left
        override val offset = Point2D(0, -deltaUD)
    }

    object Left : DirectionScreen() {
        override val turnLeft = Down
        override val turnRight = Up
        override val offset = Point2D(-1, 0)
    }

    companion object {
        val ARROWS = setOf('^', '>', 'v', '<')
    }
}

fun Char.toDirectionScreen(): DirectionScreen =
    toString().toDirectionScreen()

fun String.toDirectionScreen(): DirectionScreen =
    when (uppercase()) {
        "U", "UP" -> DirectionScreen.Up
        "R", "RIGHT" -> DirectionScreen.Right
        "D", "DOWN" -> DirectionScreen.Down
        "L", "LEFT" -> DirectionScreen.Left
        else -> throw IllegalArgumentException("No such grid direction '$this'")
    }

fun Char.arrowToDirectionScreen(): DirectionScreen =
    toString().arrowToDirectionScreen()

fun String.arrowToDirectionScreen(): DirectionScreen =
    when (this) {
        "^" -> DirectionScreen.Up
        ">" -> DirectionScreen.Right
        "v" -> DirectionScreen.Down
        "<" -> DirectionScreen.Left
        else -> throw IllegalArgumentException("No such arrow direction '$this'")
    }
