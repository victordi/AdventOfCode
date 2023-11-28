package advent2015.day6

import arrayForEach
import splitTwo

val lines = Input.readInput()

fun main() {
    val z:Boolean = true
    val z2:Boolean? = null
    val x = z.not() ?: println("123")
    val x1 = z2?.not() ?: println("1234")
//    println(first())
//    println(second())
}

fun first(): Int = run {
    val map = Array(1000) { Array(1000) { 0 } }
    lines.forEach { line ->
        val numbers = line.split(' ').filter { it.contains(',') }
        val (x1, y1) = numbers[0].splitTwo(",") { it.toInt() }
        val (x2, y2) = numbers[1].splitTwo(",") { it.toInt() }
        for (x in x1..x2) {
            for (y in y1..y2) {
                if (line.startsWith("toggle")) {
                    map[x][y] = 1 - map[x][y]
                } else if (line.startsWith("turn on")) {
                    map[x][y] = 1
                } else if (line.startsWith("turn off")) {
                    map[x][y] = 0
                } else throw IllegalArgumentException("Invalid line")
            }
        }
    }

    var cnt = 0
    map.arrayForEach { (i, j) ->
        if (map[i][j] == 1) cnt++
    }
    cnt
}

fun second(): Int = run {
    val map = Array(1000) { Array(1000) { 0 } }
    lines.forEach { line ->
        val numbers = line.split(' ').filter { it.contains(',') }
        val (x1, y1) = numbers[0].splitTwo(",") { it.toInt() }
        val (x2, y2) = numbers[1].splitTwo(",") { it.toInt() }
        for (x in x1..x2) {
            for (y in y1..y2) {
                if (line.startsWith("toggle")) {
                    map[x][y] += 2
                } else if (line.startsWith("turn on")) {
                    map[x][y] += 1
                } else if (line.startsWith("turn off")) {
                    map[x][y] -= 1
                    if (map[x][y] < 0) map[x][y] = 0
                } else throw IllegalArgumentException("Invalid line")
            }
        }
    }

    var cnt = 0
    map.arrayForEach { (i, j) ->
        cnt += map[i][j]
    }
    cnt
}