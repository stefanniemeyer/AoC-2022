@file:Suppress("unused")

package de.niemeyer.aoc2022

sealed class CompassDirection {
    abstract val turnLeft: CompassDirection
    abstract val turnRight: CompassDirection
    abstract val offset: Point2D
    abstract val degree: Int

    override fun toString(): String =
        when (this) {
            is North -> "N"
            is NorthEast -> "NE"
            is East -> "E"
            is SouthEast -> "SE"
            is South -> "S"
            is SouthWest -> "SW"
            is West -> "W"
            is NorthWest -> "NW"
        }

    operator fun invoke(dir: String): CompassDirection =
        when (dir.uppercase()) {
            "N" -> North
            "NE" -> NorthEast
            "E" -> East
            "SE" -> SouthEast
            "S" -> South
            "SW" -> SouthWest
            "W" -> West
            "NW" -> NorthWest
            else -> throw IllegalArgumentException("No such compass direction $dir")
        }

    object North : CompassDirection() {
        override val turnLeft = West
        override val turnRight = East
        override val offset = Point2D(0, 1)
        override val degree = 0
    }

    object NorthEast : CompassDirection() {
        override val turnLeft = NorthWest
        override val turnRight = SouthEast
        override val offset = Point2D(1, 1)
        override val degree = 45
    }

    object East : CompassDirection() {
        override val turnLeft = North
        override val turnRight = South
        override val offset = Point2D(1, 0)
        override val degree = 90
    }

    object SouthEast : CompassDirection() {
        override val turnLeft = NorthEast
        override val turnRight = SouthWest
        override val offset = Point2D(1, -1)
        override val degree = 135
    }

    object South : CompassDirection() {
        override val turnLeft = East
        override val turnRight = West
        override val offset = Point2D(0, -1)
        override val degree = 180
    }

    object SouthWest : CompassDirection() {
        override val turnLeft = SouthEast
        override val turnRight = NorthWest
        override val offset = Point2D(-1, -1)
        override val degree = 225
    }

    object West : CompassDirection() {
        override val turnLeft = South
        override val turnRight = North
        override val offset = Point2D(-1, 0)
        override val degree = 270
    }

    object NorthWest : CompassDirection() {
        override val turnLeft = SouthWest
        override val turnRight = NorthEast
        override val offset = Point2D(-1, 1)
        override val degree = 315
    }

    companion object {
        fun fromDegree(degree: Int): CompassDirection =
            when (Math.floorMod(360, degree)) {
                0 -> North
                45 -> NorthEast
                90 -> East
                135 -> SouthEast
                180 -> South
                225 -> SouthWest
                270 -> West
                315 -> NorthWest
                else -> throw IllegalArgumentException("No such compass direction $degree")
            }
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

fun String.toCompassDirection(): CompassDirection =
    when (uppercase()) {
        "N" -> CompassDirection.North
        "NE" -> CompassDirection.NorthEast
        "E" -> CompassDirection.East
        "SE" -> CompassDirection.SouthEast
        "S" -> CompassDirection.South
        "SW" -> CompassDirection.SouthWest
        "W" -> CompassDirection.West
        "NW" -> CompassDirection.NorthWest
        else -> throw IllegalArgumentException("No such compass direction $this")
    }

