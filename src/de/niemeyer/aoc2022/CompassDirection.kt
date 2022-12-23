package de.niemeyer.aoc2022

sealed class CompassDirection {
    abstract val turnLeft: CompassDirection
    abstract val turnRight: CompassDirection
    abstract val offset: Point2D
    abstract val degree: Int

    override fun toString(): String =
        when (this) {
            is North -> "N"
            is East -> "E"
            is South -> "S"
            is West -> "W"
        }

    operator fun invoke(dir: String): CompassDirection =
        when (dir.uppercase()) {
            "N" -> North
            "E" -> East
            "S" -> South
            "W" -> West
            else -> throw IllegalArgumentException("No such compass direction $dir")
        }

    object North : CompassDirection() {
        override val turnLeft = West
        override val turnRight = East
        override val offset = Point2D(0, 1)
        override val degree = 0

    }

    object East : CompassDirection() {
        override val turnLeft = North
        override val turnRight = South
        override val offset = Point2D(1, 0)
        override val degree = 90
    }

    object South : CompassDirection() {
        override val turnLeft = East
        override val turnRight = West
        override val offset = Point2D(0, -1)
        override val degree = 180
    }

    object West : CompassDirection() {
        override val turnLeft = South
        override val turnRight = North
        override val offset = Point2D(-1, 0)
        override val degree = 270
    }
}

fun Char.toCompassDirection(): CompassDirection =
    when (this) {
        in listOf('N') -> CompassDirection.North
        in listOf('E') -> CompassDirection.East
        in listOf('S') -> CompassDirection.South
        in listOf('W') -> CompassDirection.West
        else -> throw IllegalArgumentException("No such compass direction $this")
    }
