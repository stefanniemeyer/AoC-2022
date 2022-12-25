@file:Suppress("unused")

package de.niemeyer.aoc2022

fun Iterator<Char>.next(size: Int): String =
    (1..size).map { next() }.joinToString("")

fun Iterator<Char>.nextInt(size: Int): Int =
    next(size).toInt(2)

fun Iterator<Char>.nextUntilFirst(size: Int, stopCondition: (String) -> Boolean): List<String> {
    val output = mutableListOf<String>()
    do {
        val readValue = next(size)
        output.add(readValue)
    } while (!stopCondition(readValue))
    return output
}

fun <T> Iterator<Char>.executeUntilEmpty(function: (Iterator<Char>) -> T): List<T> {
    val output = mutableListOf<T>()
    while (this.hasNext()) {
        output.add(function(this))
    }
    return output
}

inline fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (predicate(item))
            break
    }
    return list
}

fun <T> Collection<T>.pairs(): List<Pair<T, T>> =
    this.flatMapIndexed { index, a ->
        this.drop(index).map { b -> a to b }
    }

fun Array<CharArray>.printBottomLeft() {
    for (row in this.indices) {
        for (col in this.first().indices) {
            print(this[row][col])
        }
        println()
    }
}

fun Iterable<Int>.product(): Int =
    reduce(Int::times)

fun Iterable<Long>.product(): Long =
    reduce(Long::times)

fun Char.asLong(): Long =
    digitToInt().toLong()

fun Array<CharArray>.peer(posRow: Int, posCol: Int, offsetRow: Int, offsetCol: Int): Pair<Int, Int> =
    Pair((posRow + offsetRow) % size, (posCol + offsetCol) % first().size)

infix fun IntRange.intersects(other: IntRange): Boolean =
    first <= other.last && last >= other.first

infix fun IntRange.intersect(other: IntRange): IntRange =
    maxOf(first, other.first)..minOf(last, other.last)

infix fun IntRange.union(other: IntRange): IntRange =
    minOf(first, other.first)..maxOf(last, other.last)

fun IntRange.size(): Int =
    last - first + 1
