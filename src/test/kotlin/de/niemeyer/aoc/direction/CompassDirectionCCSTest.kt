package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
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
            { assertThat(CompassDirectionCCS.North.offset).isEqualTo(Point2D(0, 1)) },
            { assertThat(CompassDirectionCCS.NorthEast.offset).isEqualTo(Point2D(1, 1)) },
            { assertThat(CompassDirectionCCS.East.offset).isEqualTo(Point2D(1, 0)) },
            { assertThat(CompassDirectionCCS.SouthEast.offset).isEqualTo(Point2D(1, -1)) },
            { assertThat(CompassDirectionCCS.South.offset).isEqualTo(Point2D(0, -1)) },
            { assertThat(CompassDirectionCCS.SouthWest.offset).isEqualTo(Point2D(-1, -1)) },
            { assertThat(CompassDirectionCCS.West.offset).isEqualTo(Point2D(-1, 0)) },
            { assertThat(CompassDirectionCCS.NorthWest.offset).isEqualTo(Point2D(-1, 1)) },
        )
    }

    @Test
    @DisplayName("degree")
    fun getDegree() {
        val degrees = List(8) { it * 45 }
        degrees.forEachIndexed { idx, degree ->
            assertThat(directions[idx].degree).isEqualTo(degree)
        }
    }

    @Test
    @DisplayName("fromDegree")
    fun testFromDegree() {
        val degrees = List(8) { it * 45 }
        degrees.forEach { degree ->
            assertThat(CompassDirectionCCS.fromDegree(degree).degree).isEqualTo(degree)
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
                assertThat(left).isEqualTo(next)
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
            assertThat(right).isEqualTo(next)
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
                assertThat(halfLeft).isEqualTo(next)

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
                assertThat(halfRight).isEqualTo(next)

            }
        }
    }

    @Test
    @DisplayName("CompassDirectionCCS.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertThat(direction.toString()).isEqualTo(directionString[idx])
        }
    }

    @Test
    @DisplayName("String.toCompassDirectionCCS")
    fun testToCompassDirectionCCS() {
        directionString.forEachIndexed { idx, direction ->
            assertThat(direction.toCompassDirectionCCS()).isEqualTo(directions[idx])
        }
    }
}
