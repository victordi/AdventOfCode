package AoC2021.day13

import utils.Input.withInput
import utils.prettyPrint

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Long {
    var result = 0L
    withInput { input ->
        val list = input.toList()
        var isFold = false
        val folds = mutableListOf<Pair<Char, Int>>()
        var coords = mutableSetOf<Pair<Int, Int>>()
        for (line in list){
            if (line.isEmpty()) {
                isFold = true
                continue
            }
            if (isFold) {
                val split = line.split('=')
                folds.add(split[0].last() to split[1].toInt())
            } else {
                val split = line.split(',')
                val x = split[0].toInt()
                val y = split[1].toInt()
                coords.add(x to y)
            }
        }
        for((dir, value) in folds) {
            if (dir == 'x') {
                coords = coords.map { (x, y) ->
                    val newX = if (x > value) value - (x - value) else x
                    newX to y
                }.toMutableSet()
            } else {
                coords = coords.map { (x, y) ->
                    val newY = if (y > value) value - (y - value) else y
                    x to newY
                }.toMutableSet()
            }
            break
        }
        result = coords.size.toLong()

    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val list = input.toList()
        var isFold = false
        val folds = mutableListOf<Pair<Char, Int>>()
        var coords = mutableSetOf<Pair<Int, Int>>()
        for (line in list){
            if (line.isEmpty()) {
                isFold = true
                continue
            }
            if (isFold) {
                val split = line.split('=')
                folds.add(split[0].last() to split[1].toInt())
            } else {
                val split = line.split(',')
                val x = split[0].toInt()
                val y = split[1].toInt()
                coords.add(x to y)
            }
        }
        for((dir, value) in folds) {
            if (dir == 'x') {
                coords = coords.map { (x, y) ->
                    val newX = if (x > value) value - (x - value) else x
                    newX to y
                }.toMutableSet()
            } else {
                coords = coords.map { (x, y) ->
                    val newY = if (y > value) value - (y - value) else y
                    x to newY
                }.toMutableSet()
            }
        }
        val x = coords.maxOf { it.first }
        val y = coords.maxOf { it.second }

        val graph = Array(y + 1) {
            Array(x + 1) { ' ' }
        }
        coords.forEach { (x, y) -> graph[y][x] = '#' }
        graph.prettyPrint()
    }
    return result
}
