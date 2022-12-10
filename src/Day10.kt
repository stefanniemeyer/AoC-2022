import Resources.resourceAsList

fun main() {
    fun part1(input: MutableMap<Int, Int>): Int {
        val interessting = input.filter { (it.key % 20 == 0) && (it.key % 40 != 0) }
        return interessting.map { it.key * it.value }.sum()
    }

    fun part2(input: MutableMap<Int, Int>): Int {
        val ROWS = 6
        val COLUMNS = 40
        val crt = MutableList(ROWS * COLUMNS) { ' ' }
        input.forEach { (cycle, register) ->
            val pixelPos = cycle - 1
            val sprite = (register - 1) .. (register + 1)
            crt[pixelPos] = if (pixelPos % COLUMNS in sprite) '#' else '.'
        }
        crt.chunked(COLUMNS).forEach { println(it.joinToString("")) }
        return 0
    }

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = processInstructions(resourceAsList(fileName = "${name}_test"))
    val puzzleInput = processInstructions(resourceAsList(name))

    check(part1(testInput) == 13_140)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 13_520)

    check(part2(testInput) == 0)
    println("\n")
    check(part2(puzzleInput) == 0) // PGPHBEAB
}

fun processInstructions(pgm: List<String>, regXInit: Int = 1): MutableMap<Int, Int> {
    var regX = regXInit
    var cycle = 1
    val signalStrength = mutableMapOf<Int, Int>(cycle to regX)

    pgm.forEach { line ->
        val op = line.substringBefore(" ")

        when (op) {
            "noop" -> {
                signalStrength.put(cycle++, regX)
            }

            "addx" -> {
                signalStrength.put(cycle++, regX)
                signalStrength.put(cycle++, regX)
                regX += line.substringAfter(" ").toInt()
            }
        }
    }
    return signalStrength
}
