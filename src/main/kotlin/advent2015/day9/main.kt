package advent2015.day9

import utils.Input

val lines = Input.readInput()

val distances = mutableMapOf<String, MutableList<Pair<String, Int>>>()

fun main() {
    lines.forEach {
        val (a, _, b, _, dist) = it.split(" ")
        distances.computeIfAbsent(a) { mutableListOf() }.add(b to dist.toInt())
        distances.computeIfAbsent(b) { mutableListOf() }.add(a to dist.toInt())
    }
    println(first())
    println(second())
}

fun first(): Int = run {
    var min = Int.MAX_VALUE
    repeat(10000) {
        var total = 0
        var current = distances.keys.random()
        val visited = mutableSetOf(current)

        while (visited != distances.keys) {
            val roads = distances[current]!!
            val cheapest = roads.filter { it.first !in visited }.minByOrNull { it.second }!!
            total += cheapest.second
            visited += cheapest.first
            current = cheapest.first
        }
        min = min.coerceAtMost(total)
    }
    min
}

fun second(): Int = run {
    var max = 0
    repeat(10000) {
        var total = 0
        var current = distances.keys.random()
        val visited = mutableSetOf(current)

        while (visited != distances.keys) {
            val roads = distances[current]!!
            val cheapest = roads.filter { it.first !in visited }.maxByOrNull { it.second }!!
            total += cheapest.second
            visited += cheapest.first
            current = cheapest.first
        }
        max = max.coerceAtLeast(total)
    }
    max
}