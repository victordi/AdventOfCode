package advent2015.day17

import utils.Input

val lines = Input.readInput()
val containers = lines.map { it.toInt() }

fun List<Int>.combinations(target: Int): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    fun generate(current: List<Int>, remaining: List<Int>) {
        if (current.sum() == target) {
            result.add(current)
            return
        }
        if (current.sum() > target) return
        if (remaining.isEmpty()) return
        generate(current + remaining.first(), remaining.drop(1))
        generate(current, remaining.drop(1))
    }
    generate(emptyList(), this)
    return result
}

fun main() {
    println(first())
    println(second())
}

val combinations = containers.combinations(150)

fun first(): Int = combinations.size

fun second(): Int = combinations.minOf { it.size }.let { minSize ->
    combinations.filter { it.size == minSize }.size
}