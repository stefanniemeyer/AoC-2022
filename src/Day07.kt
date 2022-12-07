import Resources.resourceAsList

fun main() {
    fun part1(input: List<String>): Long =
        Computer(input).run {
            runUntilTerminate()
            result
        }

    fun part2(input: List<String>): Long =
        TODO()

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsList(fileName = "${name}_test")
    val puzzleInput = resourceAsList(name)

    check(part1(testInput) == 95_437L)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 1_490_523L)

//    check(part2(testInput) == 0)
//    check(part2(puzzleInput) == 0)

//    println(part2(puzzleInput))
}

enum class Command {
    CD,
    LS,
    DIR
}

typealias Folder = List<String>

interface ProgramLine
data class Instruction(val cmd: Command, val arg: String) : ProgramLine
data class FileInfo(val folder: Folder, val name: String, val size: Long) : ProgramLine
data class DirInfo(val folder: Folder, val name: String) : ProgramLine

data class Computer(val pgm: List<String>) {
    enum class ExecutionState {
        HALTED,
        RUNNING
    }

    private val PROMPT = "$ "
    private val DIR_ENTRY = "dir "
    private val ROOT_DIR = "/"
    private val PARENT_DIR = ".."

    private var instructionPointer: Int = 0
    private val files = mutableMapOf<Pair<String, String>, Long>()
    private val cwd = mutableListOf<String>()
    var result = 0L

    fun parseInput(input: String): ProgramLine =
        if (input.startsWith(PROMPT)) {
            input.substringAfter(PROMPT).split(" ").run {
                val instruction = Instruction(Command.valueOf(this.first().uppercase()), this.getOrNull(1) ?: "")
                if (instruction.cmd == Command.CD) {
                    when (instruction.arg) {
                        ROOT_DIR -> cwd.clear()
                        PARENT_DIR -> cwd.removeAt(cwd.size - 1)
                        else -> cwd.add(instruction.arg)
                    }
                }
                instruction
            }
        } else if (input.startsWith(DIR_ENTRY)) {
            input.split(" ").run {
                DirInfo(cwd.toList(), this.last())
            }
        } else if (input[0].isDigit()) {
            input.split(" ").run {
                FileInfo(cwd.toList(), this.last(), this.first().toLong())
            }
        } else throw IllegalArgumentException(input)

    fun runUntilTerminate(): ExecutionState =
        generateSequence { executeStep() }.first { it != ExecutionState.RUNNING }

    private fun executeStep(): ExecutionState =
        when (instructionPointer) {
            !in pgm.indices -> {
                println(files)
                val dirs = files.keys
                    .groupBy(keySelector = { it.first }, valueTransform = { files[it] })
//                dirs.values.forEach {println((it as List<Long>).sum()) }
                val sumDirs = dirs.mapValues { (it.value as List<Long>).sum() }
                println("DIRS")
                println(dirs)
                println("\nSUMDIRS")
                val sumInklSubDirs = sumDirs.mapValues {
                    val dirAndSubs = sumDirs.filter { sub -> sub.key.startsWith(it.key) }
                    val sums = dirAndSubs.values.sum()
                    println("${it.key}  -> ${it.value}")
                    sums
                }
                println("\nSUM INKL SUBDIRS")
                println(sumInklSubDirs)
                val smallDirs = sumInklSubDirs.filter { it.value <= 100_000 }
                println(smallDirs)
                println(smallDirs.values.sum())
                result = smallDirs.values.sum()
                ExecutionState.HALTED
            }

            else -> {
                val inst = parseInput(pgm[instructionPointer])
                if (inst is FileInfo) {
                    val absFilename = "/" + inst.folder.joinToString("/")
                    files[absFilename to inst.name] = inst.size
                }
//                println("${cwd} ${inst}")
                instructionPointer += 1
                ExecutionState.RUNNING
            }
        }
}
