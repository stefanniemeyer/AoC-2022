package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Nested
@DisplayName("CompassDirectionScreen")
class CompassDirectionScreenTest {
    val directions = listOf(
        CompassDirectionScreen.North,
        CompassDirectionScreen.NorthEast,
        CompassDirectionScreen.East,
        CompassDirectionScreen.SouthEast,
        CompassDirectionScreen.South,
        CompassDirectionScreen.SouthWest,
        CompassDirectionScreen.West,
        CompassDirectionScreen.NorthWest
    )
    val directionString = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")

    @Test
    @DisplayName("offset")
    fun getOffset() {
        Assertions.assertAll(
            { Assertions.assertEquals(Point2D(0, -1), CompassDirectionScreen.North.offset) },
            { Assertions.assertEquals(Point2D(1, -1), CompassDirectionScreen.NorthEast.offset) },
            { Assertions.assertEquals(Point2D(1, 0), CompassDirectionScreen.East.offset) },
            { Assertions.assertEquals(Point2D(1, 1), CompassDirectionScreen.SouthEast.offset) },
            { Assertions.assertEquals(Point2D(0, 1), CompassDirectionScreen.South.offset) },
            { Assertions.assertEquals(Point2D(-1, 1), CompassDirectionScreen.SouthWest.offset) },
            { Assertions.assertEquals(Point2D(-1, 0), CompassDirectionScreen.West.offset) },
            { Assertions.assertEquals(Point2D(-1, -1), CompassDirectionScreen.NorthWest.offset) }
        )
    }

    @Test
    @DisplayName("degree")
    fun getDegree() {
        val degrees = List(8) { it * 45 }
        degrees.forEachIndexed { idx, degree ->
            Assertions.assertEquals(degree, directions[idx].degree)
        }
    }

    @Test
    @DisplayName("fromDegree")
    fun testFromDegree() {
        val degrees = List(8) { it * 45 }
        degrees.forEach { degree ->
            Assertions.assertEquals(degree, CompassDirectionScreen.fromDegree(degree).degree)
        }
    }

    @Test
    @DisplayName("turnLeft")
    fun getTurnLeft() {
        (0..3).forEach {
            (0..3).forEach {
                val org = directions[it * 2]
                val next = directions[(directions.size + (it - 1) * 2) % directions.size]
                val left = org.turnLeft
                Assertions.assertEquals(next, left)
            }
        }
    }

    @Test
    @DisplayName("turnRight")
    fun getTurnRight() {
        (0..3).forEach {
            val org = directions[it * 2]
            val next = directions[((it + 1) * 2) % directions.size]
            val right = org.turnRight
            Assertions.assertEquals(next, right)
        }
    }

    @Test
    @DisplayName("turnHalfLeft")
    fun getTurnHalfLeft() {
        (0..7).forEach {
            (0..7).forEach {
                val org = directions[it]
                val next = directions[(directions.size + it - 1) % directions.size]
                val halfLeft = org.turnHalfLeft
                Assertions.assertEquals(next, halfLeft)
            }
        }
    }

    @Test
    @DisplayName("turnHalfRight")
    fun getTurnHalfRight() {
        (0..7).forEach {
            (0..7).forEach {
                val org = directions[it]
                val next = directions[(directions.size + it + 1) % directions.size]
                val halfRight = org.turnHalfRight
                Assertions.assertEquals(next, halfRight)
            }
        }
    }

    @Test
    @DisplayName("CompassDirectionScreen.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            Assertions.assertEquals(direction.toString(), directionString[idx])
        }
    }

    @Test
    @DisplayName("String.toCompassDirectionScreen")
    fun testToCompassDirectionScreen() {
        directionString.forEachIndexed { idx, direction ->
            Assertions.assertEquals(direction.toCompassDirectionScreen(), directions[idx])
        }
    }
}
