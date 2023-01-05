/**
 * Advent of Code 2023, Day 19: Not Enough Minerals
 * Problem Description: https://adventofcode.com/2023/day/02
 *
 * This mainly is a reimplementation of forketyfork's solution
 * https://github.com/forketyfork/aoc-2022/blob/main/src/main/kotlin/year2022/Day19.kt
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc.utils.Resources.resourceAsList
import de.niemeyer.aoc.utils.getClassName

val blueprintRegex =
    """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

fun String.toBlueprint(): Blueprint {
    val (idS, oreRobotOreCostS, clayRobotOreCostS, obsidianRobotOreCostS, obsidianRobotClayCostS, geodeRobotOreCostS, geodeRobotObsidianCostS) = blueprintRegex
        .matchEntire(this)
        ?.destructured
        ?: throw IllegalArgumentException("Incorrect blueprint input line $this")
    return Blueprint(
        idS.toInt(),
        oreRobotOreCostS.toInt(),
        clayRobotOreCostS.toInt(),
        obsidianRobotOreCostS.toInt(),
        obsidianRobotClayCostS.toInt(),
        geodeRobotOreCostS.toInt(),
        geodeRobotObsidianCostS.toInt()
    )
}

data class Blueprint(
    val id: Int,
    val oreRobotOreCost: Int,
    val clayRobotOreCost: Int,
    val obsidianRobotOreCost: Int,
    val obsidianRobotClayCost: Int,
    val geodeRobotOreCost: Int,
    val geodeRobotObsidianCost: Int
) {
    val maxOreCost = maxOf(oreRobotOreCost, clayRobotOreCost, obsidianRobotOreCost, geodeRobotOreCost)
}

fun main() {
    val name = getClassName()
    val testInput = resourceAsList(fileName = "${name}_test.txt")
    val puzzleInput = resourceAsList(fileName = "${name}.txt")

    check(Day19(testInput).part1() == 33)
    val puzzleResultPart1 = Day19(puzzleInput).part1()
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 851)

    check(Day19(testInput).part2() == 3_472)
    val puzzleResultPart2 = Day19(puzzleInput).part2()
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 12_160)
}

class Day19(input: List<String>) {
    val blueprints = input.map { it.toBlueprint() }

    fun solve(blueprints: List<Blueprint>, steps: Int) =
        blueprints.map {
            solution(null, State(blueprint = it, steps, numOreRobots = 1))
        }

    fun part1(): Int =
        solve(blueprints, 24)
            .reduceIndexed { idx, acc, value -> acc + (idx + 1) * value }

    fun part2(): Int =
        solve(blueprints.take(3), 32).reduce(Int::times)
}

fun solution(prevState: State?, state: State): Int {
    if (state.remainingSteps == 0) {
        return state.geode
    }

    return buildList {
        if (state.canBuyGeodeRobot()) {
            add(state.buyGeodeRobot())
        } else {
            add(state)
            add(state.couldBuyOreRobot(prevState))
            add(state.couldBuyClayRobot(prevState))
            add(state.couldBuyObsidianRobot(prevState))
        }
    }.filterNotNull()
        .filter { it.isPossible }
        .map { it.nextStep(state) }
        .maxOf { solution(state, it) }
}

data class State(
    val blueprint: Blueprint,
    val remainingSteps: Int,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geode: Int = 0,
    val numOreRobots: Int = 0,
    val numClayRobots: Int = 0,
    val numObsidianRobots: Int = 0,
    val numGeodeRobots: Int = 0
) {
    val isPossible get() = remainingSteps > 0 && ore >= 0 && clay >= 0 && obsidian >= 0 && geode >= 0

    fun canBuyOreRobot() = ore >= blueprint.oreRobotOreCost
    fun canBuyClayRobot() = ore >= blueprint.clayRobotOreCost
    fun canBuyObsidianRobot() =
        ore >= blueprint.obsidianRobotOreCost && clay >= blueprint.obsidianRobotClayCost

    fun canBuyGeodeRobot() =
        ore >= blueprint.geodeRobotOreCost && obsidian >= blueprint.geodeRobotObsidianCost

    fun robotsEquality(otherState: State) =
        numOreRobots == otherState.numOreRobots &&
                numClayRobots == otherState.numClayRobots &&
                numObsidianRobots == otherState.numObsidianRobots &&
                numGeodeRobots == otherState.numGeodeRobots

    fun couldBuyOreRobot(prevState: State?) =
        if (!(prevState?.canBuyOreRobot() == true && robotsEquality(prevState)) && shouldBuyOreRobot) {
            copy(
                ore = ore - blueprint.oreRobotOreCost,
                numOreRobots = numOreRobots + 1
            )
        } else null

    fun couldBuyClayRobot(prevState: State?) =
        if (!(prevState?.canBuyClayRobot() == true && robotsEquality(prevState)) && shouldBuyClayRobot) {
            copy(
                ore = ore - blueprint.clayRobotOreCost,
                numClayRobots = numClayRobots + 1
            )
        } else null

    fun couldBuyObsidianRobot(prevState: State?) =
        if (!(prevState?.canBuyObsidianRobot() == true && robotsEquality(prevState)) && shouldBuyObsidianRobot) {
            copy(
                ore = ore - blueprint.obsidianRobotOreCost,
                clay = clay - blueprint.obsidianRobotClayCost,
                numObsidianRobots = numObsidianRobots + 1
            )
        } else null

    fun buyGeodeRobot() = copy(
        ore = ore - blueprint.geodeRobotOreCost,
        obsidian = obsidian - blueprint.geodeRobotObsidianCost,
        numGeodeRobots = numGeodeRobots + 1
    )

    val shouldBuyOreRobot get() = numOreRobots < blueprint.maxOreCost
    val shouldBuyClayRobot get() = numClayRobots < blueprint.obsidianRobotClayCost
    val shouldBuyObsidianRobot get() = numObsidianRobots < blueprint.geodeRobotObsidianCost

    fun nextStep(prevState: State) = copy(
        remainingSteps = remainingSteps - 1,
        ore = ore + prevState.numOreRobots,
        clay = clay + prevState.numClayRobots,
        obsidian = obsidian + prevState.numObsidianRobots,
        geode = geode + prevState.numGeodeRobots
    )
}
