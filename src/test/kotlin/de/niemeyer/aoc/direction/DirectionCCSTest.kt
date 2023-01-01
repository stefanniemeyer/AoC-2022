package de.niemeyer.aoc.direction

import de.niemeyer.aoc.points.Point2D
import org.junit.jupiter.api.Assertions.*
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
            { assertEquals(Point2D(0, 1), DirectionCCS.Up.offset) },
            { assertEquals(Point2D(1, 0), DirectionCCS.Right.offset) },
            { assertEquals(Point2D(0, -1), DirectionCCS.Down.offset) },
            { assertEquals(Point2D(-1, 0), DirectionCCS.Left.offset) },
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
    @DisplayName("DirectionCCS.toString")
    fun testToString() {
        directions.forEachIndexed { idx, direction ->
            assertEquals(direction.toString(), directionString[idx].first().toString())
        }
    }

    @Test
    @DisplayName("String.toDirectionCCS")
    fun testToDirectionCCS() {
        directionString.forEachIndexed { idx, direction ->
            assertEquals(direction.toDirectionCCS(), directions[idx])
        }
    }

    @Test
    @DisplayName("Char.arrowToDirectionCCS")
    fun charArrowToDirectionCCS() {
        listOf('^', '>', 'v', '<').forEachIndexed { idx, direction ->
            assertEquals(direction.arrowToDirectionCCS(), directions[idx])
        }
        DirectionCCS.ARROWS.forEachIndexed { idx, direction ->
            assertEquals(direction.arrowToDirectionCCS(), directions[idx])
        }
    }

    @Test
    @DisplayName("String.arrowToDirectionCCS")
    fun stringArrowToDirectionCCS() {
        listOf("^", ">", "v", "<").forEachIndexed { idx, direction ->
            assertEquals(direction.arrowToDirectionCCS(), directions[idx])
        }
    }
}
