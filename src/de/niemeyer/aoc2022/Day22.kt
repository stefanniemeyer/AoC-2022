/**
 * Advent of Code 2022, Day 22: Monkey Map
 * Problem Description: https://adventofcode.com/2022/day/22
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsText

typealias MonkeyMap = Map<Point2D, Boolean>
typealias MonkeyInstructions = List<Pair<Int, Char?>>

fun main() {
    fun Direction.facingToNumber() = when (this) {
        Direction.East -> 0
        Direction.North -> 1
        Direction.South -> 2
        Direction.West -> 3
    }

    fun part1(input: String): Int {
        val mmp = MonkeyMapPuzzle(input)
        var currentPos = Point2D(mmp.colLimitsForRows.getValue(1).first, 1)
        var currentDirection: Direction = Direction.East
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
                    Direction.West -> currentDirection.turnRight  // L North means row+1 in the Direction class, so we switch this here
                    Direction.East -> currentDirection.turnLeft   // R
                    else -> return@instructionLoop
                }
            }
        }

        return 1000 * currentPos.y + 4 * currentPos.x + currentDirection.facingToNumber()
    }

    fun part2(input: String): Int =
        TODO()

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test")
    val puzzleInput = resourceAsText(name)

    check(part1(testInput) == 6_032)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 65_368)

//    check(part2(testInput) == 0)
//    val puzzleResultPart2 = part2(puzzleInput) 
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)
}

class MonkeyMapPuzzle(val input: String) {
    var map: MonkeyMap = emptyMap()
    var instructions: MonkeyInstructions = emptyList()
    var colLimitsForRows: Map<Int, IntRange> = emptyMap()
    var rowLimitsForCols: Map<Int, IntRange> = emptyMap()

    init {
        val parsedInput = parse(input)
        map = parsedInput.first
        instructions = parsedInput.second
        colLimitsForRows = getColRangesForRows(map)
        rowLimitsForCols = getRowRangesForCols(map)
    }

    fun parseMap(input: List<String>): MonkeyMap =
        input.mapIndexed { row, line ->
            line.mapIndexedNotNull { column, c ->
                if (c == '.' || c == '#') {
                    Point2D(column + 1, row + 1) to (c == '#')
                } else {
                    null
                }
            }
        }.flatten().toMap()

    fun parseInstructions(input: String): MonkeyInstructions {
        var newInput = input.replace("R", " R ")
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

    fun parse(input: String): Pair<MonkeyMap, MonkeyInstructions> {
        val parts = input.split("\n\n")
        val mapInput = parts.first().lines().filter { it.isNotBlank() }
        val monkeyMap = parseMap(mapInput)
        val instructions = parts.last().trim()
        val parsedInstructions = parseInstructions(instructions)
//        monkeyMap.printExisting()
        return monkeyMap to parsedInstructions
    }

    fun getColRangesForRows(monkeyMap: MonkeyMap): Map<Int, IntRange> {
        val rowRange = monkeyMap.keys.minOf { it.y }..monkeyMap.keys.maxOf { it.y }
        return rowRange.associateWith { row ->
            monkeyMap.keys.filter { it.y == row }.minOf { it.x }..monkeyMap.keys.filter { it.y == row }.maxOf { it.x }
        }
    }

    fun getRowRangesForCols(monkeyMap: MonkeyMap): Map<Int, IntRange> {
        val colRange = monkeyMap.keys.minOf { it.x }..monkeyMap.keys.maxOf { it.x }
        return colRange.associateWith { col ->
            monkeyMap.keys.filter { it.x == col }.minOf { it.y }..monkeyMap.keys.filter { it.x == col }.maxOf { it.y }
        }
    }

    fun cycleMove(p: Point2D, d: Direction): Point2D {
        val newPoint = p.move(d)

        val rowLimits = rowLimitsForCols.getValue(p.x)
        val colLimits = colLimitsForRows.getValue(p.y)

        val newRow = when {
            newPoint.y > rowLimits.last -> rowLimits.first
            newPoint.y < rowLimits.first -> rowLimits.last
            else -> newPoint.y
        }
        val newCol = when {
            newPoint.x > colLimits.last -> colLimits.first
            newPoint.x < colLimits.first -> colLimits.last
            else -> newPoint.x
        }

        return Point2D(newCol, newRow)
    }

    fun isWall(p: Point2D): Boolean =
        map.getValue(p) == true
}
