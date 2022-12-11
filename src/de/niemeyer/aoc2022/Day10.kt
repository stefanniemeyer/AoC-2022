package de.niemeyer.aoc2022

/**
 * Advent of Code 2022, Day 10: Cathode-Ray Tube
 * Problem Description: https://adventofcode.com/2022/day/10
 */

import de.niemeyer.aoc2022.Resources.resourceAsList

fun main() {
    fun part1(input: Map<Int, Int>): Int =
        (20..input.keys.max() step 40).sumOf { it * input.getValue(it) }

    fun part2(input: Map<Int, Int>): String {
        val rows = 6
        val columns = 40
        val crt = MutableList(rows * columns) { ' ' }
        input.forEach { (cycle, register) ->
            val pixelPos = cycle - 1
            val sprite = (register - 1)..(register + 1)
            crt[pixelPos] = if (pixelPos % columns in sprite) '#' else '.'
        }
        return crt.chunked(columns).joinToString("\n") { it.joinToString("") }
    }

    val name = getClassName()
    val testInput = processInstructions(resourceAsList(fileName = "${name}_test"))
    val puzzleInput = processInstructions(resourceAsList(name))

    check(part1(testInput) == 13_140)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 13_520)
    println()

    val testSolution2 = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    val testResult2 = part2(testInput)
    check(testResult2 == testSolution2)
    println(testSolution2)
    println()
    val puzzleSolution2 = """
        ###...##..###..#..#.###..####..##..###..
        #..#.#..#.#..#.#..#.#..#.#....#..#.#..#.
        #..#.#....#..#.####.###..###..#..#.###..
        ###..#.##.###..#..#.#..#.#....####.#..#.
        #....#..#.#....#..#.#..#.#....#..#.#..#.
        #.....###.#....#..#.###..####.#..#.###..
    """.trimIndent()
    val puzzleResult2 = part2(puzzleInput)
    println(puzzleResult2)
    check(puzzleResult2 == puzzleSolution2) // PGPHBEAB
}

fun processInstructions(pgm: List<String>, regXInit: Int = 1): Map<Int, Int> {
    var regX = regXInit
    var cycle = 1
    val signalStrength = mutableMapOf<Int, Int>(cycle to regX)

    pgm.forEach { line ->
        val op = line.substringBefore(" ")

        when (op) {
            "noop" -> {
                signalStrength.put(cycle++, regX)
            }

            "addx" -> {
                signalStrength.put(cycle++, regX)
                signalStrength.put(cycle++, regX)
                regX += line.substringAfter(" ").toInt()
            }
        }
    }
    return signalStrength
}
