/**
 * Advent of Code 2022, Day 17: Pyroclastic Flow
 * Problem Description: https://adventofcode.com/2022/day/19
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsText

// chamber is 7 units wide
// rock appears so that its left edge is two units away from the left wall
// and the bottom is 3 units above the highest rock (or the floor)
//
// first move by jet (if possible),
// then fall downward (if possible)
// if falling is not possible, the rocks lands and the next rocks appears

fun main() {

    fun part1(input: String, maxCycles: Long = 2_022L): Long {
        val chamber = Chamber()
        var cycle = 1L
        val initialPad = 3
        var move = 0
        val moves = jetMoves(input)
        rocksSequence().forEach rockForEach@{ rock ->
            val highestRock = chamber.highestRock()
            val enlarge = maxOf(0, highestRock + initialPad + rock.stones.size - chamber.levels.size + 1)
            if (enlarge < 0) {
                println("hallo")
            }
            chamber.enlarge(enlarge)
            var movedRock = rock
            var level = highestRock + initialPad + 1

            var isLanded = false
            while (isLanded == false) {
                // try jet move
                val direction = moves[move % moves.size]
                if (chamber.isMovable(movedRock, direction, level)) {
                    movedRock = movedRock.move(direction)
                }
                move++
                if (chamber.isMovable(movedRock, RockDirection.DOWN, level)) {
                    level--
                } else {
                    // rock landed
                    movedRock.stones.mapIndexed { idx, stone ->
                        val testLevel = level + idx
                        chamber.levels[testLevel] = chamber.levels[testLevel] xor stone
                    }
//                    println("end of cycle: $cycle -> ${chamber.highestRock()}")
//                    chamber.print()
                    cycle++
                    isLanded = true
                }
            }
            if (cycle > maxCycles) {
                val res = chamber.highestRock()
                return (res + 1).toLong()
            }
        }
        return -1
    }

    fun part2(input: String): Long =
        TODO()

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test").trim()
    val puzzleInput = resourceAsText(name).trim()

    check(part1(testInput) == 3_068L)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 3_173L)

//    check(part2(testInput) == 1_514_285_714_288L)
//    val puzzleResultPart2 = part2(puzzleInput) 
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)

}

enum class RockDirection {
    LEFT, RIGHT, DOWN
}

fun rocksSequence() = sequence {
    val rocks = listOf(
        listOf(
            0b000111100
        ),
        listOf(
            0b000010000,
            0b000111000,
            0b000010000
        ),
        listOf(
            0b000001000,
            0b000001000,
            0b000111000
        ).reversed(),
        listOf(
            0b000100000,
            0b000100000,
            0b000100000,
            0b000100000
        ),
        listOf(
            0b000110000,
            0b000110000
        )
    )

    while (true) {
        yieldAll(rocks.map { Rock(it) })
    }
}

fun log(msg: String) {
    // println(msg)
}

class Chamber(var levels: IntArray = IntArray(0)) {
    val chamberBorder = 0b100000001

    fun enlarge(size: Int) {
        levels += IntArray(size)

        if (size > levels.size) {
            levels = IntArray(size) { 0 }
        }
    }

    fun isMovable(rock: Rock, direction: RockDirection, startLevel: Int): Boolean {
        val movedRock = rock.move(direction)
        val newLevel = if (direction == RockDirection.DOWN) startLevel - 1 else startLevel
        if (newLevel < 0) {
            return false
        }
        val moveResults = movedRock.stones.mapIndexed { idx, stone ->
            val testLevel = newLevel + idx
            val movable = ((stone and levels[testLevel]) == 0) && ((stone and chamberBorder) == 0)
            log("testLevel: $testLevel direction: $direction stone: ${stone.asBinaryString()} level: ${levels[testLevel].asBinaryString()} -> $movable")
            movable
        }

        val retVal = moveResults.all { it == true }
        return retVal
    }

    fun highestRock(): Int =
        levels.indexOfLast { it != 0 }

    fun print() {
        (levels.lastIndex downTo 0).forEach { level ->
            println(levels[level].asBinaryString())
        }
    }
}

fun jetMoves(input: String): List<RockDirection> =
    input.map {
        when (it) {
            '>' -> RockDirection.RIGHT
            '<' -> RockDirection.LEFT
            else -> error("invalid input jet move '$it'")
        }
    }

class Rock(val stones: List<Int>) {
    override fun toString(): String {
        return stones.joinToString("") { it.toString(2).padStart(7, '0') }
    }

    fun move(direction: RockDirection): Rock {
        return when (direction) {
            RockDirection.LEFT -> Rock(stones.map { it shl 1 })
            RockDirection.RIGHT -> Rock(stones.map { it shr 1 })
            RockDirection.DOWN -> this
        }
    }
}

fun Int.asBinaryString() = Integer.toBinaryString(this).padStart(9, '0')
