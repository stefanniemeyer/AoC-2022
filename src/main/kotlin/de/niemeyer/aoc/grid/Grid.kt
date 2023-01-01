@file:Suppress("unused")

package de.niemeyer.aoc.grid

const val ORIENT_NORMAL = false
const val ORIENT_FLIPPED = true

enum class Side(val id: Int) {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3),
}

data class TileInstructions(val orient: Boolean = ORIENT_NORMAL, val rotate: Side = Side.TOP)

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

    fun printExisting() {
        val points = gridMap.keys.toList()
        val rows = points.maxOf { it.row }
        val columns = points.maxOf { it.column }

        for (row in 0..rows) {
            for (column in 0..columns) {
                if (gridMap.containsKey(GridCellScreen(row, column))) {
                    print(if (gridMap.getValue(GridCellScreen(row, column)).value) '#' else '.')
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    fun printWithDefault(default: Boolean = false) {
        val points = gridMap.keys.toList()
        val rows = points.maxOf { it.row }
        val columns = points.maxOf { it.column }

        for (row in 0..rows) {
            for (column in 0..columns) {
                print(
                    if (gridMap.getOrDefault(
                            GridCellScreen(row, column),
                            GridCellContainer(default)
                        ).value
                    ) '#' else '.'
                )
            }
            println()
        }
    }

    fun rotate(instructions: TileInstructions): Grid {
        val rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
        val columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
        var newOffset = offset

        val result = mutableMapOf<GridCellScreen, GridCellContainer>()
//        println("rotate: ${instructions.rotate}")
        when (instructions.orient) {
            ORIENT_NORMAL -> {
                when (instructions.rotate) {
                    Side.TOP -> {
                        // nothing to do here
                        result.putAll(gridMap)
                    }

                    Side.RIGHT -> {
                        for (row in rowProg) {
                            for (col in columnProg) {
                                result[GridCellScreen(col, rowMax - row + offset.row)] =
                                    gridMap.getValue(GridCellScreen(row, col))
                            }
                        }
                        result.toMap()
                        newOffset = GridCellScreen(offset.column, offset.row)
                    }

                    Side.BOTTOM -> {
                        for (row in rowProg) {
                            for (col in columnProg) {
                                result[GridCellScreen(rowMax - row + offset.row, columnMax - col + offset.column)] =
                                    gridMap.getValue(
                                        GridCellScreen(row, col)
                                    )
                            }
                        }
                        result.toMap()
                    }

                    Side.LEFT -> {
                        for (row in rowProg) {
                            for (col in columnProg) {
                                result[GridCellScreen(columnMax - col + offset.column, row)] =
                                    gridMap.getValue(GridCellScreen(row, col))
                            }
                        }
                        result.toMap()
                        newOffset = GridCellScreen(offset.column, offset.row)
                    }
                }
            }

//            ORIENT_FLIPPED -> {
//                when (instructions.rotate) {
//                    Side.TOP -> {
//                        rowProg = IntProgression.fromClosedRange(rowMax, rowMin, -1)
//                        columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
//                    }
//
//                    Side.RIGHT -> {
//                        rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
//                        columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
//                    }
//
//                    Side.BOTTOM -> {
//                        rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
//                        columnProg = IntProgression.fromClosedRange(columnMax, columnMin, -1)
//                    }
//
//                    Side.LEFT -> {
//                        rowProg = IntProgression.fromClosedRange(rowMax, rowMin, -1)
//                        columnProg = IntProgression.fromClosedRange(columnMax, columnMin, -1)
//                    }
//                }
//            }

            else -> error("Unknown orientation ${instructions.orient}")
        }

        return Grid(result.toMap(), newOffset)
    }

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

fun Set<GridCellScreen>.printBottomLeft() {
    val rowMin = this.minOf { it.row }
    val rowMax = this.maxOf { it.row }
    val columnMin = this.minOf { it.column }
    val columnMax = this.maxOf { it.column }

    for (row in rowMin..rowMax) {
        for (column in columnMin..columnMax) {
            print(if (contains(GridCellScreen(row, column))) '#' else '.')
        }
        println()
    }
}
