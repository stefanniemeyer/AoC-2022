package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Nested
@DisplayName("CompassDirectionCCS (Cartesian Coordinate System)")
class CompassDirectionCCSTest {
    val directions = listOf(
        CompassDirectionCCS.North,
        CompassDirectionCCS.NorthEast,
        CompassDirectionCCS.East,
        CompassDirectionCCS.SouthEast,
        CompassDirectionCCS.South,
        CompassDirectionCCS.SouthWest,
        CompassDirectionCCS.West,
        CompassDirectionCCS.NorthWest
    )
    val directionString = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")

    @Test
    @DisplayName("offset")
    fun getOffset() {
        assertAll(
            { assertEquals(Point2D(0, 1), CompassDirectionCCS.North.offset) },
            { assertEquals(Point2D(1, 1), CompassDirectionCCS.NorthEast.offset) },
            { assertEquals(Point2D(1, 0), CompassDirectionCCS.East.offset) },
            { assertEquals(Point2D(1, -1), CompassDirectionCCS.SouthEast.offset) },
            { assertEquals(Point2D(0, -1), CompassDirectionCCS.South.offset) },
            { assertEquals(Point2D(-1, -1), CompassDirectionCCS.SouthWest.offset) },
            { assertEquals(Point2D(-1, 0), CompassDirectionCCS.West.offset) },
            { assertEquals(Point2D(-1, 1), CompassDirectionCCS.NorthWest.offset) }
        )
    }

    @Test
    @DisplayName("degree")
    fun getDegree() {
        val degrees = List(8) { it * 45 }
        degrees.forEachIndexed { idx, degree ->
            assertEquals(degree, directions[idx].degree)
        }
    }

    @Test
    @DisplayName("fromDegree")
    fun testFromDegree() {
        val degrees = List(8) { it * 45 }
        degrees.forEach { degree ->
            assertEquals(degree, CompassDirectionCCS.fromDegree(degree).degree)
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
                assertEquals(next, left)
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
            assertEquals(next, right)
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
                assertEquals(next, halfLeft)
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
                assertEquals(next, halfRight)
            }
        }
    }

    @Test
    @DisplayName("CompassDirectionCCS.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertEquals(direction.toString(), directionString[idx])
        }
    }

    @Test
    @DisplayName("String.toCompassDirectionCCS")
    fun testToCompassDirectionCCS() {
        directionString.forEachIndexed { idx, direction ->
            assertEquals(direction.toCompassDirectionCCS(), directions[idx])
        }
    }
}
