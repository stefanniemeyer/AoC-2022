package de.niemeyer.aoc.grid

import de.niemeyer.aoc.direction.DirectionScreen
import org.assertj.core.api.Assertions.assertThat
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
            assertThat(p.neighbors.toSet()).isEqualTo(correct)
        }
    }

    @Test
    @DisplayName("axisNeighbors")
    fun getAxisNeighbors() {
        assertThat(GridCellScreen(3, 7).axisNeighbors.toSet()).isEqualTo(
            setOf(GridCellScreen(2, 7), GridCellScreen(3, 6), GridCellScreen(4, 7), GridCellScreen(3, 8))
        )
        assertThat(GridCellScreen(-3, 7).axisNeighbors.toSet()).isEqualTo(
            setOf(GridCellScreen(-2, 7), GridCellScreen(-3, 6), GridCellScreen(-4, 7), GridCellScreen(-3, 8))
        )
        assertThat(GridCellScreen(-3, -7).axisNeighbors.toSet()).isEqualTo(
            setOf(GridCellScreen(-2, -7), GridCellScreen(-3, -6), GridCellScreen(-4, -7), GridCellScreen(-3, -8))
        )
        assertThat(GridCellScreen(3, -7).axisNeighbors.toSet()).isEqualTo(
            setOf(GridCellScreen(2, -7), GridCellScreen(3, -6), GridCellScreen(4, -7), GridCellScreen(3, -8))
        )
    }

    @Test
    @DisplayName("sharesAxisWith")
    fun testSharesAxisWith() {
        assert(GridCellScreen(3, 7) sharesAxisWith GridCellScreen(3, 2))
        assert(GridCellScreen(3, 7) sharesAxisWith GridCellScreen(1, 7))
        assertThat(GridCellScreen(3, 7) sharesAxisWith GridCellScreen(-3, 2)).isEqualTo(false)
        assertThat(GridCellScreen(3, 7) sharesAxisWith GridCellScreen(1, -7)).isEqualTo(false)
    }

    @Test
    @DisplayName("plus")
    fun testPlus() {
        assertThat(GridCellScreen(3, 7) + GridCellScreen(1, 2)).isEqualTo(GridCellScreen(4, 9))
        assertThat(GridCellScreen(3, 7) + GridCellScreen(-9, 2)).isEqualTo(GridCellScreen(-6, 9))
        assertThat(GridCellScreen(3, 7) + GridCellScreen(-5, -11)).isEqualTo(GridCellScreen(-2, -4))
        assertThat(GridCellScreen(3, 7) + GridCellScreen(-1, -15)).isEqualTo(GridCellScreen(2, -8))
    }

    @Test
    @DisplayName("times")
    fun testTimes() {
        assertThat(GridCellScreen(3, 7) * 5).isEqualTo(GridCellScreen(15, 35))
        assertThat(GridCellScreen(-3, 7) * 5).isEqualTo(GridCellScreen(-15, 35))
        assertThat(GridCellScreen(-3, -7) * 5).isEqualTo(GridCellScreen(-15, -35))
        assertThat(GridCellScreen(3, -7) * 5).isEqualTo(GridCellScreen(15, -35))
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
                assertThat(p.move(dir)).isEqualTo(p + offset)
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
                assertThat(p.moveTimes(dir, 3)).isEqualTo(p + (offset * 3))
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
        assertThat(GridCellScreen(5, 2) lineTo GridCellScreen(5, 4)).isEqualTo(wayHorizontal)

        val wayVertical = listOf(
            GridCellScreen(2, 1),
            GridCellScreen(3, 1),
            GridCellScreen(4, 1),
        )
        assertThat(GridCellScreen(2, 1) lineTo GridCellScreen(4, 1)).isEqualTo(wayVertical)

        val wayDiagonal = listOf(
            GridCellScreen(-1, -2),
            GridCellScreen(0, -1),
            GridCellScreen(1, 0),
            GridCellScreen(2, 1)
        )
        assertThat(GridCellScreen(-1, -2) lineTo GridCellScreen(2, 1)).isEqualTo(wayDiagonal)
    }

    @Test
    @DisplayName("manhattanDistanceTo")
    fun testManhattanDistanceTo() {
        assertThat(GridCellScreen(1, 1).manhattanDistanceTo(GridCellScreen(2, 2))).isEqualTo(2)
        assertThat(GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(9, 13))).isEqualTo(12)
        assertThat(GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(-9, 13))).isEqualTo(18)
        assertThat(GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(-9, -13))).isEqualTo(32)
        assertThat(GridCellScreen(3, 7).manhattanDistanceTo(GridCellScreen(9, -13))).isEqualTo(26)
    }

    @Test
    @DisplayName("chebyshevDistanceTo")
    fun testChebyshevDistanceTo() {
        assertThat(GridCellScreen(1, 1).chebyshevDistanceTo(GridCellScreen(2, 2))).isEqualTo(1)
        assertThat(GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(9, 13))).isEqualTo(6)
        assertThat(GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(-9, 13))).isEqualTo(12)
        assertThat(GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(-9, -13))).isEqualTo(20)
        assertThat(GridCellScreen(3, 7).chebyshevDistanceTo(GridCellScreen(9, -13))).isEqualTo(20)
    }

    @Test
    @DisplayName("GridCellScreen.of")
    fun testOf() {
        assertThat(GridCellScreen.of("1,2")).isEqualTo(GridCellScreen(1, 2))
        assertThat(GridCellScreen.of("-3,5")).isEqualTo(GridCellScreen(-3, 5))
        assertThat(GridCellScreen.of("-7 -3", delimiter = " ")).isEqualTo(GridCellScreen(-7, -3))
        assertThat(GridCellScreen.of("9:-17", delimiter = ":")).isEqualTo(GridCellScreen(9, -17))
    }
}
