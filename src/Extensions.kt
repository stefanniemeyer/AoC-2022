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

fun <T> Collection<T>.pairs(): List<Pair<T, T>> =
    this.flatMapIndexed { index, a ->
        this.drop(index).map { b -> a to b }
    }

fun Array<CharArray>.print() {
    for (row in this.indices) {
        for (col in this.first().indices) {
            print(this[row][col])
        }
        println()
    }
}

fun Array<CharArray>.peer(posRow: Int, posCol: Int, offsetRow: Int, offsetCol: Int): Pair<Int, Int> =
    Pair((posRow + offsetRow) % this.size, (posCol + offsetCol) % this.first().size)

infix fun IntRange.intersects(other: IntRange): Boolean =
    first <= other.last && last >= other.first

infix fun IntRange.intersect(other: IntRange): IntRange =
    maxOf(first, other.first)..minOf(last, other.last)

fun IntRange.size(): Int =
    last - first + 1
