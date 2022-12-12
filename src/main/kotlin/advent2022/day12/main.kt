package advent2022.day12

import Graph
import Input.readInput
import advent2022.day9.Point
import arrayForEach
import getAdjacent
import prettyPrint

fun main() {
    val (graph, start, end) = parseInput()
    println(first(graph, start, end))
    println(optimizedSecond(graph, start, end))
}


fun parseInput(): Triple<Graph<Int>, Point, Point> {
    val input = readInput()
    val graph = Array(input.size) { n -> Array(input[0].length) { input[n][it] - 'a' } }
    var start = 0 to 0
    var end = 0 to 0
    graph.arrayForEach { (x , y) ->
        if (input[x][y] == 'S') start = x to y
        if (input[x][y] == 'E') end = x to y
    }
    graph[start.first][start.second] = 'a' - 'a'
    graph[end.first][end.second] = 'z' - 'a'
    return Triple(graph, start, end)
}

fun MutableList<Pair<Point, Int>>.score(point: Point): Int = find { it.first == point }?.second ?: Int.MAX_VALUE
fun MutableList<Pair<Point, Int>>.visit(point: Point, steps: Int) = run {
    val idx = this.indexOfFirst { it.first == point }
    if (idx == -1) this.add(point to steps)
    else this[idx] = point to steps
}

fun first(graph: Graph<Int>, start: Point, end: Point): Int = run {
    val prev = graph[start.first][start.second]
    graph[start.first][start.second] = 0
    val queue = mutableListOf(start to 0)
    val visited = mutableListOf(start to 0)
    while(queue.isNotEmpty()) {
        queue.sortBy { it.second }
        val (next, steps) = queue.removeFirst()
        if (next == end) return steps
        val currentHeight = graph[next.first][next.second]
        graph
            .getAdjacent(next)
            .sortedByDescending { graph[it.first][it.second] }
            .forEach { (x, y) ->
                if (graph[x][y] <= currentHeight + 1 && steps + 1 < visited.score(x to y)) {
                    queue.add((x to y) to steps + 1)
                    visited.visit((x to y), steps + 1)
                }
            }
    }
    graph[start.first][start.second] = prev
    Int.MAX_VALUE
}

fun second(graph: Graph<Int>, end: Point): Int = run {
    val points = mutableListOf(Int.MAX_VALUE)
    graph.arrayForEach { start -> if (start != end) points.add(first(graph, start, end)) }
    points.min()
}

fun optimizedSecond(graph: Graph<Int>, start: Point, end: Point): Int = run {
    val queue = mutableListOf(start to 0)
    val visited = mutableListOf(start to 0)
    graph.arrayForEach { (x, y) ->
        if (graph[x][y] == 0) {
            queue.add((x to y) to 0)
            visited.add((x to y) to 0)
        }
    }
    while(queue.isNotEmpty()) {
        queue.sortBy { it.second }
        val (next, steps) = queue.removeFirst()
        if (next == end) return steps
        val currentHeight = graph[next.first][next.second]
        graph
            .getAdjacent(next)
            .sortedByDescending { graph[it.first][it.second] }
            .forEach { (x, y) ->
                if (graph[x][y] <= currentHeight + 1 && steps + 1 < visited.score(x to y)) {
                    queue.add((x to y) to steps + 1)
                    visited.visit((x to y), steps + 1)
                }
            }
    }
    Int.MAX_VALUE
}
