package advent2022.day10

import Input.readInput
import prettyPrint
import splitTwo
import kotlin.math.abs


fun main() {
    println(first())
    println(second())
}

val instructions = readInput().map {
    if (it == "noop") 0
    else it.splitTwo(" ").second.toInt()
}

fun first(): Int = run {
    var cycle = 0
    var current = 1
    var total = 0
    var nextStop = 20
    instructions.forEach {
        cycle++
        if (cycle == nextStop) {
            val power = cycle * current
            total += power
            nextStop += 40
        } else if (cycle + 1 == nextStop) {
            val power = (cycle + 1) * current
            total += power
            nextStop += 40
        }

        current += it
        if (it != 0) cycle++
    }
    total
}

fun second(): Int = run {
    var drawing = Array(6) { Array(40) { "." } }
    var cycle = 0
    var current = 1
    drawing[0][0] = "#"

    fun nextCycle() = cycle++.also { drawing.drawPixel(cycle, current) }

    instructions.forEach {
        nextCycle()
        current += it
        if (it != 0) nextCycle()
    }
    drawing.prettyPrint()

    0
}

fun Array<Array<String>>.drawPixel(cycle: Int, current: Int) {
    val x = cycle / 40
    val y = cycle % 40
    if (y in (current - 1..current + 1)) {
        this[x][y] = "#"
    }
}
