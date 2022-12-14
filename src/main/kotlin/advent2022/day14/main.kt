package advent2022.day14

import Graph
import Input.readInput
import Point
import splitTwo
import kotlin.math.max


fun main() {
    println(first())
    println(second())
}

var maxDown = 0

fun parseInput(): Graph<Int> {
    val graph = Array(500) { Array(1000) { 0 } }
    readInput().forEach {line ->
        line.split(" -> ").zipWithNext().forEach { (prev, next) ->
            val (y1, x1) = prev.splitTwo(",") { it.toInt() }
            val (y2, x2) = next.splitTwo(",") { it.toInt() }
            val (startX, endX) = minOf(x1, x2) to maxOf(x1, x2)
            val (startY, endY) = minOf(y1, y2) to maxOf(y1, y2)
            for (x in startX..endX) {
                maxDown = max(x, maxDown)
                for (y in startY..endY) {
                    graph[x][y] = 1
                }
            }
        }
    }
    return graph
}

fun Point.moveSand(graph: Graph<Int>): Point {
    if (graph[first + 1][second] == 0) return first + 1 to second
    if (graph[first + 1][second - 1] == 0) return first + 1 to second - 1
    if (graph[first + 1][second + 1] == 0) return first + 1 to second + 1
    return this
}

fun first(): Int = run {
    val graph = parseInput()
    val start = 0 to 500
    var result = 0

    while(true) {
        var current = start
        var next = current.moveSand(graph)
        while (next != current) {
            current = next
            next = next.moveSand(graph)
            if (next.first > maxDown) break
        }
        if (next.first > maxDown) break
        graph[current.first][current.second] = 1
        result++
    }

    result
}

fun second(): Int = run {
    val graph = parseInput()
    for (y in 0..999) {
        graph[maxDown + 2][y] = 1
    }
    val start = 0 to 500
    var result = 0

    while(true) {
        var current = start
        var next = current.moveSand(graph)
        while (next != current) {
            current = next
            next = next.moveSand(graph)
        }
        graph[current.first][current.second] = 1
        result++
        if (current == start) break
    }

    result
}