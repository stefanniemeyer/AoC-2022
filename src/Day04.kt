import Resources.resourceAsList

data class Section(val from: Int, val to: Int)

typealias Assignment = Pair<Section, Section>

fun String.toSection(): Section {
    val (from, to) = this.split("-").map(String::toInt)
    return Section(from, to)
}

infix fun Section.overlaps(other: Section): Boolean =
    this.from <= other.to && other.from <= this.to || other.from <= this.to && this.from <= other.to

infix fun Section.completelyOverlaps(other: Section): Boolean =
    this.from <= other.from && other.to <= this.to || other.from <= this.from && this.to <= other.to

fun parseInput(input: List<String>): List<Assignment> =
    input.map { line ->
        line.split(",")
    }.map { it.first().toSection() to it.last().toSection() }

fun main() {
    fun part1(input: List<Assignment>): Int =
        input.map { (a, b) ->
            a.completelyOverlaps(b)
        }.count { it }

    fun part2(input: List<Assignment>): Int =
        input.map { (a, b) ->
            a.overlaps(b)
        }.count { it }

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = parseInput(resourceAsList(fileName = "${name}_test"))
    val puzzleInput = parseInput(resourceAsList(name))

    check(part1(testInput) == 2)
    check(part1(puzzleInput) == 534)
    println(part1(puzzleInput))

    check(part2(testInput) == 4)
    check(part2(puzzleInput) == 841)

    println(part2(puzzleInput))
}

