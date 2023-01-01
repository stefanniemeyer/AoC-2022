package de.niemeyer.aoc.points

import de.niemeyer.aoc.direction.CompassDirectionCCS
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

class Point2DTest {
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

    //    val sq1yMax = q1.lines().size - 1
//    val sq1xMax = q1.lines().first().length - 1
    val sq1 = parsePoint2dSetBottomLeft(q1)

    //    val sq2yMax = q2.lines().size - 1
    val sq2xMax = q2.lines().first().length - 1
    val sq2 = parsePoint2dSetBottomLeft(q2).map { it + Point2D(-sq2xMax, 0) }.toSet()

    val sq3yMax = q3.lines().size - 1
    val sq3xMax = q3.lines().first().length - 1
    val sq3 = parsePoint2dSetBottomLeft(q3).map { it + Point2D(-sq3xMax, -sq3yMax) }.toSet()

    val sq4yMax = q4.lines().size - 1

    //    val sq4xMax = q4.lines().first().length - 1
    val sq4 = parsePoint2dSetBottomLeft(q4).map { it + Point2D(0, -sq4yMax) }.toSet()

    @Test
    @DisplayName("neighbors")
    fun getNeighbors() {
        val offsets = listOf(
            Point2D(-1, 1), Point2D(0, 1), Point2D(1, 1),
            Point2D(-1, 0), /* Point2D(0, 1), */ Point2D(1, 0),
            Point2D(-1, -1), Point2D(0, -1), Point2D(1, -1),
        )
        val ps = listOf(Point2D(3, 7), Point2D(-3, 7), Point2D(-3, -7), Point2D(3, -7))
        ps.forEach { p ->
            val correct = offsets.map { it + p }.toSet()
            assertEquals(correct, p.neighbors.toSet())
        }
    }

    @Test
    @DisplayName("axisNeighbors")
    fun getAxisNeighbors() {
        assertEquals(
            setOf(Point2D(2, 7), Point2D(3, 6), Point2D(4, 7), Point2D(3, 8)),
            Point2D(3, 7).axisNeighbors.toSet()
        )
        assertEquals(
            setOf(Point2D(-2, 7), Point2D(-3, 6), Point2D(-4, 7), Point2D(-3, 8)),
            Point2D(-3, 7).axisNeighbors.toSet()
        )
        assertEquals(
            setOf(Point2D(-2, -7), Point2D(-3, -6), Point2D(-4, -7), Point2D(-3, -8)),
            Point2D(-3, -7).axisNeighbors.toSet()
        )
        assertEquals(
            setOf(Point2D(2, -7), Point2D(3, -6), Point2D(4, -7), Point2D(3, -8)),
            Point2D(3, -7).axisNeighbors.toSet()
        )
    }

    @Test
    @DisplayName("sharesAxisWith")
    fun testSharesAxisWith() {
        assert(Point2D(3, 7) sharesAxisWith Point2D(3, 2))
        assert(Point2D(3, 7) sharesAxisWith Point2D(1, 7))
        assertEquals(false, Point2D(3, 7) sharesAxisWith Point2D(-3, 2))
        assertEquals(false, Point2D(3, 7) sharesAxisWith Point2D(1, -7))
    }

    @Test
    @DisplayName("plus")
    fun testPlus() {
        assertEquals(Point2D(4, 9), Point2D(3, 7) + Point2D(1, 2))
        assertEquals(Point2D(-6, 9), Point2D(3, 7) + Point2D(-9, 2))
        assertEquals(Point2D(-2, -4), Point2D(3, 7) + Point2D(-5, -11))
        assertEquals(Point2D(2, -8), Point2D(3, 7) + Point2D(-1, -15))
    }

    @Test
    @DisplayName("times")
    fun testTimes() {
        assertEquals(Point2D(15, 35), Point2D(3, 7) * 5)
        assertEquals(Point2D(-15, 35), Point2D(-3, 7) * 5)
        assertEquals(Point2D(-15, -35), Point2D(-3, -7) * 5)
        assertEquals(Point2D(15, -35), Point2D(3, -7) * 5)
    }

    val compassDirectionsCCS = listOf(
        CompassDirectionCCS.North,
        CompassDirectionCCS.NorthEast,
        CompassDirectionCCS.East,
        CompassDirectionCCS.SouthEast,
        CompassDirectionCCS.South,
        CompassDirectionCCS.SouthWest,
        CompassDirectionCCS.West,
        CompassDirectionCCS.NorthWest,
    )

    val offsets = listOf(
        Point2D(0, 1), Point2D(1, 1),
        Point2D(1, 0), Point2D(1, -1),
        Point2D(0, -1), Point2D(-1, -1),
        Point2D(-1, 0), Point2D(-1, 1),
    )

    @Test
    @DisplayName("move")
    fun testMove() {
        val ps = listOf(
            Point2D(3, 7),
            Point2D(-3, 7),
            Point2D(-3, -7),
            Point2D(3, -7)
        )
        offsets.zip(compassDirectionsCCS).forEach { (offset, dir) ->
            ps.forEach { p ->
                assertEquals(p + offset, p.move(dir))
            }
        }
    }

    @Test
    @DisplayName("moveTimes")
    fun testMoveTimes() {
        val ps = listOf(
            Point2D(3, 7),
            Point2D(-3, 7),
            Point2D(-3, -7),
            Point2D(3, -7)
        )
        offsets.zip(compassDirectionsCCS).forEach { (offset, dir) ->
            ps.forEach { p ->
                assertEquals(p + (offset * 3), p.moveTimes(dir, 3))
            }
        }
    }

    @Test
    @DisplayName("rotateLeft")
    fun testRotateLeft() {
        val rot = sq1.map { it.rotateLeft() }.toSet()
        assertEquals(rot.size, sq2.size)
        assertEquals(rot, sq2)
    }

    @Test
    @DisplayName("rotateRight")
    fun testRotateRight() {
        val rot = sq1.map { it.rotateRight() }.toSet()
        assertEquals(rot.size, sq4.size)
        assertEquals(rot, sq4)
    }

    @Test
    @DisplayName("rotate")
    fun testRotate() {
        val testing = mapOf(0 to sq1, 90 to sq2, 180 to sq3, 270 to sq4, 360 to sq1)
        testing.forEach { (deg, correct) ->
            val rot = sq1.map { it.rotate(deg) }.toSet()
            assertEquals(rot.size, correct.size)
            assertEquals(rot, correct)
        }
    }

    @Test
    @DisplayName("reflection")
    fun testReflection() {
        assertEquals(Point2D(3, -7), Point2D(3, 7).reflection(0))
        assertEquals(Point2D(-3, 7), Point2D(3, 7).reflection(90))
        assertEquals(Point2D(3, -7), Point2D(3, 7).reflection(180))
        assertEquals(Point2D(-3, 7), Point2D(3, 7).reflection(270))
    }

    @Test
    @DisplayName("lineTo")
    fun testLineTo() {
        val wayHorizontal = listOf(
            Point2D(2, 5),
            Point2D(3, 5),
            Point2D(4, 5),
        )
        assertEquals(wayHorizontal, Point2D(2, 5) lineTo Point2D(4, 5))

        val wayVertical = listOf(
            Point2D(1, 2),
            Point2D(1, 3),
            Point2D(1, 4),
        )
        assertEquals(wayVertical, Point2D(1, 2) lineTo Point2D(1, 4))

        val wayDiagonal = listOf(
            Point2D(-2, -1),
            Point2D(-1, 0),
            Point2D(0, 1),
            Point2D(1, 2)
        )
        assertEquals(wayDiagonal, Point2D(-2, -1) lineTo Point2D(1, 2))
    }

    @Test
    @DisplayName("manhattanDistanceTo")
    fun testManhattanDistanceTo() {
        assertEquals(2, Point2D(1, 1).manhattanDistanceTo(Point2D(2, 2)))
        assertEquals(12, Point2D(3, 7).manhattanDistanceTo(Point2D(9, 13)))
        assertEquals(18, Point2D(3, 7).manhattanDistanceTo(Point2D(-9, 13)))
        assertEquals(32, Point2D(3, 7).manhattanDistanceTo(Point2D(-9, -13)))
        assertEquals(26, Point2D(3, 7).manhattanDistanceTo(Point2D(9, -13)))
    }

    @Test
    @DisplayName("chebyshevDistanceTo")
    fun testChebyshevDistanceTo() {
        assertEquals(1, Point2D(1, 1).chebyshevDistanceTo(Point2D(2, 2)))
        assertEquals(6, Point2D(3, 7).chebyshevDistanceTo(Point2D(9, 13)))
        assertEquals(12, Point2D(3, 7).chebyshevDistanceTo(Point2D(-9, 13)))
        assertEquals(20, Point2D(3, 7).chebyshevDistanceTo(Point2D(-9, -13)))
        assertEquals(20, Point2D(3, 7).chebyshevDistanceTo(Point2D(9, -13)))
    }

    @Test
    @DisplayName("Point2D.of")
    fun testOf() {
        assertEquals(Point2D(1, 2), Point2D.of("1,2"))
        assertEquals(Point2D(-3, 5), Point2D.of("-3,5"))
        assertEquals(Point2D(-7, -3), Point2D.of("-7 -3", delimiter = " "))
        assertEquals(Point2D(9, -17), Point2D.of("9:-17", delimiter = ":"))
    }
}
