/**
 * Advent of Code 2022, Day 13: Distress Signal
 * Problem Description: https://adventofcode.com/2022/day/13
 */

package de.niemeyer.aoc2022

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import de.niemeyer.aoc2022.Resources.resourceAsText

@Serializable
class Packet(val data: JsonArray) : Comparable<Packet> {
    override fun compareTo(other: Packet): Int {
        val compResults = data.zip(other.data).map {
            val left = it.first
            val right = it.second

            if (left is JsonPrimitive && right is JsonPrimitive) {
                left.int - right.int
            } else if (left is JsonArray && right is JsonArray) {
                Packet(left).compareTo(Packet(right))
            } else {
                if (left is JsonPrimitive && right is JsonArray) {
                    return@map Packet(JsonArray(listOf(left))).compareTo(Packet(right))
                } else if (left is JsonArray && right is JsonPrimitive) {
                    return@map Packet(left).compareTo(Packet(JsonArray(listOf(right))))
                } else {
                    error("Invalid input")
                }
            }
        }
        return compResults.firstOrNull { it != 0 } ?: (data.size - other.data.size)
    }
}

fun main() {
    fun part1(input: String): Int {
        val res = input.split("\n\n").map { packets ->
            packets.split("\n")
                .map { it.toPacket() }
        }.mapIndexedNotNull { index, packets ->
            if (packets.first() <= packets.last()) {
                index + 1
            } else {
                null
            }
        }

        return res.sum()
    }

    fun part2(input: String): Int {
        val newInput = input.lines().filter { it.isNotBlank() }.map { it.trim().toPacket() }
        val dividerPacket1 = "[[2]]".toPacket()
        val dividerPacket2 = "[[6]]".toPacket()
        val ordered = (newInput + dividerPacket1 + dividerPacket2).sorted()
        return (ordered.indexOf(dividerPacket1) + 1) * (ordered.indexOf(dividerPacket2) + 1)
    }

    val name = getClassName()
    val testInput = resourceAsText(fileName = "${name}_test").trim()
    val puzzleInput = resourceAsText(name).trim()

    check(part1(testInput) == 13)
    val puzzleResult = part1(puzzleInput)
    println(puzzleResult)
    check(puzzleResult == 5_905)

    check(part2(testInput) == 140)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 21_691)
}

fun String.toPacket() =
    Json.decodeFromString<Packet>("{ \"data\" : ${this} }")
