import Resources.resourceAsText

fun main() {
    fun part1(input: List<Int>): Int =
        input.first()

    fun part2(input: List<Int>): Int =
        input.take(3).sum()

    fun readInput(fileName: String): List<Int> =
        resourceAsText(fileName).split("\n\n").map {
            it.trim().lines().sumOf(String::toInt)
        }.sortedDescending()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24_000)
    check(part2(testInput) == 45_000)

    val input = readInput("Day01")
    check(part1(input) == 70_764)
    println(part1(input))
    check(part2(input) == 203_905)
    println(part2(input))
}
