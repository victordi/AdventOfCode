package AoC2021.day2

import Input.withInput
import kotlin.IllegalArgumentException

fun main() = withInput { input ->
    val instructions = input.toList().map { instruction ->
        instruction.split(' ').let { split ->
            split[0] to split[1].toInt()
        }
    }
    println(first(instructions))
    println(second(instructions))
}

fun first(instructions: List<Pair<String, Int>>): Int = instructions
    .fold(0 to 0) { (x, y), (direction, nr) ->
        when (direction) {
            "forward" -> x + nr to y
            "up" -> x to y - nr
            "down" -> x to y + nr
            else -> throw IllegalArgumentException("Input list contains illegal items: $direction")
        }
    }.let { (x, y) -> x * y }

fun second(instructions: List<Pair<String, Int>>): Int = instructions
    .fold(Triple(0, 0, 0)) { (x, y, aim), (direction, nr) ->
        when (direction) {
            "forward" -> Triple(x + nr, y + aim * nr, aim)
            "up" -> Triple(x, y, aim - nr)
            "down" -> Triple(x, y, aim + nr)
            else -> throw IllegalArgumentException("Input list contains illegal items: $direction")
        }
    }.let { (x, y) -> x * y }