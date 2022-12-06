package advent2022.day6

import Input.readInput
import java.util.*


fun main() {
    println(solve(4))
    println(solve(14))
}

fun solve(size: Int): Int = with(readInput()) {
    var current = ""
    first().forEachIndexed { idx, c ->
        if (current.length == size) return@with idx

        if (c !in current) current += c
        else current = current.dropWhile { it != c }.drop(1) + c
    }
    -1
}
