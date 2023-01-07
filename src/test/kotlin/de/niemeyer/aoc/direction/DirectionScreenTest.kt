package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@Nested
@DisplayName("DirectionScreen")
class DirectionScreenTest {
    val directions = listOf(
        DirectionScreen.Up,
        DirectionScreen.Right,
        DirectionScreen.Down,
        DirectionScreen.Left
    )

    val directionString = listOf("Up", "Right", "Down", "Left")
    @Test
    @DisplayName("offset")
    fun getOffset() {
        assertAll(
            { assertThat(DirectionScreen.Up.offset).isEqualTo(Point2D(0, -1)) },
            { assertThat(DirectionScreen.Right.offset).isEqualTo(Point2D(1, 0)) },
            { assertThat(DirectionScreen.Down.offset).isEqualTo(Point2D(0, 1)) },
            { assertThat(DirectionScreen.Left.offset).isEqualTo(Point2D(-1, 0)) },
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
    @DisplayName("DirectionScreen.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertThat(direction.toString()).isEqualTo(directionString[idx].first().toString())
        }
    }

    @Test
    @DisplayName("String.toDirectionScreen")
    fun testToDirectionScreen() {
        directionString.forEachIndexed { idx, direction ->
            assertThat(direction.toDirectionScreen()).isEqualTo(directions[idx])
        }
    }

    @Test
    @DisplayName("Char.arrowToDirectionScreen")
    fun charArrowToDirectionScreen() {
        listOf('^', '>', 'v', '<').forEachIndexed { idx, direction ->
            assertThat(direction.arrowToDirectionScreen()).isEqualTo(directions[idx])
        }
        DirectionScreen.ARROWS.forEachIndexed { idx, direction ->
            assertThat(direction.arrowToDirectionScreen()).isEqualTo(directions[idx])
        }
    }

    @Test
    @DisplayName("String.arrowToDirectionScreen")
    fun stringArrowToDirectionScreen() {
        listOf("^", ">", "v", "<").forEachIndexed { idx, direction ->
            assertThat(direction.arrowToDirectionScreen()).isEqualTo(directions[idx])
        }
    }
}
