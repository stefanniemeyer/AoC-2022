/**
 * Advent of Code 2022, Day 5: Supply Stacks
 * Problem Description: https://adventofcode.com/2022/day/5
 */

import Resources.resourceAsText

typealias CrateStacks = List<ArrayDeque<Char>>

data class Move(val number: Int, val source: Int, val target: Int)
typealias Moves = List<Move>

val inputMoveLineRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
fun String.toMove(): Move {
    val (number, source, target) = inputMoveLineRegex
        .matchEntire(this)
        ?.destructured
        ?: throw IllegalArgumentException("Incorrect move input line $this")
    return Move(number.toInt(), source.toInt(), target.toInt())
}

fun parseInput(input: String): Pair<CrateStacks, Moves> {
    val (cratesLines, moveLines) = input.split("\n\n")
    val maxStack = cratesLines.trim().last().digitToInt()
    val crateStacks = List<ArrayDeque<Char>>(maxStack) { ArrayDeque() }

    cratesLines.lines().dropLast(1).forEach { line ->
        (0..(maxStack - 1)).forEach { stack ->
            val crate = line.getOrNull(stack * 4 + 1)
            if (crate != ' ' && crate != null) crateStacks[stack].addFirst(crate)
        }
    }
    val moves = moveLines.trim().lines().map { it.toMove() }
    return crateStacks to moves
}

fun main() {
    fun part1(input: String): String {
        val (crateStacks, moves) = parseInput(input)
        moves.forEach { (number, source, target) ->
            repeat(number) {
                crateStacks[target - 1] += crateStacks[source - 1].removeLast()
            }
        }
        return crateStacks.map { it.last() }.joinToString("")
    }

    fun part2(input: String): String {
        val (crateStacks, moves) = parseInput(input)
        moves.forEach { (number, source, target) ->
            val movingCrates = ArrayDeque<Char>()
            repeat(number) {
                movingCrates += crateStacks[source - 1].removeLast()
            }
            crateStacks[target - 1].addAll(movingCrates.reversed())
        }
        return crateStacks.map { it.last() }.joinToString("")
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test")
    val puzzleInput = resourceAsText(name)

    check(part1(testInput) == "CMZ")
    println(part1(puzzleInput))
    check(part1(puzzleInput) == "TDCHVHJTG")

    check(part2(testInput) == "MCD")
    println(part2(puzzleInput))
    check(part2(puzzleInput) == "NGCMPJLHV")
}
