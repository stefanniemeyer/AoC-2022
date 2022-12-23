@file:Suppress("unused")

package de.niemeyer.aoc2022

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

data class GridCell(val row: Int, val column: Int) {
    val neighbors: List<GridCell> by lazy {
        (row - 1..row + 1).flatMap { dRow ->
            (column - 1..column + 1).mapNotNull { dCol ->
                GridCell(dRow, dCol).takeUnless { it == this }
            }
        }
    }

    val axisNeighbors: List<GridCell> by lazy {
        neighbors.filter { it.sharesAxisWith(this) }
    }

    infix fun sharesAxisWith(that: GridCell): Boolean =
        row == that.row || column == that.column

    operator fun plus(other: GridCell): GridCell =
        GridCell(row + other.row, column + other.column)

    operator fun times(by: Int): GridCell =
        GridCell(row * by, column * by)

    fun move(gridDirection: GridDirection): GridCell =
        this.moveTimes(gridDirection, 1)

    fun moveTimes(gridDirection: GridDirection, offset: Int): GridCell =
        when (gridDirection) {
            GridDirection.Up -> GridCell(row - offset, column)
            GridDirection.Right -> GridCell(row, column + offset)
            GridDirection.Down -> GridCell(row + offset, column)
            GridDirection.Left -> GridCell(row, column - offset)
        }

    fun rotateLeft(): GridCell =
        GridCell(row = column, column = row * -1)

    fun rotateRight(): GridCell =
        GridCell(row = column * -1, column = row)

    infix fun lineTo(other: GridCell): List<GridCell> {
        val rowDelta = (other.row - column).sign
        val columnDelta = (other.column - column).sign
        val steps = maxOf((row - other.row).absoluteValue, (column - other.column).absoluteValue)
        return (1..steps).scan(this) { last, _ ->
            GridCell(last.row + rowDelta, last.column + columnDelta)
        }
    }

    // calculate Manhattan distance between two points
    // https://de.wikipedia.org/wiki/Manhattan-Metrik
    infix fun manhattanDistanceTo(other: GridCell): Int =
        (row - other.row).absoluteValue + (column - other.column).absoluteValue

    // calculate Chebyshev's chessboard distance
    // https://en.wikipedia.org/wiki/Chebyshev_distance
    infix fun chebyshevDistanceTo(other: GridCell): Int =
        max((row - other.row).absoluteValue, (column - other.column).absoluteValue)

    override fun toString(): String = "(r=$row, c=$column)"

    companion object {
        val ORIGIN = GridCell(0, 0)

        fun of(input: String, delimiter: String = ","): GridCell =
            GridCell(input.substringBefore(delimiter).toInt(), input.substringAfter(delimiter).toInt())
    }
}
