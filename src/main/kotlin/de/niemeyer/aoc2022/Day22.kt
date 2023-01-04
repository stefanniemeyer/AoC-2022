/**
 * Advent of Code 2022, Day 22: Monkey Map
 * Problem Description: https://adventofcode.com/2022/day/22
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.direction.DirectionCCS
import de.niemeyer.aoc.direction.DirectionScreen
import de.niemeyer.aoc.direction.toDirectionCCS
import de.niemeyer.aoc.direction.toDirectionScreen
import de.niemeyer.aoc.grid.Grid
import de.niemeyer.aoc.grid.GridCellScreen
import de.niemeyer.aoc.grid.TileInstructions
import de.niemeyer.aoc.utils.Resources.resourceAsText
import de.niemeyer.aoc.utils.getClassName

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

    fun part2(input: String, cornerCells: Map<Int, Pair<GridCellScreen, DirectionScreen>>): Int {
        val mmp = MonkeyMapPuzzle(input)
        var currentPos = GridCellScreen(0, 0)
        var currentDirection: DirectionScreen = DirectionScreen.Right
        var orgDirection: DirectionScreen = DirectionScreen.Right
        var currentSide = 5    // works for my input :-)
        val edgeLength = minOf(mmp.grid.rowMax, mmp.grid.columnMax) / 3

        val normalizedSides = mutableMapOf<Int, Grid>()
        cornerCells.forEach { side, cornerInfos ->
            normalizedSides[side] = mmp.rearrangeCube(edgeLength, cornerInfos.first, cornerInfos.second)
        }

        var currentGrid = normalizedSides.getValue(currentSide)
        run instructionLoop@{
            mmp.instructions.forEach { (steps, direction) ->
                run repeatBlock@{
                    repeat(steps) {
                        var testGrid = currentGrid
                        var testSide = currentSide
                        var testPos = currentPos.move(currentDirection)
                        var testDirection = currentDirection
                        if (testPos.row !in 0 until edgeLength || testPos.column !in 0 until edgeLength) {
                            val sideInfos = transformInfos.getValue(currentSide)
                            val sideChange = sideInfos.getValue(currentDirection)
                            testSide = sideChange.newSide
                            testGrid = normalizedSides.getValue(testSide)
                            val posAndDir = sideChange.transFunc(currentPos, edgeLength)
                            testPos = posAndDir.first
                            testDirection = posAndDir.second
                        }

                        if (testGrid.gridMap.getValue(testPos).value) {
                            // is a wall
                            return@repeatBlock
                        }
                        currentGrid = testGrid
                        currentSide = testSide
                        currentPos = testPos
                        currentDirection = testDirection
                    }
                }
                currentDirection = when (direction?.toDirectionScreen()) {
                    DirectionScreen.Left -> {
                        orgDirection = orgDirection.turnLeft
                        currentDirection.turnLeft
                    }

                    DirectionScreen.Right -> {
                        orgDirection = orgDirection.turnRight
                        currentDirection.turnRight
                    }

                    else -> return@instructionLoop
                }
            }
        }
        var faceNum = currentDirection.facingToNumber()
        val cornerCell = cornerCells.getValue(currentSide).second
        if (cornerCell == DirectionScreen.Left) {
            faceNum = (currentDirection.turnRight).facingToNumber()
        } else if (cornerCell == DirectionScreen.Right) {
            faceNum = (currentDirection.turnLeft).facingToNumber()
        }
        val org = currentGrid.gridMap.getValue(currentPos).original
        return 1000 * org.row + 4 * org.column + faceNum
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsText(fileName = "${name}.txt")

    check(part1(testInput) == 6_032)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 65_368)

    check(part2(testInput, testCornerCells) == 5_031)
    val puzzleResultPart2 = part2(puzzleInput, puzzleCornerCells)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 156_166)
}

val testCornerCells = mapOf(
    1 to Pair(GridCellScreen(5, 1), DirectionScreen.Up),
    2 to Pair(GridCellScreen(5, 5), DirectionScreen.Up),
    3 to Pair(GridCellScreen(5, 9), DirectionScreen.Up),
    4 to Pair(GridCellScreen(9, 13), DirectionScreen.Left),
    5 to Pair(GridCellScreen(1, 9), DirectionScreen.Up),
    6 to Pair(GridCellScreen(9, 9), DirectionScreen.Up),
)

/*
 * The input will be 'normalized' in a way that the top left corner of each side of the cube is at (0,0)
 * and the plan for the cube is as follows, e.g. for a 4x4 cube:
 *
 *  +------------------------+
 *  |                        |
 *  |   +------------+       |
 *  |   |            v       |
 *  |   |    +--->5555<---+  |
 *  |   |    |    5555    |  |
 *  |   |    |    5555    |  |
 *  |   |    |    5555    |  |
 *  |   v    v            v  |
 *  +-> 1111 2222 3333 4444<-+
 *      1111 2222 3333 4444
 *      1111 2222 3333 4444
 *      1111 2222 3333 4444
 *      ^    ^            ^
 *      |    |    6666    |
 *      |    |    6666    |
 *      |    |    6666    |
 *      |    +--->6666<---+
 *      |            ^
 *      +------------+
 */
val puzzleCornerCells = mapOf(
    1 to Pair(GridCellScreen(151, 1), DirectionScreen.Right),
    2 to Pair(GridCellScreen(101, 1), DirectionScreen.Right),
    3 to Pair(GridCellScreen(51, 51), DirectionScreen.Up),
    4 to Pair(GridCellScreen(1, 101), DirectionScreen.Right),
    5 to Pair(GridCellScreen(1, 51), DirectionScreen.Up),
    6 to Pair(GridCellScreen(101, 51), DirectionScreen.Up),
)

fun left2right(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(cell.row, edgeLength - 1) to DirectionScreen.Left

fun right2left(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(cell.row, 0) to DirectionScreen.Right

fun up2down(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(edgeLength - 1, cell.column) to DirectionScreen.Up

fun down2up(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(0, cell.column) to DirectionScreen.Down

fun up2up(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(0, edgeLength - 1 - cell.column) to DirectionScreen.Down

fun up2left(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(cell.column, 0) to DirectionScreen.Right

fun left2up(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(0, cell.row) to DirectionScreen.Down

fun up2right(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(edgeLength - 1 - cell.column, edgeLength - 1) to DirectionScreen.Left

fun right2up(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(0, edgeLength - 1 - cell.row) to DirectionScreen.Down

fun down2down(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(edgeLength - 1, edgeLength - 1 - cell.column) to DirectionScreen.Up

fun down2left(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(edgeLength - 1 - cell.column, 0) to DirectionScreen.Right

fun left2down(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(edgeLength - 1, edgeLength - 1 - cell.row) to DirectionScreen.Up

fun down2right(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(cell.column, edgeLength - 1) to DirectionScreen.Left

fun right2down(cell: GridCellScreen, edgeLength: Int): Pair<GridCellScreen, DirectionScreen> =
    GridCellScreen(edgeLength - 1, cell.row) to DirectionScreen.Up

data class SideChange(val newSide: Int, val transFunc: (GridCellScreen, Int) -> Pair<GridCellScreen, DirectionScreen>)

val transformInfos = mapOf(
    1 to mapOf(
        DirectionScreen.Up to SideChange(5, ::up2up),
        DirectionScreen.Right to SideChange(2, ::right2left),
        DirectionScreen.Down to SideChange(6, ::down2down),
        DirectionScreen.Left to SideChange(4, ::left2right),
    ),
    2 to mapOf(
        DirectionScreen.Up to SideChange(5, ::up2left),
        DirectionScreen.Right to SideChange(3, ::right2left),
        DirectionScreen.Down to SideChange(6, ::down2left),
        DirectionScreen.Left to SideChange(1, ::left2right),
    ),
    3 to mapOf(
        DirectionScreen.Up to SideChange(5, ::up2down),
        DirectionScreen.Right to SideChange(4, ::right2left),
        DirectionScreen.Down to SideChange(6, ::down2up),
        DirectionScreen.Left to SideChange(2, ::left2right),
    ),
    4 to mapOf(
        DirectionScreen.Up to SideChange(5, ::up2right),
        DirectionScreen.Right to SideChange(1, ::right2left),
        DirectionScreen.Down to SideChange(6, ::down2right),
        DirectionScreen.Left to SideChange(3, ::left2right),
    ),
    5 to mapOf(
        DirectionScreen.Up to SideChange(1, ::up2up),
        DirectionScreen.Right to SideChange(4, ::right2up),
        DirectionScreen.Down to SideChange(3, ::down2up),
        DirectionScreen.Left to SideChange(2, ::left2up),
    ),
    6 to mapOf(
        DirectionScreen.Up to SideChange(3, ::up2down),
        DirectionScreen.Right to SideChange(4, ::right2down),
        DirectionScreen.Down to SideChange(1, ::down2down),
        DirectionScreen.Left to SideChange(2, ::left2down),
    ),
)

class MonkeyMapPuzzle(val input: String) {
    var grid = Grid(mapOf())
    var instructions: MonkeyInstructions = emptyList()

    init {
        val parts = input.split("\n\n")
        val gridInput = parts.first().lines().filter { it.isNotBlank() }
        grid = Grid.of(gridInput, offset = GridCellScreen(1, 1))
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

    fun rearrangeCube(edgeLength: Int, corner: GridCellScreen, direction: DirectionScreen): Grid {
        val newCells = (0 until edgeLength).flatMap { row ->
            (0 until edgeLength).map { column ->
                val p = GridCellScreen(corner.row + row, corner.column + column)
                GridCellScreen(row, column) to grid.gridMap.getValue(p)
            }
        }.toMap()

        return Grid(newCells).rotate(TileInstructions(direction))
    }
}

fun DirectionScreen.facingToNumber() = when (this) {
    DirectionScreen.Right -> 0
    DirectionScreen.Down -> 1
    DirectionScreen.Left -> 2
    DirectionScreen.Up -> 3
}
