/**
 * Advent of Code 2022, Day 22: Monkey Map
 * Problem Description: https://adventofcode.com/2022/day/22
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsText
import kotlin.math.absoluteValue

typealias MonkeyInstructions = List<Pair<Int, Char?>>

fun main() {
    fun part1(input: String): Int {
        val mmp = MonkeyMapPuzzle(input)
        var currentPos = GridCell(1, mmp.grid.columnRangesForRows.getValue(1).first)
        var currentDirection: GridDirection = GridDirection.Right
        run instructionLoop@{
            mmp.instructions.forEach { (steps, direction) ->
                run repeatBlock@{
                    repeat(steps) {
                        val testPos = mmp.cycleMove(currentPos, currentDirection)
                        if (mmp.isWall(testPos)) {
                            return@repeatBlock
                        }
                        currentPos = testPos
                    }
                }
                currentDirection = when (direction?.toDirection()) {
                    Direction.Left -> currentDirection.turnLeft
                    Direction.Right -> currentDirection.turnRight
                    else -> return@instructionLoop
                }
            }
        }

        return 1000 * currentPos.row + 4 * currentPos.column + currentDirection.facingToNumber()
    }

    fun part2(input: String): Int {
        val mmp = MonkeyMapPuzzle(input)
        var currentPos = GridCell(mmp.grid.columnRangesForRows.getValue(1).first, 1)
        var currentDirection: Direction = Direction.Right

        mmp.rearrangeCube(mmp.testCornerCells)

        return -1
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test")
    val puzzleInput = resourceAsText(name)

//    check(part1(testInput) == 6_032)
//    val puzzleResultPart1 = part1(puzzleInput)
//    println(puzzleResultPart1)
//    check(puzzleResultPart1 == 65_368)

    check(part2(testInput) == 5_031)
//    val puzzleResultPart2 = part2(puzzleInput) 
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)
}

class MonkeyMapPuzzle(val input: String) {
    var grid: Grid
    var instructions: MonkeyInstructions = emptyList()

    init {
        val parts = input.split("\n\n")
        val gridInput = parts.first().lines().filter { it.isNotBlank() }
        grid  = Grid.of(gridInput, offset = GridCell(1, 1))
        instructions = parseInstructions(parts.last())
    }

    fun parseInstructions(input: String): MonkeyInstructions {
        var newInput = input.replace("R", " R ").trim()
        newInput = newInput.replace("L", " L ")
        val instructions = newInput.split(" ").chunked(size = 2).map { moves ->
            val value = moves.first().toInt()
            val direction = when (moves.size) {
                2 -> moves.last().first()
                else -> null
            }
            value to direction
        }
        return instructions
    }

    fun cycleMove(cell: GridCell, gridDirection: GridDirection): GridCell {
        val newCell = cell.move(gridDirection)

        val rowLimits = grid.rowRangesForColumns.getValue(cell.column)
        val colLimits = grid.columnRangesForRows.getValue(cell.row)

        val newRow = when {
            newCell.row > rowLimits.last -> rowLimits.first
            newCell.row < rowLimits.first -> rowLimits.last
            else -> newCell.row
        }
        val newCol = when {
            newCell.column > colLimits.last -> colLimits.first
            newCell.column < colLimits.first -> colLimits.last
            else -> newCell.column
        }

        return GridCell(newRow, newCol)
    }

    fun isWall(p: GridCell): Boolean =
        grid.gridMap.getValue(p).value

    val testCornerCells = listOf(
        GridCell(5, 1),
        GridCell(5, 5),
        GridCell(5, 9),
        GridCell(9, 13),
        GridCell(1, 9),
        GridCell(9, 9)
    )

    fun rearrangeCube(cornerCells: List<GridCell>) {
        val edgeLength = (cornerCells.first().column - cornerCells.first().row).absoluteValue
        println("Edge length: $edgeLength")

        val result = cornerCells.map { corner ->
            (0 until edgeLength).flatMap { row ->
                (0 until edgeLength).map { col ->
                    val p = GridCell(corner.column + col, corner.row + row)
                    GridCell(col, row) to grid.gridMap.getValue(p)
                }
            }.toMap()
        }
        println(result)
    }
}

fun GridDirection.facingToNumber() = when (this) {
    GridDirection.Right -> 0
    GridDirection.Up -> 1
    GridDirection.Down -> 2
    GridDirection.Left -> 3
}
