package advent2022.day9

import Input.readInput
import splitTwo
import kotlin.math.abs

typealias Point = Pair<Int, Int>

fun main() {
    println(first())
    println(second())
}

val input = with(readInput()) {
    map { it.splitTwo(" ").let { (dir, step) -> dir to step.toInt() }}
}

fun first(): Int = run {
    val visited = mutableSetOf(0 to 0)

    var (headX, headY) = 0 to 0
    var tail = 0 to 0
    input.forEach { (dir, step) ->
        (1..step).forEach {
            when (dir) {
                "D" -> headX++
                "U" -> headX--
                "L" -> headY--
                "R" -> headY++
            }
            tail = (headX to headY).drag(tail)
            visited.add(tail)
        }
    }

    visited.size
}

fun second(): Int = run {
    val visited = mutableSetOf(0 to 0)
    var (headX, headY) = 0 to 0
    val tails = mutableListOf<Point>()
    repeat(9) { tails.add(0 to 0) }

    input.forEach { (dir, step) ->
        (1..step).forEach {
            when (dir) {
                "D" -> headX++
                "U" -> headX--
                "L" -> headY--
                "R" -> headY++
            }
            var prev = headX to headY
            tails.forEachIndexed { idx, tail ->
                tails[idx] = prev.drag(tail)
                prev = tails[idx]
            }
            visited.add(tails.last())
        }
    }

    visited.size
}

fun Point.isTouching(other: Point): Boolean = other.let { (x, y) ->
    abs(this.first - x) <= 1 && abs(this.second - y) <= 1
}

fun Point.drag(other: Point): Point = other.let { (x, y) ->
    if (isTouching(other)) other
    else {
        if (this.first == x) {
            if (this.second > y) x to y + 1
            else x to y - 1
        } else if (this.second == y) {
            if (this.first > x) x + 1 to y
            else x - 1 to y
        } else {
            if (this.first > x && this.second > y) x + 1 to y + 1
            else if (this.first > x && this.second < y) x + 1 to y - 1
            else if (this.first < x && this.second < y) x - 1 to y - 1
            else x - 1 to y + 1
        }
    }
}