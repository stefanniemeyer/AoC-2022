package de.niemeyer.aoc.grid

import de.niemeyer.aoc.direction.DirectionScreen
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

class GridTest {
    val q1 = """
            #.....##
            ....#..#
            #...#...
            ....#...
            #...#...
            ........
            ########
        """.trimIndent()

    val q2 = """
            ##....#
            #.....#
            ......#
            .####.#
            ......#
            ......#
            ......#
            #.#.#.#
        """.trimIndent()

    val q3 = """
            ########
            ........
            ...#...#
            ...#....
            ...#...#
            #..#....
            ##.....#
        """.trimIndent()

    val q4 = """
            #.#.#.#
            #......
            #......
            #......
            #.####.
            #......
            #.....#
            #....##
        """.trimIndent()

    val gq1 = Grid.of(q1, offset = GridCellScreen(0, 0))
    val gq2 = Grid.of(q2, offset = GridCellScreen(0, 0))
    val gq3 = Grid.of(q3, offset = GridCellScreen(0, 0))
    val gq4 = Grid.of(q4, offset = GridCellScreen(0, 0))
    val gq1at35 = Grid.of(q1, offset = GridCellScreen(3, 5))
    val gq2at53 = Grid.of(q2, offset = GridCellScreen(5, 3))
    val gq3at35 = Grid.of(q3, offset = GridCellScreen(3, 5))
    val gq4at53 = Grid.of(q4, offset = GridCellScreen(5, 3))

    @Nested
    inner class Rotate {
        @Test
        @DisplayName("rotate top")
        fun testRotateTop() {
            val res = gq1.rotate(TileInstructions(DirectionScreen.Up))
            assertThat(res.gridMap.keys.size).isEqualTo(gq1.gridMap.keys.size)
            assertThat(res.offset.row).isEqualTo(gq1.offset.row)
            assertThat(res.offset.column).isEqualTo(gq1.offset.column)
            assert(gq1.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(0, 0),
                GridCellScreen(0, 7) to GridCellScreen(0, 7),
                GridCellScreen(6, 0) to GridCellScreen(6, 0),
                GridCellScreen(6, 7) to GridCellScreen(6, 7),
            )
            transposed.forEach { (org, newPos) ->
                assertThat(res.gridMap.getValue(newPos).original).isEqualTo(org)
            }

            val resAt35 = gq1at35.rotate(TileInstructions(DirectionScreen.Up))
            assertThat(resAt35.gridMap.keys.size).isEqualTo(gq1at35.gridMap.keys.size)
            assertThat(resAt35.offset.row).isEqualTo(gq1at35.offset.row)
            assertThat(resAt35.offset.column).isEqualTo(gq1at35.offset.column)
            assert(gq1at35.gridMap.keys.containsAll(resAt35.gridMap.keys))
        }

        @Test
        @DisplayName("rotate right")
        fun testRotateRight() {
            val res = gq1.rotate(TileInstructions(DirectionScreen.Right))
            assertThat(res.gridMap.keys.size).isEqualTo(gq4.gridMap.keys.size)
            assertThat(res.offset.row).isEqualTo(gq4.offset.row)
            assertThat(res.offset.column).isEqualTo(gq4.offset.column)
            assert(gq4.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(0, 6),
                GridCellScreen(0, 7) to GridCellScreen(7, 6),
                GridCellScreen(6, 0) to GridCellScreen(0, 0),
                GridCellScreen(6, 7) to GridCellScreen(7, 0),
            )
            transposed.forEach { (org, newPos) ->
                assertThat(res.gridMap.getValue(newPos).original).isEqualTo(org)
            }

            val resAt53 = gq1at35.rotate(TileInstructions(DirectionScreen.Right))
            assertThat(resAt53.gridMap.keys.size).isEqualTo(gq4at53.gridMap.keys.size)
            assertThat(resAt53.offset.row).isEqualTo(gq4at53.offset.row)
            assertThat(resAt53.offset.column).isEqualTo(gq4at53.offset.column)
            assert(gq4at53.gridMap.keys.containsAll(resAt53.gridMap.keys))
        }

        @Test
        @DisplayName("rotate bottom")
        fun testRotateBottom() {
            val res = gq1.rotate(TileInstructions(DirectionScreen.Down))
            assertThat(res.gridMap.keys.size).isEqualTo(gq3.gridMap.keys.size)
            assertThat(res.offset.row).isEqualTo(gq3.offset.row)
            assertThat(res.offset.column).isEqualTo(gq3.offset.column)
            assert(gq3.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(6, 7),
                GridCellScreen(0, 7) to GridCellScreen(6, 0),
                GridCellScreen(6, 0) to GridCellScreen(0, 7),
                GridCellScreen(6, 7) to GridCellScreen(0, 0),
            )
            transposed.forEach { (org, newPos) ->
                assertThat(res.gridMap.getValue(newPos).original).isEqualTo(org)
            }

            val resAt35 = gq1at35.rotate(TileInstructions(DirectionScreen.Down))
            assertThat(resAt35.gridMap.keys.size).isEqualTo(gq3at35.gridMap.keys.size)
            assertThat(resAt35.offset.row).isEqualTo(gq3at35.offset.row)
            assertThat(resAt35.offset.column).isEqualTo(gq3at35.offset.column)
            assert(gq3at35.gridMap.keys.containsAll(resAt35.gridMap.keys))
        }

        @Test
        @DisplayName("rotate left")
        fun testRotateLeft() {
            val res = gq1.rotate(TileInstructions(DirectionScreen.Left))
            assertThat(res.gridMap.keys.size).isEqualTo(gq2.gridMap.keys.size)
            assertThat(res.offset.row).isEqualTo(gq2.offset.row)
            assertThat(res.offset.column).isEqualTo(gq2.offset.column)
            assert(gq2.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(7, 0),
                GridCellScreen(0, 7) to GridCellScreen(0, 0),
                GridCellScreen(6, 0) to GridCellScreen(7, 6),
                GridCellScreen(6, 7) to GridCellScreen(0, 6),
            )
            transposed.forEach { (org, newPos) ->
                assertThat(res.gridMap.getValue(newPos).original).isEqualTo(org)
            }

            val resAt53 = gq1at35.rotate(TileInstructions(DirectionScreen.Left))
            assertThat(resAt53.gridMap.keys.size).isEqualTo(gq2at53.gridMap.keys.size)
            assertThat(resAt53.offset.row).isEqualTo(gq2at53.offset.row)
            assertThat(resAt53.offset.column).isEqualTo(gq2at53.offset.column)
            assert(gq2at53.gridMap.keys.containsAll(resAt53.gridMap.keys))
        }
    }

    val asym = """
        |  #.#
        | #.#
        |#######
    """.trimMargin()

    val gq = Grid.of(asym)

    @Test
    @DisplayName("columnRangesForRows")
    fun getColumnRangesForRows() {
        val correct = listOf(
            2..4,
            1..3,
            0..6,
        )
        gq.columnRangesForRows.forEach { (row, range) ->
            assertThat(range).isEqualTo(correct[row])
        }
    }

    @Test
    @DisplayName("rowRangesForColumns")
    fun getRowRangesForColumns() {
        val correct = listOf(
            2..2,
            1..2,
            0..2,
            0..2,
            0..2,
            2..2,
            2..2,
        )
        gq.rowRangesForColumns.forEach { (column, range) ->
            assertThat(range).isEqualTo(correct[column])
        }
    }

    @Test
    @DisplayName("toPrintableStringExisting")
    fun testToPrintableStringExisting() {
        val qne = listOf(
            "      ",
            "   #  ",
            "  #.# ",
            " #####",
        ).joinToString("\n")
        val gpne = Grid.of(qne)
        val res = gpne.toPrintableStringExisting()
        assertThat(res).isEqualTo(qne)
    }

    @Test
    @DisplayName("toPrintableStringWithDefault")
    fun testToPrintableStringWithDefault() {
        val mv = mapOf(
            GridCellScreen(1, 2) to GridCellContainer(true),
            GridCellScreen(2, 4) to GridCellContainer(true),
        )
        val res = Grid(mv).toPrintableStringWithDefault()
        assertThat(res).isEqualTo(
            listOf(
                "#..",
                "..#",
            ).joinToString("\n"),
        )
    }
}
