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
    val treeHeight = treeMap.trees.getValue(this)

    val upVisible = treeMap.upTrees(this).all { treeMap.trees.getValue(it) < treeHeight }
    val downVisible = treeMap.downTrees(this).all { treeMap.trees.getValue(it) < treeHeight }
    val leftVisible = treeMap.leftTrees(this).all { treeMap.trees.getValue(it) < treeHeight }
    val rightVisible = treeMap.rightTrees(this).all { treeMap.trees.getValue(it) < treeHeight }
    return upVisible || downVisible || leftVisible || rightVisible
}

fun Tree.scenicScore(treeMap: TreeMap): Int {
    val treeHeight = treeMap.trees.getValue(this)

    val upTrees = treeMap.upTrees(this)
    val downTrees = treeMap.downTrees(this)
    val leftTrees = treeMap.leftTrees(this)
    val rightTrees = treeMap.rightTrees(this)

    var upVisible = upTrees.windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    upVisible = if (upVisible == -1) upTrees.size else upVisible + 1
    var downVisible = downTrees.windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    downVisible = if (downVisible == -1) downTrees.size else downVisible + 1
    var leftVisible = leftTrees.windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    leftVisible = if (leftVisible == -1) leftTrees.size else leftVisible + 1
    var rightVisible = rightTrees.windowed(1).indexOfFirst { treeMap.trees.getValue(it[0]) >= treeHeight }
    rightVisible = if (rightVisible == -1) rightTrees.size else rightVisible + 1

    return upVisible * downVisible * leftVisible * rightVisible
}
