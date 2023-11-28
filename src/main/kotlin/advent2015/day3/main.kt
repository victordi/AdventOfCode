package advent2015.day3

import every
import move
import toDirection

val lines = Input.readInput()

fun main() {
    println(first())
    println(second())
}

fun first(): Int = run {
    var current = 0 to 0
    val visited = mutableSetOf(current)
    lines.first().forEach {
        current = current.move(it.toDirection())
        visited.add(current)
    }
    visited.size
}

fun second(): Int = run {
    var santa = 0 to 0
    var robot = 0 to 0
    val visited = mutableSetOf(santa)
    val directions = lines.first()
    directions.toList().every(2).forEach {
        santa = santa.move(it[0].toDirection())
        robot = robot.move(it[1].toDirection())
        visited.add(santa)
        visited.add(robot)
    }
    visited.size
}