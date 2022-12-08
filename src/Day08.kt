import Resources.resourceAsList

fun main() {
    fun part1(input: TreeMap): Int {
        val visi = input.keys.map { tree ->
            tree visibleIn input
        }.count { it }

        return visi
    }

    fun part2(input: TreeMap): Int =
        TODO()

    var name = Throwable().stackTrace.first { it.className.contains("Day") }.className.split(".")[0]
    name = name.removeSuffix("Kt")

    val testInput = resourceAsList(fileName = "${name}_test").toPointMap()
    val puzzleInput = resourceAsList(name).toPointMap()

    check(part1(testInput) == 21)
    println(part1(puzzleInput))
    check(part1(puzzleInput) == 1_820)

//    check(part2(testInput) == 0)
//    check(part2(puzzleInput) == 0)

//    println(part2(puzzleInput))
}

fun List<String>.toPointMap(): TreeMap =
    this.flatMapIndexed { row, line ->
        line.mapIndexed { col, tree ->
            Point2D(col, row) to tree.digitToInt()
        }
    }.toMap()

typealias TreeMap = Map<Point2D, Int>
typealias Tree = Point2D

infix fun Tree.visibleIn(trees: TreeMap): Boolean {
    val points = trees.keys.toList()
    val rows = points.maxOf { it.y }
    val columns = points.maxOf { it.x }
    val treeHeight = trees.getValue(this)

    val upTrees = (this lineTo Point2D(this.x, 0)) - this
    val downTrees = (this lineTo Point2D(this.x, columns)) - this
    val leftTrees = (this lineTo Point2D(0, this.y)) - this
    val rightTrees = (this lineTo Point2D(rows, this.y)) - this
    val upVisible = upTrees.all { trees.getValue(it) < treeHeight }
    val downVisible = downTrees.all { trees.getValue(it) < treeHeight }
    val leftVisible = leftTrees.all { trees.getValue(it) < treeHeight }
    val rightVisible = rightTrees.all { trees.getValue(it) < treeHeight }
    return upVisible || downVisible || leftVisible || rightVisible
}
