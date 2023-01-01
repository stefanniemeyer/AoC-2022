package de.niemeyer.aoc.grid

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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
            val res = gq1.rotate(TileInstructions(ORIENT_NORMAL, Side.TOP))
            assertEquals(gq1.gridMap.keys.size, res.gridMap.keys.size)
            assertEquals(gq1.offset.row, res.offset.row)
            assertEquals(gq1.offset.column, res.offset.column)
            assert(gq1.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(0, 0),
                GridCellScreen(0, 7) to GridCellScreen(0, 7),
                GridCellScreen(6, 0) to GridCellScreen(6, 0),
                GridCellScreen(6, 7) to GridCellScreen(6, 7),
            )
            transposed.forEach { (org, newPos) ->
                assertEquals(org, res.gridMap.getValue(newPos).original)
            }

            val resAt35 = gq1at35.rotate(TileInstructions(ORIENT_NORMAL, Side.TOP))
            assertEquals(gq1at35.gridMap.keys.size, resAt35.gridMap.keys.size)
            assertEquals(gq1at35.offset.row, resAt35.offset.row)
            assertEquals(gq1at35.offset.column, resAt35.offset.column)
            assert(gq1at35.gridMap.keys.containsAll(resAt35.gridMap.keys))
        }

        @Test
        @DisplayName("rotate right")
        fun testRotateRight() {
            val res = gq1.rotate(TileInstructions(ORIENT_NORMAL, Side.RIGHT))
            assertEquals(gq4.gridMap.keys.size, res.gridMap.keys.size)
            assertEquals(gq4.offset.row, res.offset.row)
            assertEquals(gq4.offset.column, res.offset.column)
            assert(gq4.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(0, 6),
                GridCellScreen(0, 7) to GridCellScreen(7, 6),
                GridCellScreen(6, 0) to GridCellScreen(0, 0),
                GridCellScreen(6, 7) to GridCellScreen(7, 0),
            )
            transposed.forEach { (org, newPos) ->
                assertEquals(org, res.gridMap.getValue(newPos).original)
            }

            val resAt53 = gq1at35.rotate(TileInstructions(ORIENT_NORMAL, Side.RIGHT))
            assertEquals(gq4at53.gridMap.keys.size, resAt53.gridMap.keys.size)
            assertEquals(gq4at53.offset.row, resAt53.offset.row)
            assertEquals(gq4at53.offset.column, resAt53.offset.column)
            assert(gq4at53.gridMap.keys.containsAll(resAt53.gridMap.keys))
        }

        @Test
        @DisplayName("rotate bottom")
        fun testRotateBottom() {
            val res = gq1.rotate(TileInstructions(ORIENT_NORMAL, Side.BOTTOM))
            assertEquals(gq3.gridMap.keys.size, res.gridMap.keys.size)
            assertEquals(gq3.offset.row, res.offset.row)
            assertEquals(gq3.offset.column, res.offset.column)
            assert(gq3.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(6, 7),
                GridCellScreen(0, 7) to GridCellScreen(6, 0),
                GridCellScreen(6, 0) to GridCellScreen(0, 7),
                GridCellScreen(6, 7) to GridCellScreen(0, 0),
            )
            transposed.forEach { (org, newPos) ->
                assertEquals(org, res.gridMap.getValue(newPos).original)
            }

            val resAt35 = gq1at35.rotate(TileInstructions(ORIENT_NORMAL, Side.BOTTOM))
            assertEquals(gq3at35.gridMap.keys.size, resAt35.gridMap.keys.size)
            assertEquals(gq3at35.offset.row, resAt35.offset.row)
            assertEquals(gq3at35.offset.column, resAt35.offset.column)
            assert(gq3at35.gridMap.keys.containsAll(resAt35.gridMap.keys))
        }

        @Test
        @DisplayName("rotate left")
        fun testRotateLeft() {
            val res = gq1.rotate(TileInstructions(ORIENT_NORMAL, Side.LEFT))
            assertEquals(gq2.gridMap.keys.size, res.gridMap.keys.size)
            assertEquals(gq2.offset.row, res.offset.row)
            assertEquals(gq2.offset.column, res.offset.column)
            assert(gq2.gridMap.keys.containsAll(res.gridMap.keys))
            val transposed = listOf(
                GridCellScreen(0, 0) to GridCellScreen(7, 0),
                GridCellScreen(0, 7) to GridCellScreen(0, 0),
                GridCellScreen(6, 0) to GridCellScreen(7, 6),
                GridCellScreen(6, 7) to GridCellScreen(0, 6),
            )
            transposed.forEach { (org, newPos) ->
                assertEquals(org, res.gridMap.getValue(newPos).original)
            }

            val resAt53 = gq1at35.rotate(TileInstructions(ORIENT_NORMAL, Side.LEFT))
            assertEquals(gq2at53.gridMap.keys.size, resAt53.gridMap.keys.size)
            assertEquals(gq2at53.offset.row, resAt53.offset.row)
            assertEquals(gq2at53.offset.column, resAt53.offset.column)
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
            assertEquals(correct[row], range)
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
            assertEquals(correct[column], range)
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
        assertEquals(qne, res)
    }

    @Test
    @DisplayName("toPrintableStringWithDefault")
    fun testToPrintableStringWithDefault() {
        val mv = mapOf(
            GridCellScreen(1, 2) to GridCellContainer(true),
            GridCellScreen(2, 4) to GridCellContainer(true),
        )
        val res = Grid(mv).toPrintableStringWithDefault()
        assertEquals(
            listOf(
                "#..",
                "..#",
            ).joinToString("\n"),
            res
        )
    }
}
