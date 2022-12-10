/**
 * Advent of Code 2022, Day 2: Rock Paper Scissors
 * Problem Description: https://adventofcode.com/2022/day/2
 */

import Resources.resourceAsList

fun main() {
    fun part1(input: List<Pair<String, String>>): Int =
        input.fold(0) { acc, op ->
            acc + score(op.first, op.second)
        }

    fun part2(input: List<Pair<String, String>>): Int =
        input.fold(0) { acc, op ->
            acc + score(op.first, op.second)
        }

    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test").map { it.toPair() }
    val puzzleInput = resourceAsList(fileName = name).map { it.toPair() }

    check(part1(testInput) == 15)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 12156)

    val testInput2 = resourceAsList(fileName = "${name}_test").map { it.toPairByResult() }
    val puzzleInput2 = resourceAsList(fileName = name).map { it.toPairByResult() }

    check(part2(testInput2) == 12)
    println(part2(puzzleInput2))
    check(part2(puzzleInput2) == 10835)
}

fun String.toPair(): Pair<String, String> =
    this.split(" ").let { it[0].toElfShape() to it[1].toElfShape() }

fun String.toPairByResult(): Pair<String, String> =
    this.split(" ").let { it[0].toElfShape() to it[1].toShapeByResult(it[0].toElfShape()) }

fun String.toElfShape(): String = when (this) {
    "X" -> "R"
    "Y" -> "P"
    "Z" -> "S"
    "A" -> "R"
    "B" -> "P"
    "C" -> "S"
    else -> throw IllegalArgumentException("Unknown shape: $this")
}

fun String.toShapeByResult(elf: String): String =
    when (this) {
        "Y" -> elf  // draw
        "X" -> when (elf) {    // lose
            "R" -> "S"
            "P" -> "R"
            "S" -> "P"
            else -> throw IllegalArgumentException("Unknown shape: $elf")
        }

        "Z" -> when (elf) {    // win
            "R" -> "P"
            "P" -> "S"
            "S" -> "R"
            else -> throw IllegalArgumentException("Unknown shape: $elf")
        }

        else -> throw IllegalArgumentException("Unknown result: $this")
    }

fun shapeScore(shape: String) =
    when (shape) {
        "R" -> 1
        "P" -> 2
        "S" -> 3
        else -> 0
    }

fun score(elf: String, player: String): Int {
    val baseScore = shapeScore(player)
    if (elf == player) {
        return 3 + baseScore
    }
    when (player) {
        "R" -> if (elf == "S") return 6 + baseScore
        "P" -> if (elf == "R") return 6 + baseScore
        "S" -> if (elf == "P") return 6 + baseScore
    }
    return 0 + baseScore
}
// A, X Rock       1
// B, Y Paper      2
// C, Z Scissors   3

// X lose
// Y draw
// Z win
