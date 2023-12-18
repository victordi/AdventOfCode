package AoC2021.day7

import utils.Input.withInput
import kotlin.math.abs

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = Int.MAX_VALUE
    withInput {
        val list = it.first().split(',').map { it.toInt() }.toList()
        val avg = list.average()
        val sorted = list.sorted()
        val median = if (list.size % 2 == 0) (sorted[list.size / 2 ] )
                    else sorted[list.size / 2]
        result = list.sumOf { abs(it - median) }
    }
    return result
}

fun second(): Int {
    var result = Int.MAX_VALUE
    withInput {
        val list = it.first().split(',').map { it.toInt() }.toList()
        val avg = list.average().toInt()
        result = list.distance(avg)
    }
    return result
}

fun List<Int>.distance(median: Int): Int {
    return sumOf { factorial(abs(it - median))}
}

fun factorial(value: Int): Int =
    if (value == 0) 0
    else value + factorial(value - 1)