/**
 * Advent of Code 2022, Day 8: Treetop Tree House
 * Problem Description: https://adventofcode.com/2022/day/8
 */

import Resources.resourceAsList

fun main() {
    fun part1(input: TreeMap): Int =
        input.trees.keys.map { tree ->
            tree visibleIn input
        }.count { it }

    fun part2(input: TreeMap): Int {
        val scores = input.trees.keys.map { tree ->
            tree.scenicScore(input)
        }
        return scores.max()
    }

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsList(fileName = "${name}_test").toPointMap()
    val puzzleInput = resourceAsList(name).toPointMap()

    check(part1(testInput) == 21)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 1_820)

    check(part2(testInput) == 8)
    println(part2(puzzleInput))
    check(part2(puzzleInput) == 385_112)
}

fun List<String>.toPointMap(): TreeMap =
    TreeMap(this.flatMapIndexed { row, line ->
        line.mapIndexed { col, tree ->
            Point2D(col, row) to tree.digitToInt()
        }
    }.toMap())

typealias Tree = Point2D

data class TreeMap(val trees: Map<Tree, Int>) {
    val points = trees.keys.toList()
    val rows = points.maxOf { it.y }
    val columns = points.maxOf { it.x }

    fun upTrees(tree: Tree) = (tree lineTo Point2D(tree.x, 0)) - tree
    fun downTrees(tree: Tree) = (tree lineTo Point2D(tree.x, columns)) - tree
    fun leftTrees(tree: Tree) = (tree lineTo Point2D(0, tree.y)) - tree
    fun rightTrees(tree: Tree) = (tree lineTo Point2D(rows, tree.y)) - tree
}

infix fun Tree.visibleIn(treeMap: TreeMap): Boolean {
    fun visibleTrees(trees: List<Tree>, maxHeight: Int) =
        trees.all { treeMap.trees.getValue(it) < maxHeight }

    val treeHeight = treeMap.trees.getValue(this)

    return listOf(treeMap.upTrees(this), treeMap.downTrees(this), treeMap.leftTrees(this), treeMap.rightTrees(this))
        .map { trees ->
            visibleTrees(trees, treeHeight)
        }.any { it }
}

fun Tree.scenicScore(treeMap: TreeMap): Int {
    val treeHeight = treeMap.trees.getValue(this)

    return listOf(treeMap.upTrees(this), treeMap.downTrees(this), treeMap.leftTrees(this), treeMap.rightTrees(this))
        .map { trees ->
            trees.takeUntil { treeMap.trees.getValue(it) >= treeHeight }.size
        }.product()
}
