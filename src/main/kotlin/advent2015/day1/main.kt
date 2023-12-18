package advent2015.day1

import utils.Input

val lines = Input.readInput()

fun main() {
    println(first())
    println(second())
}

fun first(): Int = lines.first().count { it == '(' } - lines.first().count { it == ')' }

fun second(): Int = run {
    var floor = 0
    lines.first().forEachIndexed { index, c ->
        if (c == '(') floor++
        else floor--
        if (floor == -1) return index + 1
    }
    -1
}