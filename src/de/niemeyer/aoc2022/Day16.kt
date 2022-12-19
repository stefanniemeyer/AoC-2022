/**
 * Advent of Code 2022, Day 16: Proboscidea Volcanium
 * Problem Description: https://adventofcode.com/2022/day/16
 *
 * The searchPaths functions comes from Todd Ginsberg https://todd.ginsberg.com/post/advent-of-code/2022/day16
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsList
import com.github.shiguruikai.combinatoricskt.combinations

fun main() {
    var shortestPathCosts = mutableMapOf<String, MutableMap<String, Int>>()
    var cages: Map<String, Valve> = mapOf()

    fun parseInput(input: List<String>): Map<String, Valve> {
        return input.map { Valve.of(it) }.associateBy { it.name }
    }

    fun searchPaths(
        from: String,
        maxTime: Int,
        visited: Set<String> = emptySet(),
        usedTime: Int = 0,
        totalFlow: Int = 0
    ): Int =
        shortestPathCosts
            .getValue(from)
            .asSequence()
            .filterNot { (nextValve, _) -> nextValve in visited }
            .filter { (_, traversalCost) -> usedTime + traversalCost + 1 < maxTime }
            .maxOfOrNull { (nextValve, traversalCost) ->
                searchPaths(
                    nextValve,
                    maxTime,
                    visited + nextValve,
                    usedTime + traversalCost + 1,
                    totalFlow + ((maxTime - usedTime - traversalCost - 1) * cages.getValue(nextValve).flowRate)
                )
            } ?: totalFlow

    fun part1(input: Map<String, Valve>): Int {
        cages = input

        return searchPaths("AA", 30)
    }

    fun part2(input: Map<String, Valve>): Int {
        cages = input
        return shortestPathCosts.keys.filter { it != "AA" }
            .combinations(shortestPathCosts.size / 2)
            .map { it.toSet() }
            .maxOf { halfOfTheRooms ->
                searchPaths("AA", 26, halfOfTheRooms) +
                        searchPaths("AA", 26, shortestPathCosts.keys - halfOfTheRooms)
            }
    }

    val name = getClassName()
    val testInput = parseInput(resourceAsList(fileName = "${name}_test"))
    val puzzleInput = parseInput(resourceAsList(name))

    shortestPathCosts = findShortestsPaths(testInput)
    check(part1(testInput) == 1_651)

    shortestPathCosts = findShortestsPaths(puzzleInput)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 1_906)

    shortestPathCosts = findShortestsPaths(testInput)
    check(part2(testInput) == 1_707)

    shortestPathCosts = findShortestsPaths(puzzleInput)
    val puzzleResultPart2 = part2(puzzleInput)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 2_548)
}

class Valve(val name: String, val flowRate: Int, val neighbors: List<String>, val vertex: Vertex) {
    companion object {
        fun of(input: String): Valve {
            val (nameS, flowRateS, neighborList) = inputValveLineRegex
                .matchEntire(input)
                ?.destructured
                ?: error("Incorrect valve input line $input")
            val neighborsL = neighborList.split(", ")
            return Valve(nameS, flowRateS.toInt(), neighborsL, Vertex(nameS))
        }
    }
}

fun Map<String, Valve>.toGraph(): Map<Vertex, List<Edge>> =
    map {
        it.value.vertex to
                it.value.neighbors.map { neighbor ->
                    Edge(this[neighbor]!!.vertex, 1)   // it takes 1 minute to move from one valve to another
                }
    }.toMap()

fun findShortestsPaths(input: Map<String, Valve>): MutableMap<String, MutableMap<String, Int>> {
    val graph = input.toGraph()
    val paths = mutableMapOf<String, MutableMap<String, Int>>()
    val start = input["AA"]

    val relevantValves = input.values.filter { it.flowRate > 0 || it == start }.map { it.name }
    relevantValves.forEach { from ->
        val fromValve = input.getValue(from)
        paths[from] = mutableMapOf()
        relevantValves.forEach { to ->
            if (from != to) {
                val distances = dijkstra(graph, fromValve.vertex)
                distances.forEach { dist ->
                    if (dist.key.name != fromValve.name && dist.key.name in relevantValves) {
                        paths[fromValve.name, dist.key.name] = dist.value
                    }
                }
            }
        }
    }

    return paths
}

val inputValveLineRegex = """Valve (\S+) has flow rate=(\d+); tunnel[s]? lead[s]? to valve[s]? (.+)""".toRegex()

private operator fun Map<String, MutableMap<String, Int>>.set(key1: String, key2: String, value: Int) {
    getValue(key1)[key2] = value
}

private operator fun Map<String, Map<String, Int>>.get(
    key1: String,
    key2: String,
    defaultValue: Int = Int.MAX_VALUE
): Int =
    get(key1)?.get(key2) ?: defaultValue
