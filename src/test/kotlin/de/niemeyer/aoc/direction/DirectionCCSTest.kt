package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Nested
@DisplayName("DirectionCCS (Cartesian Coordinate System)")
class DirectionCCSTest {
    val directions = listOf(
        DirectionCCS.Up,
        DirectionCCS.Right,
        DirectionCCS.Down,
        DirectionCCS.Left
    )

    val directionString = listOf("Up", "Right", "Down", "Left")
    @Test
    @DisplayName("offset")
    fun getOffset() {
        assertAll(
            { assertThat(DirectionCCS.Up.offset).isEqualTo(Point2D(0, 1)) },
            { assertThat(DirectionCCS.Right.offset).isEqualTo(Point2D(1, 0)) },
            { assertThat(DirectionCCS.Down.offset).isEqualTo(Point2D(0, -1)) },
            { assertThat(DirectionCCS.Left.offset).isEqualTo(Point2D(-1, 0)) },
        )
    }

    @Test
    @DisplayName("turnLeft")
    fun getTurnLeft() {
        (0..3).forEach {
            (0..3).forEach {
                val org = directions[it]
                val next = directions[(directions.size + it - 1) % directions.size]
                val left = org.turnLeft
                assertThat(left).isEqualTo(next)
            }
        }
    }

    @Test
    @DisplayName("turnRight")
    fun getTurnRight() {
        (0..3).forEach {
            val org = directions[it]
            val next = directions[(it + 1) % directions.size]
            val right = org.turnRight
            assertThat(right).isEqualTo(next)
        }
    }

    @Test
    @DisplayName("DirectionCCS.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertThat(direction.toString()).isEqualTo(directionString[idx].first().toString())
        }
    }

    @Test
    @DisplayName("String.toDirectionCCS")
    fun testToDirectionCCS() {
        directionString.forEachIndexed { idx, direction ->
            assertThat(direction.toDirectionCCS()).isEqualTo(directions[idx])
        }
    }

    @Test
    @DisplayName("Char.arrowToDirectionCCS")
    fun charArrowToDirectionCCS() {
        listOf('^', '>', 'v', '<').forEachIndexed { idx, direction ->
            assertThat(direction.arrowToDirectionCCS()).isEqualTo(directions[idx])
        }
        DirectionCCS.ARROWS.forEachIndexed { idx, direction ->
            assertThat(direction.arrowToDirectionCCS()).isEqualTo(directions[idx])
        }
    }

    @Test
    @DisplayName("String.arrowToDirectionCCS")
    fun stringArrowToDirectionCCS() {
        listOf("^", ">", "v", "<").forEachIndexed { idx, direction ->
            assertThat(direction.arrowToDirectionCCS()).isEqualTo(directions[idx])
        }
    }
}
