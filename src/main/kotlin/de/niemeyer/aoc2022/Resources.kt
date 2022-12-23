package de.niemeyer.aoc2022

import java.io.File

typealias Point2DBooleanMap = Map<Point2D, Boolean>
typealias Point2DIntMap = Map<Point2D, Int>
typealias Point2DIntArray = Array<IntArray>

internal object Resources {
    fun resourceAsString(fileName: String, delimiter: String = ""): String =
        resourceAsList(fileName).reduce { a, b -> "$a$delimiter$b" }

    fun resourceAsText(fileName: String): String =
        File("src", "$fileName.txt").readText()

    fun resourceAsList(fileName: String): List<String> =
        File("src", "$fileName.txt").readLines()

    fun resourceAsListOfInt(fileName: String): List<Int> =
        resourceAsList(fileName).map { it.toInt() }

    fun resourceAsListOfLong(fileName: String): List<Long> =
        resourceAsList(fileName).map { it.toLong() }

    fun resourceAsListOfString(fileName: String): List<String> =
        File("src", "$fileName.txt").readLines()

    fun resourceAsPoint2DBooleanMap(fileName: String): Point2DBooleanMap {
        val input = File("src", "$fileName.txt").readLines()

        return input.flatMapIndexed { y, row ->
            row.mapIndexed { x, point ->
                Point2D(x, y) to (point == '#')
            }
        }.toMap()
    }

    fun resourceAsPoint2DIntMap(fileName: String): Point2DIntMap {
        val input = File("src", "$fileName.txt").readLines()

        return input.flatMapIndexed { y, row ->
            row.mapIndexed { x, point ->
                Point2D(x, y) to point.digitToInt()
            }
        }.toMap()
    }

    fun resourceAsArrayOfIntArray(fileName: String): Array<IntArray> {
        val input = File("src", "$fileName.txt").readLines()

        return input.map { row ->
            row.map { digit ->
                digit.digitToInt()
            }.toIntArray()
        }.toTypedArray()
    }

    fun resourceAsArrayOfCharArray(fileName: String): Array<CharArray> {
        val input = File("src", "$fileName.txt").readLines()

        return input.map { row ->
            row.map { digit ->
                digit
            }.toCharArray()
        }.toTypedArray()
    }
}
