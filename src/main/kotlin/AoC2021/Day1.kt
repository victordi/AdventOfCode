package AoC2021.day1

import Input.withInput

fun main() {
    println(first())
    println(second())
}

private fun first(): Int {
    var result = 0
    withInput {
        val list = it.map { it.toInt() }.toList()
        var last = list.first()
        list.drop(1).forEach {
            if (it > last) result++
            last = it
        }
    }
    return result
}

private fun second(): Int {
    var result = 0
    withInput {
        val list = it.map { it.toInt() }.toList()
        var prev = list[0] + list[1] + list[2]
        list.forEachIndexed { index, i ->
            if ( index < list.size - 2) {
                val sum = list[index] + list[index + 1] + list[index + 2]
                if (sum > prev) result++
                prev = sum
            }
        }
    }
    return result
}