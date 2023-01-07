package de.niemeyer.aoc.points

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ParsingTest {
    val inp = """
            ##.....##
            #..#..#..
            .#.#...#.
        """.trimIndent()

    @Test
    @DisplayName("parsePoint2dSetTopLeft")
    fun testParsePoint2dSetTopLeft() {
        val tl = parsePoint2dSetTopLeft(inp)
        val correct = setOf(
            Point2D(0, 0),
            Point2D(1, 0),
            Point2D(7, 0),
            Point2D(8, 0),
            Point2D(0, 1),
            Point2D(3, 1),
            Point2D(6, 1),
            Point2D(1, 2),
            Point2D(3, 2),
            Point2D(7, 2),
        )
        assertThat(tl.size).isEqualTo(correct.size)
        assertThat(tl).isEqualTo(correct)
    }

    @Test
    @DisplayName("parsePoint2dSetBottomLeft")
    fun testParsePoint2dSetBottomLeft() {
        val tl = parsePoint2dSetBottomLeft(inp)
        val correct = setOf(
            Point2D(1, 0),
            Point2D(3, 0),
            Point2D(7, 0),
            Point2D(0, 1),
            Point2D(3, 1),
            Point2D(6, 1),
            Point2D(0, 2),
            Point2D(1, 2),
            Point2D(7, 2),
            Point2D(8, 2),
        )
        assertThat(tl.size).isEqualTo(correct.size)
        assertThat(tl).isEqualTo(correct)
    }
}
