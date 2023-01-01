package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
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
            { assertEquals(Point2D(0, -1), DirectionScreen.Up.offset) },
            { assertEquals(Point2D(1, 0), DirectionScreen.Right.offset) },
            { assertEquals(Point2D(0, 1), DirectionScreen.Down.offset) },
            { assertEquals(Point2D(-1, 0), DirectionScreen.Left.offset) },
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
                assertEquals(next, left)
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
            assertEquals(next, right)
        }
    }

    @Test
    @DisplayName("DirectionScreen.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertEquals(direction.toString(), directionString[idx].first().toString())
        }
    }

    @Test
    @DisplayName("String.toDirectionScreen")
    fun testToDirectionScreen() {
        directionString.forEachIndexed { idx, direction ->
            assertEquals(direction.toDirectionScreen(), directions[idx])
        }
    }

    @Test
    @DisplayName("Char.arrowToDirectionScreen")
    fun charArrowToDirectionScreen() {
        listOf('^', '>', 'v', '<').forEachIndexed { idx, direction ->
            assertEquals(direction.arrowToDirectionScreen(), directions[idx])
        }
        DirectionScreen.ARROWS.forEachIndexed { idx, direction ->
            assertEquals(direction.arrowToDirectionScreen(), directions[idx])
        }
    }

    @Test
    @DisplayName("String.arrowToDirectionScreen")
    fun stringArrowToDirectionScreen() {
        listOf("^", ">", "v", "<").forEachIndexed { idx, direction ->
            assertEquals(direction.arrowToDirectionScreen(), directions[idx])
        }
    }
}
