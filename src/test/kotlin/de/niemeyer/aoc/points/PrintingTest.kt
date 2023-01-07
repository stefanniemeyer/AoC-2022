package de.niemeyer.aoc.points

import org.assertj.core.api.Assertions.assertThat
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
        assertThat(parsePoint2dSetTopLeft(inp).toStringTopLeft()).isEqualTo(inp)
    }

    @Test
    @DisplayName("toStringBottomLeft")
    fun testToStringBottomLeft() {
        assertThat(parsePoint2dSetBottomLeft(inp).toStringBottomLeft()).isEqualTo(inp)
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
        assertThat(m.toPrintableStringExisting('+')).isEqualTo(correct)
        assertThat(m.toPrintableStringExisting()).isEqualTo(correct.replace('+', ' '))
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
        assertThat(inp.toPrintableStringBottomLeft()).isEqualTo(correct)
    }
}
