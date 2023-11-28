package advent2015.day2

val lines = Input.readInput()

fun main() {
    println(first())
    println(second())
}

fun first(): Long = lines.sumOf { line ->
    val sides = line.split('x').map { it.toLong() }
    val lw = sides[0] * sides[1]
    val wh = sides[1] * sides[2]
    val hl = sides[2] * sides[0]
    2 * (lw + wh + hl) + minOf(lw, wh, hl)
}

fun second(): Long = lines.sumOf { line ->
    val sides = line.split('x').map { it.toLong() }
    val lw = 2 * (sides[0] + sides[1])
    val wh = 2 * (sides[1] + sides[2])
    val hl = 2 * (sides[2] + sides[0])
    minOf(lw, wh, hl) + sides[0] * sides[1] * sides[2]
}