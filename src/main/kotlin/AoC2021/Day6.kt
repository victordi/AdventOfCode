package AoC2021.day6

import Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Long {
    var result = 0L
    withInput { seq ->
        val list = seq.first().split(',').map { it.toInt() }.toList()

        for (nr in list) {
            result += nrFish(80 - nr - 1)
        }
        result += list.size
    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput { seq ->
        val list = seq.first().split(',').map { it.toInt() }.toList()

        for (nr in list) {
            result += nrFish(256 - nr - 1)
        }
        result += list.size
    }
    return result
}

val map = mutableMapOf<Int, Long>()

fun nrFish(daysLeft: Int): Long {
    if (daysLeft in map) return map[daysLeft]!!
    var result = 0L
    if (daysLeft >= 0) {
        result++
        result += nrFish(daysLeft - 9) + nrFish(daysLeft - 7)
    }
    map[daysLeft] = result
    return result
}