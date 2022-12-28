/**
 * Advent of Code 2022, Day 24: Blizzard Basin
 * Problem Description: https://adventofcode.com/2022/day/27
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    check(Day24(testInput).part1() == 18)
    val puzzleResultPart1 = Day24(puzzleInput).part1()
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 288)

    check(Day24(testInput).part2() == 54)
    val puzzleResultPart2 = Day24(puzzleInput).part2()
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 861)
}

class Day24(val input: List<String>) {
    private val yMax = input.lastIndex
    private val xMax = input.first().lastIndex
    private val start = Point2D(input.first().indexOfFirst { it == '.' }, yMax)
    private val goal = Point2D(input.last().indexOfFirst { it == '.' }, 0)
    private val blizzardLoop = (yMax - 1).lcm(xMax - 1)

    private val steps = mutableMapOf(
        0 to input.flatMapIndexed { y, line ->
            line.withIndex().filter { it.value in Direction.ARROWS }
                .map { (x, c) ->
                    val p = Point2D(x, yMax - y)
                    p to listOf(Blizzard(p, c.toDirection()))
                }
        }.toMap()
    )

    data class Blizzard(val pos: Point2D, val direction: Direction)
    data class State(val step: Int, val position: Point2D)

    fun part1(): Int =
        solve(State(0, start), goal)

    fun part2() = listOf(start, goal, start, goal)
        .windowed(2)
        .fold(0) { step, (from, to) -> solve(State(step, from), to) }

    private fun solve(initialState: State, targetPosition: Point2D): Int {
        val seenStates = mutableMapOf<Point2D, MutableSet<Int>>()
        val possibleStates = ArrayDeque(setOf(initialState))
        while (!possibleStates.isEmpty()) {
            val state = possibleStates.removeFirst()
            if (state.position == targetPosition) {
                return state.step - 1
            }
            if (seenStates.getOrPut(state.position) { mutableSetOf() }.add(state.step % blizzardLoop)) {
                val nextBlizzards = steps.computeIfAbsent(state.step % blizzardLoop) {
                    steps[state.step % blizzardLoop - 1]!!.values.flatMap { blizzards ->
                        blizzards.map { blizzard -> blizzard.next().let { it.pos to it } }
                    }.groupBy({ it.first }, { it.second }).toMap()
                }
                listOf(Direction.Down, Direction.Left, Direction.Up, Direction.Right)
                    .map { dir -> state.position.move(dir) }
                    .filter {
                        (it.x in (1 until xMax) && it.y in (1 until yMax))
                                || it == start || it == goal

                    }
                    .plus(state.position)
                    .filterNot { nextBlizzards.keys.contains(it) }        // test if new position is occupied by blizzard
                    .forEach { nextPosition -> possibleStates.addLast(State(state.step + 1, nextPosition)) }
            }
        }
        error("Solution not found")
    }

    fun Blizzard.next(): Blizzard {
        val nextPos = this.pos.move(direction).let {
            if (it.x == 0) {
                it.copy(x = xMax - 1)
            } else if (it.y == 0) {
                it.copy(y = yMax - 1)
            } else if (it.x == xMax) {
                it.copy(x = 1)
            } else if (it.y == yMax) {
                it.copy(y = 1)
            } else {
                it
            }
        }

        return Blizzard(nextPos, direction)
    }
}
