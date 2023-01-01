/**
 * Advent of Code 2022, Day 17: Pyroclastic Flow
 * Problem Description: https://adventofcode.com/2022/day/19
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.utils.Resources.resourceAsText
import de.niemeyer.aoc.utils.getClassName

// chamber is 7 units wide
// rock appears so that its left edge is two units away from the left wall
// and the bottom is 3 units above the highest rock (or the floor)
//
// first move by jet (if possible),
// then fall downward (if possible)
// if falling is not possible, the rocks lands and the next rocks appears

fun main() {
    data class CacheKey(val skyline: List<Int>, val jetIndex: Int)
    data class CacheValue(val highestRock: Int, val cycle: Long)

    fun solve(input: String, maxCycles: Long): Long {
        val cache = mutableMapOf<CacheKey, CacheValue>()
        val chamber = Chamber()
        var cycle = 1L
        val initialPad = 3
        var jetIdx = 0
        val moves = jetMoves(input)
        var simulatedHeight = 0L
        rocksSequence().forEach rockForEach@{ rock ->
            val highestRock = chamber.highestRock()
            if (((cycle - 1) % rocks.size) == 0L) {
                val cacheKey = CacheKey(chamber.skyline(), jetIdx)
                val cacheValue = CacheValue(highestRock, cycle)
                if (cache.containsKey(cacheKey) && simulatedHeight == 0L) {
                    val storedCachedValue = cache.getValue(cacheKey)
                    val cycleLength = (cycle - storedCachedValue.cycle)
                    val simCyles = (maxCycles - cycle) / cycleLength
                    val restCyles = (maxCycles - cycle) % cycleLength
                    val gainingHeightPerCycle = highestRock - storedCachedValue.highestRock
                    simulatedHeight = simCyles * gainingHeightPerCycle
                    cycle = maxCycles - restCyles
                    log("cycle $cycle: cache hit for $cacheKey: $storedCachedValue now: $cacheValue")
                    log("simulating $simCyles cycles rest $restCyles cyclesLength $cycleLength gaining height $gainingHeightPerCycle")
                    log("simulated height $simulatedHeight\n")
                } else {
                    cache[cacheKey] = cacheValue
                }
            }
            val enlarge = maxOf(0, highestRock + initialPad + rock.stones.size - chamber.levels.size + 1)
            chamber.enlarge(enlarge)
            var movedRock = rock
            var level = highestRock + initialPad + 1

            var isLanded = false
            while (isLanded == false) {
                // try jet move
                val direction = moves[jetIdx]
                if (chamber.isMovable(movedRock, direction, level)) {
                    movedRock = movedRock.move(direction)
                }
                jetIdx = (jetIdx + 1) % moves.size
                if (chamber.isMovable(movedRock, RockDirection.DOWN, level)) {
                    level--
                } else {
                    // rock landed
                    movedRock.stones.mapIndexed { idx, stone ->
                        val testLevel = level + idx
                        chamber.levels[testLevel] = chamber.levels[testLevel] xor stone
                    }
//                    log("end of cycle: $cycle -> ${chamber.highestRock()}")
//                    chamber.print()
                    cycle++
                    isLanded = true
                }
            }
            if (cycle > maxCycles) {
                val res = chamber.highestRock() + simulatedHeight
                return res + 1
            }
        }
        return -1
    }

    fun part1(input: String): Long =
        solve(input, 2_022L)

    fun part2(input: String): Long =
        solve(input, 1_000_000_000_000L)

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test.txt").trim()
    val puzzleInput = resourceAsText(fileName = "${name}.txt").trim()

    check(part1(testInput) == 3_068L)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 3_173L)

    check(part2(testInput) == 1_514_285_714_288L)
    val puzzleResultPart2 = part2(puzzleInput)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 1_570_930_232_582L)

}

enum class RockDirection {
    LEFT, RIGHT, DOWN
}

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

fun rocksSequence() = sequence {
    while (true) {
        yieldAll(rocks.map { Rock(it) })
    }
}

@Suppress("UNUSED_PARAMETER")
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

    fun skyline(): List<Int> {
        val highestRock = highestRock()
        if (highestRock == -1) {
            return listOf()
        }
        val maxDepth = -highestRock - 1
        val result = MutableList<Int>(7) { maxDepth }
        for (column in 0..6) {
            val pattern = 0b010000000 shr column
            log("pattern: ${pattern.asBinaryString()}")
            rowTesting@ for (row in highestRock downTo 0) {
                val testLevel = levels[row]
                log("row: $row: ${testLevel.asBinaryString()}")
                if ((testLevel and pattern) != 0) {
                    if (result[column] == maxDepth) {
                        result[column] = row - highestRock
                    }
                }
            }
        }
        return result.toList()
    }

    @Suppress("unused")
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
