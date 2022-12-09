import Resources.resourceAsList
import kotlin.math.sign

fun main() {
    fun part1(input: List<Movement>): Int {
        var head = Point2D.ORIGIN
        var tail = Point2D.ORIGIN
        var visitedHead = mutableSetOf<Point2D>(head)
        var visitedTail = mutableSetOf<Point2D>(tail)
        input.forEach { instr ->
            repeat(instr.distance) {
                head = head.move(instr.direction)
                tail = neededTailPos(head, tail)
//                println(head)
                visitedHead += head
                visitedTail += tail
            }
        }
//        println(visitedHead)
        return visitedTail.size
    }

//    fun part2(input: List<Movement>): Int =
//        TODO()

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsList(fileName = "${name}_test").map { it.toMovement() }
    val puzzleInput = resourceAsList(name).map { it.toMovement() }

    check(part1(testInput) == 13)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 5_902)

//    check(part2(testInput) == 0)
//    println(part2(puzzleInput))
//    check(part2(puzzleInput) == 0)
}

data class Movement(val direction: Direction, val distance: Int)
fun String.toMovement(): Movement {
    val direction = this[0].toDirection()
    val distance = this.substringAfter(" ").toInt()
    return Movement(direction, distance)
}

fun neededTailPos(head: Point2D, tail:Point2D): Point2D =
    when {
        head == tail -> tail
        tail in head.neighbors -> tail
        else -> tail + Point2D(sign(head.x - tail.x), sign(head.y - tail.y))

    }

fun sign(i: Int) = when {
    i > 0 -> 1
    i < 0 -> -1
    else -> 0
}
