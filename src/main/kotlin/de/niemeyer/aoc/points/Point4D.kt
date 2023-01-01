@file:Suppress("unused")

package de.niemeyer.aoc.points

import java.util.*

data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) : Point {
    override val neighbors: List<Point4D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).flatMap { dy ->
                (z - 1..z + 1).flatMap { dz ->
                    (w - 1..w + 1).mapNotNull { dw ->
                        Point4D(dx, dy, dz, dw).takeUnless { it == this }
                    }
                }
            }
        }
    }
}
