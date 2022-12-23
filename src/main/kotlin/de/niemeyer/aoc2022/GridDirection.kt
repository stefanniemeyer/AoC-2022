package de.niemeyer.aoc2022

sealed class GridDirection {
    abstract val turnLeft: GridDirection
    abstract val turnRight: GridDirection
    abstract val offset: Point2D

    override fun toString(): String =
        when (this) {
            is Up -> "U"
            is Right -> "R"
            is Down -> "D"
            is Left -> "L"
        }

    operator fun invoke(dir: String): GridDirection =
        when (dir.uppercase()) {
            "U", "UP" -> Up
            "R", "RIGHT" -> Right
            "D", "DOWN" -> Down
            "L", "LEFT" -> Left
            else -> throw IllegalArgumentException("No such grid direction $dir")
        }

    object Up : GridDirection() {
        override val turnLeft = Left
        override val turnRight = Right
        override val offset = Point2D(0, -1)
    }

    object Right : GridDirection() {
        override val turnLeft = Up
        override val turnRight = Down
        override val offset = Point2D(1, 0)
    }

    object Down : GridDirection() {
        override val turnLeft = Right
        override val turnRight = Left
        override val offset = Point2D(0, 1)
    }

    object Left : GridDirection() {
        override val turnLeft = Down
        override val turnRight = Up
        override val offset = Point2D(-1, 0)
    }
}

fun Char.toGridDirection(): GridDirection =
    when (this) {
        in listOf('U') -> GridDirection.Up
        in listOf('R') -> GridDirection.Right
        in listOf('D') -> GridDirection.Down
        in listOf('L') -> GridDirection.Left
        else -> throw IllegalArgumentException("No such grid direction $this")
    }

