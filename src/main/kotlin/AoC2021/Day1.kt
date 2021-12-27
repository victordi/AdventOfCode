package AoC2021.day1

import Input.withInput
import zip3

fun main() = withInput { input ->
    val numbers = input.map { it.toInt() }.toList()
    println(first(numbers))
    println(second(numbers))
}

fun first(numbers: List<Int>): Int = numbers
    .zipWithNext().count { (a, b) ->
        b > a
    }

fun Triple<Int, Int, Int>.sum(): Int = this.first + this.second + this.third

fun second(numbers: List<Int>): Int = numbers
    .zip3().zipWithNext().count { (a, b) ->
        b.sum() > a.sum()
    }