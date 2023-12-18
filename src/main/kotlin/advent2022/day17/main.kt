package advent2022.day17

import utils.Input
import utils.Point

val moves = Input.readInput().first().toCharArray().toList()

sealed class Shape {
    abstract fun getPoints(middle: Point): List<Point>
    abstract fun spawn(top: Int): Point
    abstract fun next(): Shape
    abstract fun top(middle: Point): Int
}
object One: Shape() {
    override fun getPoints(middle: Point): List<Point> = (0..3).map { middle.first to middle.second + it }
    override fun spawn(top: Int): Point = (top + 4) to 2
    override fun next(): Shape = Two
    override fun top(middle: Point): Int = middle.first
}
object Two: Shape() {
    override fun getPoints(middle: Point): List<Point> = middle.let { (x, y) -> listOf((x - 1) to y, (x + 1) to y, x to (y - 1), x to (y + 1))}
    override fun spawn(top: Int): Point = (top + 5) to 3
    override fun next(): Shape = Three
    override fun top(middle: Point): Int = middle.first + 1
}
object Three: Shape() {
    override fun getPoints(middle: Point): List<Point> = middle.let { (x, y) -> listOf(
        (x - 1) to (y - 1), (x - 1) to y, (x - 1) to (y + 1),
        x to (y + 1), (x + 1) to (y + 1))
    }
    override fun spawn(top: Int): Point = (top + 5) to 3
    override fun next(): Shape = Four
    override fun top(middle: Point): Int = middle.first + 1
}
object Four: Shape() {
    override fun getPoints(middle: Point): List<Point> = (0..3).map { (middle.first + it) to middle.second}
    override fun spawn(top: Int): Point = (top + 4) to 2
    override fun next(): Shape = Five
    override fun top(middle: Point): Int = middle.first + 3
}
object Five: Shape() {
    override fun getPoints(middle: Point): List<Point> = middle.let { (x, y) -> listOf(x to y, x to y + 1, x + 1 to y, x + 1 to y + 1) }
    override fun spawn(top: Int): Point = (top + 4) to 2
    override fun next(): Shape = One
    override fun top(middle: Point): Int = middle.first + 1
}

fun main() {
    println(first())
    println(second())
}

fun first(): Int = run {
    var top = 0
    val visited = mutableSetOf<Point>()
    (0..6).forEach { visited += 0 to it }
    var current: Shape = One
    var directionIdx = 0
    (1..2022).forEach {
        var (x, y) = current.spawn(top)
        while(true) {
            val dir = moves[directionIdx]
            directionIdx = (directionIdx + 1) % moves.size
            if (dir == '>') {
                if (current.getPoints(x to y + 1).find { it.second > 6 || it in visited } == null) {
                    y += 1
                }
            } else {
                if (current.getPoints(x to y - 1).find { it.second < 0 || it in visited } == null) {
                    y -= 1
                }
            }
            if (current.getPoints(x - 1 to y).find { it in visited } == null) {
                x -= 1
            } else {
                break
            }
        }
        top = maxOf(top, current.top(x to y))
        current.getPoints(x to y).forEach { visited += it }
        current = current.next()
    }
    top
}

data class State(val piece: Shape, val idx: Int, val prevRows: Set<Point>)

fun second(): Long = run {
    var top = 0L
    val visited = mutableSetOf<Point>()
    (0..6).forEach { visited += 0 to it }
    var current: Shape = One
    var directionIdx = 0
    val prevStates = mutableSetOf<State>()
    var cycleIdx = 0L
    val prevRows = 3000
    lateinit var cycleState: State
    for (i in (1..1_000_000_000_000)) {
        var (x, y) = current.spawn(top.toInt())
        while(true) {
            val dir = moves[directionIdx]
            directionIdx = (directionIdx + 1) % moves.size
            if (dir == '>') {
                if (current.getPoints(x to y + 1).find { it.second > 6 || it in visited } == null) {
                    y += 1
                }
            } else {
                if (current.getPoints(x to y - 1).find { it.second < 0 || it in visited } == null) {
                    y -= 1
                }
            }
            if (current.getPoints(x - 1 to y).find { it in visited } == null) {
                x -= 1
            } else {
                break
            }
        }

        top = maxOf(top, current.top(x to y).toLong())
        current.getPoints(x to y).forEach { visited += it }
        current = current.next()

        val rows = visited.filter { it.first >= top - prevRows }.map { top.toInt() - it.first to it.second }.toSet()
        val state = State(current, directionIdx, rows)
        if (state in prevStates) {
            cycleIdx = i
            cycleState = state
            break
        }
        prevStates += state
    }
    val repeats = 1_000_000_000_000L / cycleIdx
    val remainders = 1_000_000_000_000L % cycleIdx
    current = cycleState.piece
    directionIdx = cycleState.idx
    val newVisited = cycleState.prevRows.toMutableSet()
    var newTop = prevRows.toLong()
    for (i in 1..remainders) {
        var (x, y) = current.spawn(top.toInt())
        while(true) {
            val dir = moves[directionIdx]
            directionIdx = (directionIdx + 1) % moves.size
            if (dir == '>') {
                if (current.getPoints(x to y + 1).find { it.second > 6 || it in newVisited } == null) {
                    y += 1
                }
            } else {
                if (current.getPoints(x to y - 1).find { it.second < 0 || it in newVisited } == null) {
                    y -= 1
                }
            }
            if (current.getPoints(x - 1 to y).find { it in newVisited } == null) {
                x -= 1
            } else {
                break
            }
        }

        newTop = maxOf(newTop, current.top(x to y).toLong())
        current.getPoints(x to y).forEach { newVisited += it }
        current = current.next()
    }
    top * repeats + (newTop - prevRows)
}