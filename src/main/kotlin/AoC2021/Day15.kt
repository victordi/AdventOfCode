package AoC2021.day15

import utils.Graph
import utils.Input.withInput
import utils.getAdjacent

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun Graph<Int>.dijkstra(x: Int, y: Int): Graph<Int> {
    val dist = Array(size) { Array(size) { Int.MAX_VALUE } }
    dist[x][y] = 0
    val priorityQueue: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()
    priorityQueue.add(x to y to 0)
    while (priorityQueue.isNotEmpty()) {
        priorityQueue.sortBy { it.second }
        val (pair, weight) = priorityQueue.first()
        val (currentX, currentY) = pair
        priorityQueue.removeAt(0)
        this.getAdjacent(currentX, currentY).forEach { (nextX, nextY) ->
            if (dist[nextX][nextY] > this[nextX][nextY] + weight) {
                dist[nextX][nextY] = this[nextX][nextY] + weight
                priorityQueue.add(nextX to nextY to dist[nextX][nextY])
            }
        }
    }
    return dist
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.map {it.toCharArray().map { it.digitToInt() }.toTypedArray()}.toList().toTypedArray()
        val distances = list.dijkstra(0, 0)
        result = distances[distances.size - 1][distances[0].size - 1]
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput { input ->
        val list = input.map {it.toCharArray().map { it.digitToInt() }}.toList()
        val n = list.size
        val graph = Array(n * 5) { Array(n * 5) { 0 } }
        for (idx in 0 until 5) {
            for (idy in 0 until 5) {
                for (i in list.indices) {
                    for (j in list[0].indices) {
                        val newValue = list[i][j] + idx + idy
                        graph[i + idx * n][j + idy * n] = if (newValue > 9) newValue % 10 + 1 else newValue
                    }
                }
            }
        }
        val distances = graph.dijkstra(0, 0)
        result = distances[distances.size - 1][distances[0].size - 1]
    }
    return result
}