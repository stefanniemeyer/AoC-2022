package de.niemeyer.aoc2022

sealed class Direction {
    abstract val turnLeft: Direction
    abstract val turnRight: Direction
    abstract val offset: Point2D

    override fun toString(): String =
        when (this) {
            is Up -> "U"
            is Right -> "R"
            is Down -> "D"
            is Left -> "L"
        }

    operator fun invoke(dir: String): Direction =
        when (dir.uppercase()) {
            "U", "UP" -> Up
            "R", "RIGHT" -> Right
            "D", "DOWN" -> Down
            "L", "LEFT" -> Left
            else -> throw IllegalArgumentException("No such direction $dir")
        }

    object Up : Direction() {
        override val turnLeft = Left
        override val turnRight = Right
        override val offset = Point2D(0, 1)
    }

    object Right : Direction() {
        override val turnLeft = Up
        override val turnRight = Down
        override val offset = Point2D(1, 0)
    }

    object Down : Direction() {
        override val turnLeft = Right
        override val turnRight = Left
        override val offset = Point2D(0, -1)
    }

    object Left : Direction() {
        override val turnLeft = Down
        override val turnRight = Up
        override val offset = Point2D(-1, 0)
    }

    companion object {
        val ARROWS = setOf('>', '<', '^', 'v')
    }
}

fun Char.toDirection(): Direction =
    when (this) {
        in listOf('U', '^') -> Direction.Up
        in listOf('R', '>') -> Direction.Right
        in listOf('D', 'v') -> Direction.Down
        in listOf('L', '<') -> Direction.Left
        else -> throw IllegalArgumentException("No such direction $this")
    }
