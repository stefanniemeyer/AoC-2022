package de.niemeyer.aoc2022

sealed class CompassDirection {
    abstract val turnLeft: de.niemeyer.aoc2022.CompassDirection
    abstract val turnRight: de.niemeyer.aoc2022.CompassDirection
    abstract val offset: de.niemeyer.aoc2022.Point2D
    abstract val degree: Int

    override fun toString(): String =
        when (this) {
            is de.niemeyer.aoc2022.CompassDirection.North -> "N"
            is de.niemeyer.aoc2022.CompassDirection.East -> "E"
            is de.niemeyer.aoc2022.CompassDirection.South -> "S"
            is de.niemeyer.aoc2022.CompassDirection.West -> "W"
        }

    operator fun invoke(dir: String): de.niemeyer.aoc2022.CompassDirection =
        when (dir.uppercase()) {
            "N" -> de.niemeyer.aoc2022.CompassDirection.North
            "E" -> de.niemeyer.aoc2022.CompassDirection.East
            "S" -> de.niemeyer.aoc2022.CompassDirection.South
            "W" -> de.niemeyer.aoc2022.CompassDirection.West
            else -> throw IllegalArgumentException("No such compass direction $dir")
        }

    object North : de.niemeyer.aoc2022.CompassDirection() {
        override val turnLeft = de.niemeyer.aoc2022.CompassDirection.West
        override val turnRight = de.niemeyer.aoc2022.CompassDirection.East
        override val offset = de.niemeyer.aoc2022.Point2D(0, 1)
        override val degree = 0

    }

    object East : de.niemeyer.aoc2022.CompassDirection() {
        override val turnLeft = de.niemeyer.aoc2022.CompassDirection.North
        override val turnRight = de.niemeyer.aoc2022.CompassDirection.South
        override val offset = de.niemeyer.aoc2022.Point2D(1, 0)
        override val degree = 90
    }

    object South : de.niemeyer.aoc2022.CompassDirection() {
        override val turnLeft = de.niemeyer.aoc2022.CompassDirection.East
        override val turnRight = de.niemeyer.aoc2022.CompassDirection.West
        override val offset = de.niemeyer.aoc2022.Point2D(0, -1)
        override val degree = 180
    }

    object West : de.niemeyer.aoc2022.CompassDirection() {
        override val turnLeft = de.niemeyer.aoc2022.CompassDirection.South
        override val turnRight = de.niemeyer.aoc2022.CompassDirection.North
        override val offset = de.niemeyer.aoc2022.Point2D(-1, 0)
        override val degree = 270
    }
}

fun Char.toCompassDirection(): de.niemeyer.aoc2022.CompassDirection =
    when (this) {
        in listOf('N') -> de.niemeyer.aoc2022.CompassDirection.North
        in listOf('E') -> de.niemeyer.aoc2022.CompassDirection.East
        in listOf('S') -> de.niemeyer.aoc2022.CompassDirection.South
        in listOf('W') -> de.niemeyer.aoc2022.CompassDirection.West
        else -> throw IllegalArgumentException("No such compass direction $this")
    }
