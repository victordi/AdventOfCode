package advent2015.day10

import utils.Input

val lines = Input.readInput()

fun String.readOutLoud(): String = run {
    val result = StringBuilder()
    var count = 1
    var current = this[0]
    for (i in 1 until this.length) {
        if (this[i] == current) count++
        else {
            result.append(count).append(current)
            count = 1
            current = this[i]
        }
    }
    result.append(count).append(current)
    result.toString()
}

fun solve(n: Int) = (1..n).fold("1113222113") { acc, _ -> acc.readOutLoud() }.length

fun main() {
    println(solve(40))
    println(solve(50))
}
