/**
 * Advent of Code 2022, Day 20: Grove Positioning System
 * Problem Description: https://adventofcode.com/2022/day/20
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsListOfLong
import kotlin.math.absoluteValue

fun main() {
    fun parseInput(input: List<Long>, decryptionKey: Long = 1L): List<LinkedList> {
        val result = mutableListOf<LinkedList>()
        val firstValue = input.first() * decryptionKey
        val head = LinkedList(firstValue)
        result.add(head)
        var tail = head
        input.drop(1).forEach { value ->
            tail = tail.addAfter(value * decryptionKey)
            result.add(tail)
        }
        head.previous = tail
        tail.next = head
        return result.toList()
    }

    fun solve(input: List<Long>, rounds: Int = 1, decryptionKey: Long = 1): Long {
        val encFile = parseInput(input, decryptionKey)
        var zero: LinkedList? = null
        val numNumbers = input.size

        repeat(rounds) {
            encFile.forEach { current ->
                if (current.value == 0L) {
                    zero = current
                }
                current.move((current.value % (numNumbers - 1)).toInt())
            }
        }
        require(zero != null)
        val e1000 = zero!![1000 % numNumbers].value
        val e2000 = zero!![2000 % numNumbers].value
        val e3000 = zero!![3000 % numNumbers].value
        return e1000 + e2000 + e3000
    }

    fun part1(input: List<Long>): Long =
        solve(input)

    fun part2(input: List<Long>): Long =
        solve(input, 10,811_589_153L)

    val name = getClassName()
    val testInput = resourceAsListOfLong(fileName = "${name}_test")
    val puzzleInput = resourceAsListOfLong(name)

    check(part1(testInput) == 3L)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 1_591L)

    check(part2(testInput) == 1_623_178_306L)
    val puzzleResultPart2 = part2(puzzleInput)
    println(puzzleResultPart2)
    check(puzzleResultPart2 == 14_579_387_544_492L)
}

data class LinkedList(val value: Long, var previous: LinkedList? = null, var next: LinkedList? = null)

fun LinkedList.addAfter(value: Long): LinkedList {
    val next = this.next
    val newNode = LinkedList(value, this, next)
    this.next = newNode
    next?.previous = newNode
    return newNode
}

fun LinkedList.move(relativePos: Int) {
    if (relativePos == 0) return // nothing to do

    val next = this.next
    val previous = this.previous

    // relink old neighbors
    previous?.next = next
    next?.previous = previous

    var target = this
    repeat(relativePos.absoluteValue) {
        target = if (relativePos < 0) target.previous!! else target.next!!
    }

    if (relativePos > 0) {
        this.next = target.next
        this.previous = target

        // relink new neighbors
        target.next?.previous = this
        target.next = this
    } else {
        this.next = target
        this.previous = target.previous

        // relink new neighbors
        target.previous?.next = this
        target.previous = this
    }
}

operator fun LinkedList.get(relativePos: Int): LinkedList {
    var current = this
    repeat(relativePos.absoluteValue) {
        current = if (relativePos < 0) current.previous!! else current.next!!
    }
    return current
}

@Suppress("unused")
fun LinkedList.print() {
    var current = this
    var count = 10
    do {
        print("${current.value}, ")
        current = current.next!!
    } while (count-- >= 0 && current != this)
    println()
}
