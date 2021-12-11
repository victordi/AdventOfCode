package AoC2021

import Input.withInput
import getAdjacentWithCorners
import java.util.*

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Long {
    var result = 0L
    withInput { input ->
        val list = input.map { it.toCharArray().map { it.digitToInt() }}.toList()
        val matrix = Array(10) {
            list[it].toTypedArray()
        }
        for (step in 0..99) {
            val visited = mutableSetOf<Pair<Int,Int>>()
            for (i in matrix.indices) {
                for (j in matrix.indices) {
                    matrix[i][j] += 1
                    if (matrix[i][j] > 9 && i to j !in visited) {
                        val stack = LinkedList<Pair<Int,Int>>()
                        visited.add(i to j)
                        stack.add(i to j)
                        while (stack.size > 0) {
                            val (x, y) = stack.pop()
                            for ((a, b) in matrix.getAdjacentWithCorners(x, y)) {
                                matrix[a][b] += 1
                                if (matrix[a][b] > 9 && a to b !in visited) {
                                    visited.add(a to b)
                                    stack.add(a to b)
                                }
                            }
                        }
                    }
                }
            }
            for (i in matrix.indices) {
                for (j in matrix.indices) {
                    if (matrix[i][j] > 9) {
                        matrix[i][j] = 0
                        result++
                    }
                }
            }
        }

    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val list = input.map { it.toCharArray().map { it.digitToInt() }}.toList()
        val matrix = Array(10) {
            list[it].toTypedArray()
        }
        var bool = true
        var step = 0L
        while(bool) {
            step++
            val visited = mutableSetOf<Pair<Int,Int>>()
            for (i in matrix.indices) {
                for (j in matrix.indices) {
                    matrix[i][j]++
                    if (matrix[i][j] > 9 && i to j !in visited) {
                        val stack = LinkedList<Pair<Int,Int>>()
                        stack.add(i to j)
                        visited.add(i to j)
                        while (stack.size > 0) {
                            val (x, y) = stack.pop()
                            for ((a, b) in matrix.getAdjacentWithCorners(x, y)) {
                                matrix[a][b] += 1
                                if (matrix[a][b] > 9 && a to b !in visited) {
                                    visited.add(a to b)
                                    stack.add(a to b)
                                }
                            }
                        }
                    }
                }
            }
            var cnt = 0
            for (i in matrix.indices) {
                for (j in matrix.indices) {
                    if (matrix[i][j] > 9) {
                        matrix[i][j] = 0
                        cnt++
                    }
                }
            }
            if (cnt == 100) {
                result = step
                bool = false
            }
        }
    }
    return result
}