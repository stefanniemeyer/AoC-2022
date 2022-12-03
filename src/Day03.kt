import Resources.resourceAsList

fun main() {
    fun part1(input: List<String>): Int =
        input.map { rucksack ->
            val compartmentItems = rucksack.chunked(rucksack.length / 2).map { it.toCharArray().toSet() }
            val inter = compartmentItems[0].intersect(compartmentItems[1])
            inter.first().priority()
        }.sum()

    fun part2(input: List<String>): Int =
        input.windowed(3, 3).map { rucksacks ->
            val rucksackItems = rucksacks.map { it.toCharArray().toSet() }
            val inter = rucksackItems.reduce { acc, set -> acc.intersect(set) }
            inter.first().priority()
        }.sum()

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")
    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    check(part1(testInput) == 157)
    check(part1(puzzleInput) == 7_817)
    println(part1(puzzleInput))

    check(part2(testInput) == 70)
    check(part2(puzzleInput) == 2_444)
    println(part2(puzzleInput))
}

fun Char.priority(): Int = when (this) {
    in 'a'..'z' -> this - 'a' + 1
    else -> this - 'A' + 27
}
