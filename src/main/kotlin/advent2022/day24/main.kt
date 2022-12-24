package advent2022.day24

import Graph
import Point
import arrayForEach
import java.util.*

val lines = Input.readInput()
val n = lines.size
val m = lines[0].length
val map: Graph<MutableList<Char>> = Array(n) { lines[it].toCharArray().map { mutableListOf(it) }.toTypedArray() }
val end = n - 1 to lines.last().indexOf('.')

fun main() {
    computeMaps()
    println(first())
    println(second())
}

typealias State = Pair<Point, Int>

fun moveBlizzards() {
    val toDoSteps: MutableList<Triple<Point, Point, Char>> = mutableListOf()
    map.arrayForEach { (x, y) ->
        map[x][y].forEach { c ->
            if (c in listOf('<', '>', 'v', '^')) {
                val (newX, newY) = when (c) {
                    '>' -> {
                        var yy = y + 1
                        if (yy == m - 1) yy = 1
                        x to yy
                    }

                    '<' -> {
                        var yy = y - 1
                        if (yy == 0) yy = m - 2
                        x to yy
                    }

                    '^' -> {
                        var xx = x - 1
                        if (xx == 0) xx = n - 2
                        xx to y
                    }

                    'v' -> {
                        var xx = x + 1
                        if (xx == n - 1) xx = 1
                        xx to y
                    }
                    else -> throw IllegalArgumentException("Can't get here")
                }
                toDoSteps.add(Triple(x to y, newX to newY, c))
            }
        }
    }
    toDoSteps.forEach { (p1, p2, c) ->
        if (c == '.') println("WTF")
        val (x, y) = p1
        val (newX, newY) = p2
        map[x][y].remove(c)
        if (map[x][y].isEmpty()) map[x][y].add('.')
        map[newX][newY].add(c)
    }
}

fun Point.getAvailableSteps(map: Graph<MutableList<Char>>): List<Point> {
    val result = mutableListOf<Point>()
    if (map[first][second] == mutableListOf('.')) result.add(first to second)
    if (first - 1 >= 0 && map[first - 1][second] == mutableListOf('.')) result.add(first - 1 to second)
    if (first + 1 < n && map[first + 1][second] == mutableListOf('.')) result.add(first + 1 to second)
    if (second - 1 >= 0 && map[first][second - 1] == mutableListOf('.')) result.add(first to second - 1)
    if (second + 1 < m && map[first][second + 1] == mutableListOf('.')) result.add(first to second + 1)
    return result.toList()
}

val mapsByTurn = mutableMapOf<Int, Graph<MutableList<Char>>>()
fun computeMaps() {
    repeat(1000) {
       mapsByTurn[it] = Array(n) { i -> Array(m) { j -> map[i][j].toList().toMutableList() } }
       moveBlizzards()
    }
}

fun travel(start: Point, end: Point, startTurn: Int = 1): Int = run {
    val queue = LinkedList<State>()
    val visited = mutableSetOf<State>()

    queue.add(start to startTurn)
    while(queue.isNotEmpty()) {
        val (point, turn) = queue.pop()
        if (point to turn in visited) continue
        visited.add(point to turn)
        if (point == end) return turn - 1
        point.getAvailableSteps(mapsByTurn[turn]!!).forEach { queue.add(it to turn + 1) }
    }
    -1
}

fun first(): Int = travel(Point(0, 1), end)
fun second(): Int = travel(Point(0, 1), end, travel(end, Point(0, 1), travel(Point(0, 1), end)))