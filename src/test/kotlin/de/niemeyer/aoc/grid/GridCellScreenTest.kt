package de.niemeyer.aoc.grid

import de.niemeyer.aoc.direction.DirectionScreen
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GridCellScreenTest {
    @Test
    @DisplayName("neighbors")
    fun getNeighbors() {
        val offsets = listOf(
            GridCellScreen(-1, 1), GridCellScreen(0, 1), GridCellScreen(1, 1),
            GridCellScreen(-1, 0), /* GridCellScreen(0, 1), */ GridCellScreen(1, 0),
            GridCellScreen(-1, -1), GridCellScreen(0, -1), GridCellScreen(1, -1),
        )
        val ps = listOf(GridCellScreen(3, 7), GridCellScreen(-3, 7), GridCellScreen(-3, -7), GridCellScreen(3, -7))
        ps.forEach { p ->
            val correct = offsets.map { it + p }.toSet()
            assertEquals(correct, p.neighbors.toSet())
        }
    }

    @Test
    @DisplayName("axisNeighbors")
    fun getAxisNeighbors() {
        assertEquals(
            setOf(GridCellScreen(2, 7), GridCellScreen(3, 6), GridCellScreen(4, 7), GridCellScreen(3, 8)),
            GridCellScreen(3, 7).axisNeighbors.toSet()
        )
        assertEquals(
            setOf(GridCellScreen(-2, 7), GridCellScreen(-3, 6), GridCellScreen(-4, 7), GridCellScreen(-3, 8)),
            GridCellScreen(-3, 7).axisNeighbors.toSet()
        )
        assertEquals(
            setOf(GridCellScreen(-2, -7), GridCellScreen(-3, -6), GridCellScreen(-4, -7), GridCellScreen(-3, -8)),
            GridCellScreen(-3, -7).axisNeighbors.toSet()
        )
        assertEquals(
            setOf(GridCellScreen(2, -7), GridCellScreen(3, -6), GridCellScreen(4, -7), GridCellScreen(3, -8)),
            GridCellScreen(3, -7).axisNeighbors.toSet()
        )
    }

    @Test
    @DisplayName("sharesAxisWith")
    fun testSharesAxisWith() {
        assert(GridCellScreen(3, 7) sharesAxisWith GridCellScreen(3, 2))
        assert(GridCellScreen(3, 7) sharesAxisWith GridCellScreen(1, 7))
        assertEquals(false, GridCellScreen(3, 7) sharesAxisWith GridCellScreen(-3, 2))
        assertEquals(false, GridCellScreen(3, 7) sharesAxisWith GridCellScreen(1, -7))
    }

    @Test
    @DisplayName("plus")
    fun testPlus() {
        assertEquals(GridCellScreen(4, 9), GridCellScreen(3, 7) + GridCellScreen(1, 2))
        assertEquals(GridCellScreen(-6, 9), GridCellScreen(3, 7) + GridCellScreen(-9, 2))
        assertEquals(GridCellScreen(-2, -4), GridCellScreen(3, 7) + GridCellScreen(-5, -11))
        assertEquals(GridCellScreen(2, -8), GridCellScreen(3, 7) + GridCellScreen(-1, -15))
    }

    @Test
    @DisplayName("times")
    fun testTimes() {
        assertEquals(GridCellScreen(15, 35), GridCellScreen(3, 7) * 5)
        assertEquals(GridCellScreen(-15, 35), GridCellScreen(-3, 7) * 5)
        assertEquals(GridCellScreen(-15, -35), GridCellScreen(-3, -7) * 5)
        assertEquals(GridCellScreen(15, -35), GridCellScreen(3, -7) * 5)
    }

    val directionScreen = listOf(
        DirectionScreen.Up,
        DirectionScreen.Right,
        DirectionScreen.Down,
        DirectionScreen.Left,
    )

    val offsets = listOf(
        GridCellScreen(-1, 0),
        GridCellScreen(0, 1),
        GridCellScreen(1, 0),
        GridCellScreen(0, -1),
    )

    @Test
    @DisplayName("move")
    fun testMove() {
        val ps = listOf(
            GridCellScreen(3, 7),
            GridCellScreen(-3, 7),
            GridCellScreen(-3, -7),
            GridCellScreen(3, -7)
        )
        offsets.zip(directionScreen).forEach { (offset, dir) ->
            ps.forEach { p ->
                assertEquals(p + offset, p.move(dir))
            }
        }
    }

    @Test
    @DisplayName("moveTimes")
    fun testMoveTimes() {
        val ps = listOf(
            GridCellScreen(3, 7),
            GridCellScreen(-3, 7),
            GridCellScreen(-3, -7),
            GridCellScreen(3, -7)
        )
        offsets.zip(directionScreen).forEach { (offset, dir) ->
            ps.forEach { p ->
                assertEquals(p + (offset * 3), p.moveTimes(dir, 3))
            }
        }
    }

    @Test
    @DisplayName("lineTo")
    fun testLineTo() {
        val wayHorizontal = listOf(
            GridCellScreen(5, 2),
            GridCellScreen(5, 3),
            GridCellScreen(5, 4),
        )
        assertEquals(wayHorizontal, GridCellScreen(5, 2) lineTo GridCellScreen(5, 4))

        val wayVertical = listOf(
            GridCellScreen(2, 1),
            GridCellScreen(3, 1),
            GridCellScreen(4, 1),
        )
        assertEquals(wayVertical, GridCellScreen(2, 1) lineTo GridCellScreen(4, 1))

        val wayDiagonal = listOf(
            GridCellScreen(-1, -2),
            GridCellScreen(0, -1),
            GridCellScreen(1, 0),
            GridCellScreen(2, 1)
        )
        assertEquals(wayDiagonal, GridCellScreen(-1, -2) lineTo GridCellScreen(2, 1))
    }

    @Test
    @DisplayName("manhattanDistanceTo")
    fun testManhattanDistanceTo() {
        assertEquals(2, GridCellScreen(1, 1).manhattanDistanceTo(GridCellScreen(2, 2)))
        assertEquals(12, GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(9, 13)))
        assertEquals(18, GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(-9, 13)))
        assertEquals(32, GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(-9, -13)))
        assertEquals(26, GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(9, -13)))
    }

    @Test
    @DisplayName("chebyshevDistanceTo")
    fun testChebyshevDistanceTo() {
        assertEquals(1, GridCellScreen(1, 1).chebyshevDistanceTo(GridCellScreen(2, 2)))
        assertEquals(6, GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(9, 13)))
        assertEquals(12, GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(-9, 13)))
        assertEquals(20, GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(-9, -13)))
        assertEquals(20, GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(9, -13)))
    }

    @Test
    @DisplayName("GridCellScreen.of")
    fun testOf() {
        assertEquals(GridCellScreen(1, 2), GridCellScreen.of("1,2"))
        assertEquals(GridCellScreen(-3, 5), GridCellScreen.of("-3,5"))
        assertEquals(GridCellScreen(-7, -3), GridCellScreen.of("-7 -3", delimiter = " "))
        assertEquals(GridCellScreen(9, -17), GridCellScreen.of("9:-17", delimiter = ":"))
    }
}
