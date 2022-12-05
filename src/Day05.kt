import Resources.resourceAsText

typealias CrateStacks = List<ArrayDeque<Char>>

data class Move(val number: Int, val source: Int, val target: Int)
typealias Moves = List<Move>

val inputMoveLineRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
fun String.toMove(): Move {
    val (number, source, target) = inputMoveLineRegex
        .matchEntire(this)
        ?.destructured
        ?: throw IllegalArgumentException("Incorrect move input line $this")
    return Move(number.toInt(), source.toInt(), target.toInt())
}

fun parseInput(input: String): Pair<CrateStacks, Moves> {
    val (cratesLines, moveLines) = input.split("\n\n")
    val maxStack = cratesLines.trim().last().digitToInt()
    val crateStacks: CrateStacks = buildList {
        (1..maxStack).forEach {
            add(ArrayDeque())
        }
    }

    cratesLines.lines().dropLast(1).forEach { line ->
        (0..(maxStack - 1)).forEach { stack ->
            val crate = line.getOrNull(stack * 4 + 1)
            if (crate != ' ' && crate != null) crateStacks[stack].addFirst(crate)
        }
    }
    val moves = moveLines.trim().lines().map { it.toMove() }
    return crateStacks to moves
}

fun main() {
    fun part1(input: String): String {
        val (crateStacks, moves) = parseInput(input)
        moves.forEach { (number, source, target) ->
            (1..number).forEach {
                crateStacks[target - 1] += crateStacks[source - 1].removeLast()
            }
        }
        return crateStacks.map { it.last() }.joinToString("")
    }

    fun part2(input: String): String {
        val (crateStacks, moves) = parseInput(input)
        moves.forEach { (number, source, target) ->
            val movingCrates = ArrayDeque<Char>()
            (1..number).forEach {
                movingCrates += crateStacks[source - 1].removeLast()
            }
            crateStacks[target - 1].addAll(movingCrates.reversed())
        }
        return crateStacks.map { it.last() }.joinToString("")
    }

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsText(fileName = "${name}_test")
    val puzzleInput = resourceAsText(name)

    check(part1(testInput) == "CMZ")
    check(part1(puzzleInput) == "TDCHVHJTG")
    println(part1(puzzleInput))

    check(part2(testInput) == "MCD")
    check(part2(puzzleInput) == "NGCMPJLHV")

    println(part2(puzzleInput))
}
