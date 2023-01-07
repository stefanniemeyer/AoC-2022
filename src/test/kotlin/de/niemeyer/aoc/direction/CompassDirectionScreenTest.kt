package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
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
        assertAll(
            { assertThat(CompassDirectionScreen.North.offset).isEqualTo(Point2D(0, -1)) },
            { assertThat(CompassDirectionScreen.NorthEast.offset).isEqualTo(Point2D(1, -1)) },
            { assertThat(CompassDirectionScreen.East.offset).isEqualTo(Point2D(1, 0)) },
            { assertThat(CompassDirectionScreen.SouthEast.offset).isEqualTo(Point2D(1, 1)) },
            { assertThat(CompassDirectionScreen.South.offset).isEqualTo(Point2D(0, 1)) },
            { assertThat(CompassDirectionScreen.SouthWest.offset).isEqualTo(Point2D(-1, 1)) },
            { assertThat(CompassDirectionScreen.West.offset).isEqualTo(Point2D(-1, 0)) },
            { assertThat(CompassDirectionScreen.NorthWest.offset).isEqualTo(Point2D(-1, -1)) },
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
            assertThat(CompassDirectionScreen.fromDegree(degree).degree).isEqualTo(degree)
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
    @DisplayName("CompassDirectionScreen.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertThat(direction.toString()).isEqualTo(directionString[idx])
        }
    }

    @Test
    @DisplayName("String.toCompassDirectionScreen")
    fun testToCompassDirectionScreen() {
        directionString.forEachIndexed { idx, direction ->
            assertThat(direction.toCompassDirectionScreen()).isEqualTo(directions[idx])
        }
    }
}
