@file:Suppress("unused")

package de.niemeyer.aoc2022

const val ORIENT_NORMAL = false
const val ORIENT_FLIPPED = true

enum class Side(val id: Int) {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3),
}

data class TileInstructions(val orient: Boolean = ORIENT_NORMAL, val rotate: Side = Side.TOP)

data class GridCellContainer(val value: Boolean, val original: GridCell = GridCell(0, 0))

class Grid(var gridMap: Map<GridCell, GridCellContainer>) {
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
                if (gridMap.containsKey(GridCell(row, column))) {
                    print(if (gridMap.getValue(GridCell(row, column)).value) '#' else '.')
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

//    fun rotateClockwise(): Grid =
//        gridMap.map { (cell, container) ->
//            GridCell(cell.column, -cell.row) to container
//        }.toMap().let { Grid(it) }

    fun printWithDefault(default: Boolean = false) {
        val points = gridMap.keys.toList()
        val rows = points.maxOf { it.row }
        val columns = points.maxOf { it.column }

        for (row in 0..rows) {
            for (column in 0..columns) {
                print(
                    if (gridMap.getOrDefault(
                            GridCell(row, column),
                            GridCellContainer(default)
                        ).value
                    ) '#' else '.'
                )
            }
            println()
        }
    }

    fun rotate(instructions: TileInstructions) {
        val rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
        val columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)

        val result = mutableMapOf<GridCell, GridCellContainer>()
        println("rotate: ${instructions.rotate}")
        when (instructions.orient) {
            ORIENT_NORMAL -> {
                when (instructions.rotate) {
                    Side.TOP -> {
                        // nothing to do here
                        printExisting()
                        return
                    }

                    Side.RIGHT -> {
                        for (row in rowProg) {
                            for (col in columnProg) {
                                result[GridCell(col, rowMax - row + 1)] = gridMap.getValue(GridCell(row, col))
                            }
                        }
                        result.toMap().let { Grid(it).printExisting() }
                    }

                    Side.BOTTOM -> {
                        for (row in rowProg) {
                            for (col in columnProg) {
                                result[GridCell(rowMax - row + 1, columnMax - col + 1)] = gridMap.getValue(GridCell(row, col))
                            }
                        }
                        result.toMap().let { Grid(it).printExisting() }
                    }

                    Side.LEFT -> {
                        for (row in rowProg) {
                            for (col in columnProg) {
                                result[GridCell(columnMax - col + 1, row)] = gridMap.getValue(GridCell(row, col))
                            }
                        }
                        result.toMap().let { Grid(it).printExisting() }
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

        gridMap = result.toMap()
    }

    companion object {
        fun of(input: List<String>, offset: GridCell = GridCell(0, 0), ignoreChar: Char = ' '): Grid =
            input.mapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { columnIndex, c ->
                    if (c == ignoreChar) {
                        null
                    } else {
                        val cell = GridCell(rowIndex + offset.row, columnIndex + offset.column)
                        cell to GridCellContainer(c == '#', cell.copy())
                    }
                }
            }.flatten().toMap().let { Grid(it) }

        fun of(input: String): Grid =
            of(input.lines())
    }
}

fun Set<GridCell>.printBottomLeft() {
    val rowMin = this.minOf { it.row }
    val rowMax = this.maxOf { it.row }
    val columnMin = this.minOf { it.column }
    val columnMax = this.maxOf { it.column }

    for (row in rowMin..rowMax) {
        for (column in columnMin..columnMax) {
            print(if (contains(GridCell(row, column))) '#' else '.')
        }
        println()
    }
}
