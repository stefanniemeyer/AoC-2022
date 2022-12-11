package de.niemeyer.aoc2022

/**
 * Advent of Code 2022, Day 9: Rope Bridge
 * Problem Description: https://adventofcode.com/2022/day/9
 */

import de.niemeyer.aoc2022.Resources.resourceAsList
import kotlin.math.sign

fun main() {
    fun part1(input: List<Movement>): Int {
        var head = Point2D.ORIGIN
        var tail = Point2D.ORIGIN
        val visitedTail = mutableSetOf<Point2D>(tail)
        input.forEach { instr ->
            repeat(instr.distance) {
                head = head.move(instr.direction)
                tail = neededTailPos(head, tail)
                visitedTail += tail
            }
        }
        return visitedTail.size
    }

    fun part2(input: List<Movement>): Int {
        val MAX_KNOTS = 9
        val HEAD = 0
        val knots = MutableList(MAX_KNOTS + 1) { Point2D.ORIGIN }
        val visitedLastKnot = mutableSetOf(knots[MAX_KNOTS])
        input.forEach { instr ->
            repeat(instr.distance) {
                knots[HEAD] = knots[HEAD].move(instr.direction)
                for (i in 1..MAX_KNOTS) {
                    knots[i] = neededTailPos(knots[i - 1], knots[i])
                }
                visitedLastKnot += knots[MAX_KNOTS]
            }
        }

        return visitedLastKnot.size
    }

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test").map { it.toMovement() }
    val puzzleInput = resourceAsList(name).map { it.toMovement() }

    check(part1(testInput) == 13)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 5_902)

    val testInput2 = resourceAsList(fileName = "${name}_test2").map { it.toMovement() }
    check(part2(testInput2) == 36)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 2_445)
}

data class Movement(val direction: Direction, val distance: Int)

fun String.toMovement(): Movement {
    val direction = this[0].toDirection()
    val distance = this.substringAfter(" ").toInt()
    return Movement(direction, distance)
}

fun neededTailPos(head: Point2D, tail: Point2D): Point2D =
    when {
        tail.chebyshevDistanceTo(head) <= 1 -> tail
        else -> tail + Point2D((head.x - tail.x).sign, (head.y - tail.y).sign)

    }
