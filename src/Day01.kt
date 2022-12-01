import Resources.resourceAsText

fun main() {
    fun part1(input: String): Int {
        val calories: List<String> = input.split("\n\n")
        return calories.map { it.lines().filter { line -> !line.isEmpty() }.map { cals -> cals.toInt() } }.map { calI -> calI.sum() }.max()
    }

    fun part2(input: String): Int {
        val calories: List<String> = input.split("\n\n")
        return calories.map { it.lines().filter { line -> !line.isEmpty() }.map { cals -> cals.toInt() } }.map { calI -> calI.sum() }.sorted().reversed().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = resourceAsText("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = resourceAsText("Day01")
    println(part1(input))
    println(part2(input))
}
