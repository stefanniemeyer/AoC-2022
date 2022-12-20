/**
 * Advent of Code 2022, Day 20: Grove Positioning System
 * Problem Description: https://adventofcode.com/2022/day/20
 */

package de.niemeyer.aoc2022

import de.niemeyer.aoc2022.Resources.resourceAsListOfInt
import kotlin.math.absoluteValue

fun main() {
    fun parseInput(input: List<Int>): List<LinkedList> {
        val result = mutableListOf<LinkedList>()
        var firstValue = input.first()
        val head = LinkedList(firstValue)
        result.add(head)
        var tail = head
        input.drop(1).forEach { value ->
            tail = tail.addAfter(value)
            result.add(tail)
        }
        head.previous = tail
        tail.next = head
//        head.print()
        return result.toList()
    }

    fun part1(input: List<Int>): Int {
        val encFile = parseInput(input)
        var zero: LinkedList? = null

        encFile.forEach { current ->
            if (current.value == 0) {
                zero = current
            }
            current.move(current.value)
//            current.print()
        }
        require (zero != null)
        val numNumbers = input.size
        val e1000 = zero!![1000 % numNumbers].value
        val e2000 = zero!![2000 % numNumbers].value
        val e3000 = zero!![3000 % numNumbers].value
        return e1000 + e2000 + e3000
    }

    fun part2(input: List<Int>): Int =
        TODO()

    val name = getClassName()
    val testInput = resourceAsListOfInt(fileName = "${name}_test")
    val puzzleInput = resourceAsListOfInt(name)

    check(part1(testInput) == 3)
    val puzzleResultPart1 = part1(puzzleInput)
    println(puzzleResultPart1)
    check(puzzleResultPart1 == 1_591)

//    check(part2(testInput) == 0)
//    val puzzleResultPart2 = part2(puzzleInput) 
//    println(puzzleResultPart2)
//    check(puzzleResultPart2 == 0)
}

data class LinkedList(val value: Int, var previous: LinkedList? = null, var next: LinkedList? = null)

fun LinkedList.addAfter(value: Int): LinkedList {
    val next = this.next
    val newNode = LinkedList(value, this, next)
    this.next = newNode
    next?.previous = newNode
    return newNode
}

fun LinkedList.move(pos: Int) {
    if (pos == 0) return // nothing to do

    val next = this.next
    val previous = this.previous

    // relink old neighbors
    previous?.next = next
    next?.previous = previous

    var target = this
    repeat(pos.absoluteValue) {
        target = if (pos < 0) target.previous!! else target.next!!
    }

    if (pos > 0) {
        this.next = target.next
        this.previous = target

        // relink new neighbors
        target?.next?.previous = this
        target.next = this
    } else {
        this.next = target
        this.previous = target.previous

        // relink new neighbors
        target?.previous?.next = this
        target.previous = this
    }
}

operator fun LinkedList.get(pos: Int): LinkedList {
    var current = this
    repeat(pos.absoluteValue) {
        current = if (pos < 0) current.previous!! else current.next!!
    }
    return current
}

fun LinkedList.print() {
    var current = this
    var count = 10
    do {
        print("${current.value}, ")
        current = current.next!!
    } while (count-- >= 0 && current != this)
    println()
}
