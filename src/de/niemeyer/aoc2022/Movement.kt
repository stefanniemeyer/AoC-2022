/*
 * Most of the code comes from Todd Ginsberg
 */

package de.niemeyer.aoc2022

import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.math.max

sealed class Direction {
    abstract val turnLeft: Direction
    abstract val turnRight: Direction
    abstract val offset: Point2D

    operator fun invoke(dir: String): Direction =
        when (dir) {
            "N", "U" -> North
            "S", "D" -> South
            "E", "R" -> East
            "W", "L" -> West
            "forward" -> East
            "up" -> North
            "down" -> South
            else -> throw IllegalArgumentException("No such direction $dir")
        }

    object North : Direction() {
        override val turnLeft = West
        override val turnRight = East
        override val offset = Point2D(0, 1)
    }

    object South : Direction() {
        override val turnLeft = East
        override val turnRight = West
        override val offset = Point2D(0, -1)
    }

    object West : Direction() {
        override val turnLeft = South
        override val turnRight = North
        override val offset = Point2D(-1, 0)
    }

    object East : Direction() {
        override val turnLeft = North
        override val turnRight = South
        override val offset = Point2D(1, 0)
    }
}

fun Char.toDirection(): Direction =
    when (this) {
        in listOf('N', 'U') -> Direction.North
        in listOf('S', 'D') -> Direction.South
        in listOf('W', 'L') -> Direction.West
        in listOf('E', 'R') -> Direction.East
        else -> throw IllegalArgumentException("No such direction $this")
    }

data class Point2D(val x: Int, val y: Int) : Point {
    override val neighbors: List<Point2D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).mapNotNull { dy ->
                Point2D(dx, dy).takeUnless { it == this }
            }
        }
    }

    val axisNeighbors: List<Point2D> by lazy {
        neighbors.filter { it.sharesAxisWith(this) }
    }

    operator fun plus(other: Point2D): Point2D =
        Point2D(x + other.x, y + other.y)

    operator fun times(by: Int): Point2D =
        Point2D(x * by, y * by)

    fun move(direction: Direction): Point2D =
        this.moveTimes(direction, 1)

    fun moveTimes(direction: Direction, offset: Int): Point2D =
        when (direction) {
            Direction.North -> Point2D(x, y + offset)
            Direction.South -> Point2D(x, y - offset)
            Direction.East -> Point2D(x + offset, y)
            Direction.West -> Point2D(x - offset, y)
        }

    // calculate Manhattan distance between two points
    // https://de.wikipedia.org/wiki/Manhattan-Metrik
    infix fun manhattanDistanceTo(other: Point2D): Int =
        (x - other.x).absoluteValue + (y - other.y).absoluteValue

    // calculate Chebyshev's chessboard distance
    // https://en.wikipedia.org/wiki/Chebyshev_distance
    infix fun chebyshevDistanceTo(other: Point2D): Int =
        max((x - other.x).absoluteValue, (y - other.y).absoluteValue)

    fun rotateLeft(): Point2D =
        Point2D(x = y * -1, y = x)

    fun rotateRight(): Point2D =
        Point2D(x = y, y = x * -1)

    infix fun sharesAxisWith(that: Point2D): Boolean =
        x == that.x || y == that.y

    infix fun lineTo(that: Point2D): List<Point2D> {
        val xDelta = (that.x - x).sign
        val yDelta = (that.y - y).sign
        val steps = maxOf((x - that.x).absoluteValue, (y - that.y).absoluteValue)
        return (1..steps).scan(this) { last, _ -> Point2D(last.x + xDelta, last.y + yDelta) }
    }

    companion object {
        val ORIGIN = Point2D(0, 0)

        fun of(input: String, delimiter: String = ","): Point2D =
            Point2D(input.substringBefore(delimiter).toInt(), input.substringAfter(delimiter).toInt())
    }
}

//fun Map<Point2D, Boolean>.print() {
//    val points = keys.toList()
//    val rows = points.maxOf { it.y }
//    val columns = points.maxOf { it.x }
//
//    for (y in 0..rows) {
//        for (x in 0..columns) {
//            print(if (this.getOrDefault(Point2D(x, y), false)) '#' else '.')
//        }
//        println()
//    }
//}

fun MutableMap<Point2D, Char>.print() {
    val points = keys.toList()
    val left = points.minOf { it.x }
    val top = points.minOf { it.y }
    val rows = points.maxOf { it.y }
    val columns = points.maxOf { it.x }

    for (y in top..rows) {
        for (x in left..columns) {
            print(this.getOrDefault(Point2D(x, y), '.'))
        }
        println()
    }
}

fun Set<Point2D>.print() {
    val xMin = this.minOf { it.x }
    val xMax = this.maxOf { it.x }
    val yMin = this.minOf { it.y }
    val yMax = this.maxOf { it.y }

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            print(if (this.contains(Point2D(x, y))) '#' else '.')
        }
        println()
    }
}

interface Point {
    val neighbors: List<Point>
}

data class Point3D(val x: Int, val y: Int, val z: Int) : Point {
    override val neighbors: List<Point3D> by lazy {
        (x - 1..x + 1).flatMap { dx ->
            (y - 1..y + 1).flatMap { dy ->
                (z - 1..z + 1).mapNotNull { dz ->
                    Point3D(dx, dy, dz).takeUnless { it == this }
                }
            }
        }
    }

    val hexNeighbors: List<Point3D> by lazy {
        HEX_OFFSETS.map { this + it.value }
    }

    infix fun sharesAxisWith(that: Point3D): Boolean =
        x == that.x || y == that.y || z == that.z

    val axisNeighbors: List<Point3D> by lazy {
        neighbors.filter { it.sharesAxisWith(this) }
    }

    operator fun plus(other: Point3D): Point3D =
        Point3D(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point3D): Point3D =
        Point3D(x - other.x, y - other.y, z - other.z)

    fun rotate(d: Int): Point3D {
        val c0 = d % 3
        val c0s = 1 - ((d / 3) % 2) * 2
        val c1 = (c0 + 1 + (d / 6) % 2) % 3
        val c1s = 1 - (d / 12) * 2
        val c2 = 3 - c0 - c1
        val c2s = c0s * c1s * (if (c1 == (c0 + 1) % 3) 1 else -1)
        val tp = listOf(this.x, this.y, this.z)
        return Point3D(tp[c0] * c0s, tp[c1] * c1s, tp[c2] * c2s)
    }

    infix fun distanceTo(other: Point3D): Int =
        (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue + (this.z - other.z).absoluteValue

    fun hexNeighbor(dir: String): Point3D =
        if (dir in HEX_OFFSETS) HEX_OFFSETS.getValue(dir) + this
        else throw IllegalArgumentException("No dir: $dir")

    companion object {
        val ORIGIN = Point3D(0, 0, 0)
        val HEX_OFFSETS = mapOf(
            "e" to Point3D(1, -1, 0),
            "w" to Point3D(-1, 1, 0),
            "ne" to Point3D(1, 0, -1),
            "nw" to Point3D(0, 1, -1),
            "se" to Point3D(0, -1, 1),
            "sw" to Point3D(-1, 0, 1),
        )

        fun of(input: String, delimiter: String = ","): Point3D {
            val (x, y, z) = input.split(delimiter).map { it.toInt() }
            return Point3D(x, y, z)
        }
    }
}

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

fun dijkstraDE(graph: Map<String, Map<String, Int>>, start: String, goal: String): Map<String, Int> {
    // Entfernungen zu allen Knoten initialisieren
    val distances = graph.keys.associate { it to Int.MAX_VALUE }.toMutableMap()
    distances[start] = 0

    // Set mit unbesuchten Knoten
    val unvisited = distances.keys.toMutableSet()

    // Vorgänger-Knoten für jeden Knoten
    val previous = mutableMapOf<String, String>()

    // Aktuellen Knoten festlegen
    var current = start

    // Solange es unbesuchte Knoten gibt...
    while (unvisited.isNotEmpty()) {
        // Entfernungen zu Nachbarknoten aktualisieren
        graph[current]?.forEach { (neighbor, distance) ->
            val newDistance = distances[current]!! + distance
            if (newDistance < distances[neighbor]!!) {
                distances[neighbor] = newDistance
                previous[neighbor] = current
            }
        }

        // Aktuellen Knoten als besucht markieren
        unvisited.remove(current)

        // Nächsten unbesuchten Knoten mit minimaler Entfernung auswählen
        current = unvisited.minBy { distances[it]!! }!!
    }

    return distances
}

class Vertex(val name: String) {
    val neighbors = mutableListOf<Edge>()

    override fun toString() = name
}

class Edge(val neighbor: Vertex, val weight: Int)

fun dijkstra(graph: Map<Vertex, List<Edge>>, source: Vertex, target: Vertex): Map<Vertex, Int> {
    val dist = mutableMapOf<Vertex, Int>()
    val predecessor = mutableMapOf<Vertex, Vertex?>()
    val visited = mutableSetOf<Vertex>()
    val queue = PriorityQueue<Pair<Vertex, Int>>(compareBy { it.second })

    // Initialize distances
    for (vertex in graph.keys) {
        dist[vertex] = if (vertex == source) 0 else Int.MAX_VALUE
        predecessor[vertex] = null
    }

    queue.add(source to 0)

    while (queue.isNotEmpty()) {
        val (neighbor, distance) = queue.poll()
        if (neighbor in visited) continue
        visited.add(neighbor)
        for (edge in graph[neighbor]!!) {
            val alt = distance + edge.weight
            if (alt < dist[edge.neighbor]!!) {
                dist[edge.neighbor] = alt
                predecessor[edge.neighbor] = neighbor
                queue.add(edge.neighbor to alt)
            }
        }
    }
//    printShortestPath(predecessor, source, target)
    return dist
}

fun printShortestPath(predecessor: Map<Vertex, Vertex?>, source: Vertex, target: Vertex) {
    val path = mutableListOf<Vertex>()
    var u: Vertex? = target
    while (u != null) {
        path.add(u)
        u = predecessor[u]
    }
    path.reverse()
    println("Shortest path from $source to $target: ${path.joinToString(" -> ")}")
}


