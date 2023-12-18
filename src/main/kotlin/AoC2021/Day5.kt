package AoC2021.day5

import utils.Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput {
        var map = mutableMapOf<Pair<Int,Int>, Int>()
        val list = it.toList()
        for (line in list) {
            val (fst, snd) = line.parse()
            var (x1, y1) = fst
            var (x2, y2) = snd

            if (x1 == x2) {
                for (y in y1.coerceAtMost(y2)..y1.coerceAtLeast(y2)) {
                    if (x1 to y in map) {
                        map[x1 to y] = map[x1 to y]!! + 1
                        if (map[x1 to y] == 2) result++
                    } else {
                        map[x1 to y] = 1
                    }
                }
            }
            else if (y1 == y2) {
                for (x in x1.coerceAtMost(x2)..x1.coerceAtLeast(x2)) {
                    if (x to y1 in map) {
                        map[x to y1] = map[x to y1]!! + 1
                        if (map[x to y1] == 2) result++
                    } else {
                        map[x to y1] = 1
                    }
                }
            } else {
                var xfactor = 1
                var yfactor = 1
                if (x1 > x2) xfactor = -1
                if (y1 > y2) yfactor = -1
                while (true) {
                    if (x1 to y1 in map) {
                        map[x1 to y1] = map[x1 to y1]!! + 1
                        if (map[x1 to y1] == 2) result++
                    } else {
                        map[x1 to y1] = 1
                    }
                    if (x1 == x2) break
                    x1 += xfactor
                    y1 += yfactor
                }
            }
        }
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput {

    }
    return result
}

fun String.parse(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val split = this.split("->")
    val left = split[0].dropLast(1).split(',').map { it.toInt() }
    val right = split[1].drop(1).split(',').map { it.toInt() }
    return Pair(left[0] to left[1], right[0] to right[1])
}