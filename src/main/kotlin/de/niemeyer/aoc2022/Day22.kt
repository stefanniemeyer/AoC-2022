/**
 * Advent of Code 2022, Day 22: Monkey Map
 * Problem Description: https://adventofcode.com/2022/day/22
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.direction.DirectionCCS
import de.niemeyer.aoc.direction.DirectionScreen
import de.niemeyer.aoc.direction.toDirectionCCS
import de.niemeyer.aoc.grid.Grid
import de.niemeyer.aoc.grid.GridCellScreen
import de.niemeyer.aoc.utils.Resources.resourceAsText
import de.niemeyer.aoc.utils.getClassName
import kotlin.math.absoluteValue

typealias MonkeyInstructions = List<Pair<Int, Char?>>

fun main() {
    fun part1(input: String): Int {
        val mmp = MonkeyMapPuzzle(input)
        var currentPos = GridCellScreen(1, mmp.grid.columnRangesForRows.getValue(1).first)
        var currentDirection: DirectionScreen = DirectionScreen.Right
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
                currentDirection = when (direction?.toDirectionCCS()) {
                    DirectionCCS.Left -> currentDirection.turnLeft
                    DirectionCCS.Right -> currentDirection.turnRight
                    else -> return@instructionLoop
                }
            }
        }

        return 1000 * currentPos.row + 4 * currentPos.column + currentDirection.facingToNumber()
    }

    fun part2(input: String): Int {
        val mmp = MonkeyMapPuzzle(input)
        var currentPos = GridCellScreen(mmp.grid.columnRangesForRows.getValue(1).first, 1)
        var currentDirectionCCS: DirectionCCS = DirectionCCS.Right

        mmp.rearrangeCube(mmp.testCornerCells)

        return -1
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsText(fileName = "${name}.txt")

    check(part1(testInput) == 6_032)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 65_368)

//    check(part2(testInput) == 5_031)
//    val puzzleResultPart2 = part2(puzzleInput)
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)
}

class MonkeyMapPuzzle(val input: String) {
    var grid = Grid(mapOf())
    var instructions: MonkeyInstructions = emptyList()

    init {
        val parts = input.split("\n\n")
        val gridInput = parts.first().lines().filter { it.isNotBlank() }
        grid  = Grid.of(gridInput, offset = GridCellScreen(1, 1))
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

    fun cycleMove(cell: GridCellScreen, directionScreen: DirectionScreen): GridCellScreen {
        val newCell = cell.move(directionScreen)

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

        return GridCellScreen(newRow, newCol)
    }

    fun isWall(p: GridCellScreen): Boolean =
        grid.gridMap.getValue(p).value

    val testCornerCells = listOf(
        GridCellScreen(5, 1),
        GridCellScreen(5, 5),
        GridCellScreen(5, 9),
        GridCellScreen(9, 13),
        GridCellScreen(1, 9),
        GridCellScreen(9, 9)
    )

    fun rearrangeCube(cornerCells: List<GridCellScreen>) {
        val edgeLength = (cornerCells.first().column - cornerCells.first().row).absoluteValue
        println("Edge length: $edgeLength")

        val result = cornerCells.map { corner ->
            (0 until edgeLength).flatMap { row ->
                (0 until edgeLength).map { col ->
                    val p = GridCellScreen(corner.column + col, corner.row + row)
                    GridCellScreen(col, row) to grid.gridMap.getValue(p)
                }
            }.toMap()
        }
        println(result)
    }
}

fun DirectionScreen.facingToNumber() = when (this) {
    DirectionScreen.Right -> 0
    DirectionScreen.Up -> 1
    DirectionScreen.Down -> 2
    DirectionScreen.Left -> 3
}
