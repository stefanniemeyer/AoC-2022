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

    val upVisible = visibleTrees(treeMap.upTrees(this), treeHeight)
    val downVisible = visibleTrees(treeMap.downTrees(this), treeHeight)
    val leftVisible = visibleTrees(treeMap.leftTrees(this), treeHeight)
    val rightVisible = visibleTrees(treeMap.rightTrees(this), treeHeight)

    return upVisible || downVisible || leftVisible || rightVisible
}

fun Tree.scenicScore(treeMap: TreeMap): Int {
    val treeHeight = treeMap.trees.getValue(this)

    var upVisible = treeMap.upTrees(this).windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    upVisible = if (upVisible == -1) treeMap.upTrees(this).size else upVisible + 1
    var downVisible = treeMap.downTrees(this).windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    downVisible = if (downVisible == -1) treeMap.downTrees(this).size else downVisible + 1
    var leftVisible = treeMap.leftTrees(this).windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    leftVisible = if (leftVisible == -1) treeMap.leftTrees(this).size else leftVisible + 1
    var rightVisible = treeMap.rightTrees(this).windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    rightVisible = if (rightVisible == -1) treeMap.rightTrees(this).size else rightVisible + 1

    return upVisible * downVisible * leftVisible * rightVisible
}
