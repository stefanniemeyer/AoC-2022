import Resources.resourceAsText

fun main() {
    fun detectDistinctChars(input: String, distChars: Int): Int =
        (0 .. input.length - distChars).firstOrNull { pos ->
            input.substring(pos, pos + distChars).toSet().size == distChars
        }?.let { it + distChars } ?: -1

    fun part1(input: String): Int =
        detectDistinctChars(input, 4)

    fun part2(input: String): Int =
        detectDistinctChars(input, 14)

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsText(fileName = "${name}_test")
    val puzzleInput = resourceAsText(name)

    check(part1(testInput) == 7)
    check(part1(puzzleInput) == 1_965)
    println(part1(puzzleInput))

    check(part2(testInput) == 19)
    check(part2(puzzleInput) == 2_773)

    println(part2(puzzleInput))
}
