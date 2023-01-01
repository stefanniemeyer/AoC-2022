@file:Suppress("unused")

package de.niemeyer.aoc.points

fun Map<Point2D, Boolean>.toStringTopLeft() =
    this.keys.toSet().toStringWithOrientation(orientationTopLeft = true)

fun Map<Point2D, Boolean>.printTopLeft() =
    println(this.keys.toSet().toStringWithOrientation(orientationTopLeft = true))

fun Map<Point2D, Boolean>.toStringBottomLeft() =
    this.keys.toStringWithOrientation(orientationTopLeft = false)

fun Map<Point2D, Boolean>.printBottomLeft() =
    println(this.keys.toSet().toStringWithOrientation(orientationTopLeft = false))

fun Set<Point2D>.toStringTopLeft() =
    toStringWithOrientation(orientationTopLeft = true)

fun Set<Point2D>.printTopLeft() =
    println(toStringWithOrientation(orientationTopLeft = true))

fun Set<Point2D>.toStringBottomLeft() =
    toStringWithOrientation(orientationTopLeft = false)

fun Set<Point2D>.printBottomLeft() =
    println(toStringWithOrientation(orientationTopLeft = false))

fun Set<Point2D>.toStringWithOrientation(orientationTopLeft: Boolean = true): String {
    val yMin = this.minOf { it.y }
    val yMax = this.maxOf { it.y }

    val yProg: IntProgression
    if (orientationTopLeft == true) {
        yProg = yMin..yMax
    } else {
        yProg = yMax downTo yMin
    }
    return toPrintableString(yProg)
}

fun Set<Point2D>.toPrintableString(yProg: IntProgression): String {
    val xMin = this.minOf { it.x }
    val xMax = this.maxOf { it.x }

    return buildString {
        yProg.forEach { y ->
            for (x in xMin..xMax) {
                append(if (contains(Point2D(x, y))) '#' else '.')
            }
            appendLine()
        }
    }.removeSuffix(System.lineSeparator())
}

fun Map<Point2D, Boolean>.printExisting() =
    println(toPrintableStringExisting())

fun Map<Point2D, Boolean>.toPrintableStringExisting(nonExistingChar: Char = ' '): String {
    val points = keys.toList()
    val rows = points.maxOf { it.y }
    val columns = points.maxOf { it.x }

    return buildString {
        for (y in 0..rows) {
            for (x in 0..columns) {
                if (this@toPrintableStringExisting.containsKey(Point2D(x, y))) {
                    append(if (getValue(Point2D(x, y))) '#' else '.')
                } else {
                    append(nonExistingChar)
                }
            }
            appendLine()
        }
    }.removeSuffix(System.lineSeparator())
}

fun Array<CharArray>.printBottomLeft() =
    println(toPrintableStringBottomLeft())

fun Array<CharArray>.toPrintableStringBottomLeft(): String =
    buildString {
        for (y in this@toPrintableStringBottomLeft.indices) {
            for (x in this@toPrintableStringBottomLeft.first().indices) {
                append(this@toPrintableStringBottomLeft[y][x])
            }
            appendLine()
        }
    }.removeSuffix(System.lineSeparator())
