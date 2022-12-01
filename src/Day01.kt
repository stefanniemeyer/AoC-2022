import Resources.resourceAsText

fun main() {
    fun part1(input: List<String>): Int =
        input.map {
            it.lines()
                .filter { line -> !line.isEmpty() }
                .map { calS -> calS.toInt() }
        }.map { calI -> calI.sum() }
            .max()


    fun part2(input: List<String>): Int =
        input.map {
            it.lines()
                .filter { line -> !line.isEmpty() }
                .map { calS -> calS.toInt() }
        }.map { calI -> calI.sum() }
            .sorted()
            .reversed()
            .take(3)
            .sum()


    // test if implementation meets criteria from the description, like:
    val testInput = resourceAsText("Day01_test").split("\n\n")
    check(part1(testInput) == 24_000)
    check(part2(testInput) == 45_000)

    val input = resourceAsText("Day01").split("\n\n")
    check(part1(input) == 70_764)
    println(part1(input))
    check(part2(input) == 203_905)
    println(part2(input))
}
