@file:Suppress("unused")

package de.niemeyer.aoc.grid

import de.niemeyer.aoc.direction.DirectionScreen

enum class ORIENTATION {
    NORMAL,
    FLIPPED
}

data class TileInstructions(val rotate: DirectionScreen = DirectionScreen.Up, val orient: ORIENTATION = ORIENTATION.NORMAL)

data class GridCellContainer(val value: Boolean, val original: GridCellScreen = GridCellScreen(0, 0))

class Grid(var gridMap: Map<GridCellScreen, GridCellContainer>, val offset: GridCellScreen = GridCellScreen(0, 0)) {
    val rowMin = if (gridMap.isEmpty()) 0 else gridMap.keys.minOf { it.row }
    val rowMax = if (gridMap.isEmpty()) 0 else gridMap.keys.maxOf { it.row }
    val columnMin = if (gridMap.isEmpty()) 0 else gridMap.keys.minOf { it.column }
    val columnMax = if (gridMap.isEmpty()) 0 else gridMap.keys.maxOf { it.column }

    val columnRangesForRows by lazy {
        val rowRange = gridMap.keys.minOf { it.row }..gridMap.keys.maxOf { it.row }
        rowRange.associateWith { row ->
            gridMap.keys.filter { it.row == row }.minOf { it.column }..gridMap.keys.filter { it.row == row }
                .maxOf { it.column }
        }
    }

    val rowRangesForColumns by lazy {
        val colRange = gridMap.keys.minOf { it.column }..gridMap.keys.maxOf { it.column }
        colRange.associateWith { col ->
            gridMap.keys.filter { it.column == col }.minOf { it.row }..gridMap.keys.filter { it.column == col }
                .maxOf { it.row }
        }
    }

    fun rotate(instructions: TileInstructions): Grid {
        val rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
        val columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
        var newOffset = offset

        val result = mutableMapOf<GridCellScreen, GridCellContainer>()
        val transFunc: (Int, Int) -> GridCellScreen
        when (instructions.orient) {
            ORIENTATION.NORMAL -> {
                when (instructions.rotate) {
                    DirectionScreen.Up -> transFunc = { r, c -> GridCellScreen(r, c) }

                    DirectionScreen.Right -> {
                        transFunc = { r, c -> GridCellScreen(c, rowMax - r + offset.row) }
                        newOffset = GridCellScreen(offset.column, offset.row)
                    }

                    DirectionScreen.Down -> {
                        transFunc = { r, c -> GridCellScreen(rowMax - r + offset.row, columnMax - c + offset.column) }
                    }

                    DirectionScreen.Left -> {
                        transFunc = { r, c -> GridCellScreen(columnMax - c + offset.column, r) }
                        newOffset = GridCellScreen(offset.column, offset.row)
                    }
                }

                for (row in rowProg) {
                    for (column in columnProg) {
                        result[transFunc(row, column)] = gridMap.getValue(GridCellScreen(row, column))
                    }
                }
                result.toMap()
            }

            else -> error("Unknown orientation ${instructions.orient}")
        }

//            ORIENTATION.FLIPPED -> {
//                when (instructions.rotate) {
//                    DirectionScreen.Up -> {
//                        rowProg = IntProgression.fromClosedRange(rowMax, rowMin, -1)
//                        columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
//                    }
//
//                    DirectionScreen.Right -> {
//                        rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
//                        columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
//                    }
//
//                    DirectionScreen.Down -> {
//                        rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
//                        columnProg = IntProgression.fromClosedRange(columnMax, columnMin, -1)
//                    }
//
//                    DirectionScreen.Left -> {
//                        rowProg = IntProgression.fromClosedRange(rowMax, rowMin, -1)
//                        columnProg = IntProgression.fromClosedRange(columnMax, columnMin, -1)
//                    }
//                }
//            }
//

        return Grid(result.toMap(), newOffset)
    }

    fun printExisting() =
        println(toPrintableStringExisting())

    fun toPrintableStringExisting(): String =
        buildString {
            for (row in offset.row..rowMax) {
                for (column in offset.column..columnMax) {
                    if (gridMap.containsKey(GridCellScreen(row, column))) {
                        append(if (gridMap.getValue(GridCellScreen(row, column)).value) '#' else '.')
                    } else {
                        append(" ")
                    }
                }
                appendLine()
            }
        }.removeSuffix(System.lineSeparator())

    fun printWithDefault(default: Boolean = false) =
        println(toPrintableStringWithDefault(default))

    fun toPrintableStringWithDefault(default: Boolean = false): String =
        buildString {
            for (row in rowMin..rowMax) {
                for (column in columnMin..columnMax) {
                    append(
                        if (gridMap.getOrDefault(
                                GridCellScreen(row, column),
                                GridCellContainer(default)
                            ).value
                        ) '#' else '.'
                    )
                }
                appendLine()
            }
        }.removeSuffix(System.lineSeparator())

    companion object {
        fun of(input: List<String>, offset: GridCellScreen = GridCellScreen(0, 0), ignoreChar: Char = ' '): Grid =
            input.mapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { columnIndex, c ->
                    if (c == ignoreChar) {
                        null
                    } else {
                        val cell = GridCellScreen(rowIndex + offset.row, columnIndex + offset.column)
                        cell to GridCellContainer(c == '#', cell.copy())
                    }
                }
            }.flatten().toMap().let { Grid(it, offset) }

        fun of(input: String, offset: GridCellScreen = GridCellScreen(0, 0), ignoreChar: Char = ' '): Grid =
            of(input.lines(), offset, ignoreChar)
    }
}
