@file:Suppress("unused")

package de.niemeyer.aoc.search

import java.util.*

class Vertex(val name: String) {
    val neighbors = mutableListOf<Edge>()

    override fun toString() = name
}

class Edge(val neighbor: Vertex, val weight: Int)

fun dijkstra(graph: Map<Vertex, List<Edge>>, source: Vertex /*, target: Vertex? = null */): Map<Vertex, Int> {
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
