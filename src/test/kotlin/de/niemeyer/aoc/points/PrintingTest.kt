package de.niemeyer.aoc.points

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PrintingTest {
    val inp = """
            ##.....##
            #..#..#..
            .#.#...#.
        """.trimIndent()

    @Test
    @DisplayName("toStringTopLeft")
    fun testToStringTopLeft() {
        assertEquals(inp, parsePoint2dSetTopLeft(inp).toStringTopLeft())
    }

    @Test
    @DisplayName("toStringBottomLeft")
    fun testToStringBottomLeft() {
        assertEquals(inp, parsePoint2dSetBottomLeft(inp).toStringBottomLeft())
    }

    @Test
    @DisplayName("toPrintableStringExisting")
    fun testToPrintableStringExisting() {
        val m = mapOf(
            Point2D(2, 1) to true,
            Point2D(5, 4) to true
        )
        val correct = """
            ++++++
            ++#+++
            ++++++
            ++++++
            +++++#
        """.trimIndent()
        assertEquals(correct, m.toPrintableStringExisting('+'))
        assertEquals(correct.replace('+', ' '), m.toPrintableStringExisting())
    }

    @Test
    @DisplayName("Array<CharArray>.toPrintableStringBottomLeft")
    fun testToPrintableStringBottomLeft() {
        val inp = arrayOf(
            (
                    "abcde").toCharArray(),
            "12345".toCharArray(),
            "ABCDE".toCharArray()
        )
        val correct = """
            abcde
            12345
            ABCDE
        """.trimIndent()
        assertEquals(correct, inp.toPrintableStringBottomLeft())
    }
}
