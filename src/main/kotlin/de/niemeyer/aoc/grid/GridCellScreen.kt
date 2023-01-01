@file:Suppress("unused")

package de.niemeyer.aoc.grid

import de.niemeyer.aoc.direction.DirectionScreen
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

data class GridCellScreen(val row: Int, val column: Int) {
    val neighbors: List<GridCellScreen> by lazy {
        (row - 1..row + 1).flatMap { dRow ->
            (column - 1..column + 1).mapNotNull { dCol ->
                GridCellScreen(dRow, dCol).takeUnless { it == this }
            }
        }
    }

    val axisNeighbors: List<GridCellScreen> by lazy {
        listOf(GridCellScreen(0, 1), GridCellScreen(1, 0), GridCellScreen(0, -1), GridCellScreen(-1, 0)).map { this + it }
    }

    infix fun sharesAxisWith(that: GridCellScreen): Boolean =
        row == that.row || column == that.column

    operator fun plus(other: GridCellScreen): GridCellScreen =
        GridCellScreen(row + other.row, column + other.column)

    operator fun times(by: Int): GridCellScreen =
        GridCellScreen(row * by, column * by)

    fun move(directionScreen: DirectionScreen): GridCellScreen =
        this.moveTimes(directionScreen, 1)

    fun moveTimes(directionScreen: DirectionScreen, offset: Int): GridCellScreen =
        when (directionScreen) {
            DirectionScreen.Up -> GridCellScreen(row - offset, column)
            DirectionScreen.Right -> GridCellScreen(row, column + offset)
            DirectionScreen.Down -> GridCellScreen(row + offset, column)
            DirectionScreen.Left -> GridCellScreen(row, column - offset)
        }

    fun rotateLeft(): GridCellScreen =
        GridCellScreen(row = column, column = row * -1)

    fun rotateRight(): GridCellScreen =
        GridCellScreen(row = column * -1, column = row)

    infix fun lineTo(other: GridCellScreen): List<GridCellScreen> {
        val rowDelta = (other.row - row).sign
        val columnDelta = (other.column - column).sign
        val steps = maxOf((row - other.row).absoluteValue, (column - other.column).absoluteValue)
        return (1..steps).scan(this) { last, _ ->
            GridCellScreen(last.row + rowDelta, last.column + columnDelta)
        }
    }

    // calculate Manhattan distance between two points
    // https://de.wikipedia.org/wiki/Manhattan-Metrik
    infix fun manhattanDistanceTo(other: GridCellScreen): Int =
        (row - other.row).absoluteValue + (column - other.column).absoluteValue

    // calculate Chebyshev's chessboard distance
    // https://en.wikipedia.org/wiki/Chebyshev_distance
    infix fun chebyshevDistanceTo(other: GridCellScreen): Int =
        max((row - other.row).absoluteValue, (column - other.column).absoluteValue)

    override fun toString(): String = "(r=$row, c=$column)"

    companion object {
        val ORIGIN = GridCellScreen(0, 0)

        fun of(input: String, delimiter: String = ","): GridCellScreen =
            GridCellScreen(input.substringBefore(delimiter).toInt(), input.substringAfter(delimiter).toInt())
    }
}
