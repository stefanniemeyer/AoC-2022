@file:Suppress("unused")

package de.niemeyer.aoc.utils

// println("abcdefghij".iterator().next(5))
// abcde
fun Iterator<Char>.next(size: Int): String =
    (1..size).map { next() }.joinToString("")

// println("10011111".iterator().nextInt(4))
// 9     # 1001
fun Iterator<Char>.nextInt(size: Int): Int =
    next(size).toInt(2)

// println("abcdefghij".iterator().nextUntilFirst(3) { it == "def" })
// [abc, def]
fun Iterator<Char>.nextUntilFirst(size: Int, stopCondition: (String) -> Boolean): List<String> {
    val output = mutableListOf<String>()
    do {
        val readValue = next(size)
        output.add(readValue)
    } while (!stopCondition(readValue))
    return output
}

// println("abcdef".iterator().executeUntilEmpty { it.next() < 'c' })
// [true, true, false, false, false, false]
fun <T> Iterator<Char>.executeUntilEmpty(function: (Iterator<Char>) -> T): List<T> {
    val output = mutableListOf<T>()
    while (this.hasNext()) {
        output.add(function(this))
    }
    return output
}

// println("abcdefghij".asIterable().takeUntil { it == 'c' })
// [a, b, c]
inline fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (predicate(item))
            break
    }
    return list
}

// listOf(1, 2, 3).pairs().forEach { println(it) }
// (1, 1)
// (1, 2)
// (1, 3)
// (2, 2)
// (2, 3)
// (3, 3)
fun <T> Collection<T>.pairs(): List<Pair<T, T>> =
    this.flatMapIndexed { index, a ->
        this.drop(index).map { b -> a to b }
    }

fun Iterable<Int>.product(): Int =
    reduce(Int::times)

fun Iterable<Long>.product(): Long =
    reduce(Long::times)

fun Char.asLong(): Long =
    digitToInt().toLong()

// val a = arrayOf((
//         "abcde").toCharArray(),
//     "12345".toCharArray(),
//     "ABCDE".toCharArray()
// )// println(a.peer(1, 1, 2, 2))
// (0, 3)
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
