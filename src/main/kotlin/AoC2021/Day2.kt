package AoC2021.day2

import Input.withInput

fun main() {
    println(first())
    println(second())
}

fun first(): Int {
    var result = 0
    withInput {
        val list = it.toList()
        var (x, y) = 0 to 0
        list.forEach {
            val split = it.split(' ')
            val direction = split[0]
            val nr = split[1].toInt()
            when(direction) {
                "forward" -> x += nr
                "up" -> y -= nr
                "down" -> y += nr
            }
        }
        result = x * y
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput {
        val list = it.toList()
        var (x, y) = 0 to 0
        var aim = 0
        list.forEach {
            val split = it.split(' ')
            val direction = split[0]
            val nr = split[1].toInt()
            when(direction) {
                "forward" -> {
                    x += nr
                    y += aim * nr
                }
                "up" -> aim -= nr
                "down" -> aim += nr
            }
        }
        result = x * y
    }
    return result
}