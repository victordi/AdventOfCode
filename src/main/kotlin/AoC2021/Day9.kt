package AoC2021.day9

import Input.withInput
import getAdjacent

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput {
        val list = it.map { it.toCharArray().map { it.digitToInt() } }.toList()
        val matrix = Array(list.size) {
            list[it].toTypedArray()
        }
        val n = matrix.size - 1
        val m = matrix[0].size - 1
        for (i in 0..n) {
            for (j in 0..m) {
                val lowPoint = matrix.getAdjacent(i, j).fold(true) { acc, (a, b) ->
                    acc && matrix[i][j] < matrix[a][b]
                }
                if (lowPoint) {
                    result += matrix[i][j] + 1
                }
            }
        }
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput {
        val list = it.map { it.toCharArray().map { it.digitToInt() } }.toList()
        val matrix = Array(list.size) { idx ->
            list[idx].toTypedArray()
        }
        val basins = mutableListOf<Int>()
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                val lowPoint = matrix.getAdjacent(i, j).fold(true) { acc, (a, b) ->
                    acc && matrix[i][j] < matrix[a][b]
                }
                if (lowPoint) {
                    val seen = mutableSetOf<Pair<Int, Int>>()
                    val queue = mutableListOf(i to j)
                    while (queue.size > 0) {
                        val (x, y) = queue.removeFirst()

                        seen.add(x to y)
                        matrix.getAdjacent(x, y)
                            .filter { it !in seen }
                            .filter { (a, b) -> matrix[a][b] != 9 }
                            .forEach { queue.add(it) }
                    }
                    basins.add(seen.size)
                }
            }
        }
        basins.sortDescending()
        result = basins[0] * basins[1] * basins[2]
    }
    return result
}