package advent2022.day23

import AoC2021.Day20.adjacent
import Point
import arrow.core.Tuple4

fun parseInput(): Array<Point> {
    val lines = Input.readInput()
    val elves = mutableSetOf<Point>()
    lines.forEachIndexed { x, s ->
        s.toCharArray().forEachIndexed { y, c ->
            if (c == '#') elves.add(x to y)
        }
    }
    return elves.toTypedArray()
}

val elves = parseInput()

fun main() {
    println(first())
    println(second())
}

typealias Order = Pair<List<Point>, Point>
fun Point.roundOrder(round: Int): Tuple4<Order, Order, Order, Order> {
    val neighbours = adjacent().minus(this)
    val north = neighbours.filter { it.first == first - 1 } to Pair(first - 1, second)
    val south = neighbours.filter { it.first == first + 1 } to Pair(first + 1, second)
    val west = neighbours.filter { it.second == second - 1 } to Pair(first, second - 1)
    val east = neighbours.filter { it.second == second + 1 } to Pair(first, second + 1)
    return when(round % 4) {
        0 -> Tuple4(north, south, west, east)
        1 -> Tuple4(south, west, east, north)
        2 -> Tuple4(west, east, north, south)
        3 -> Tuple4(east, north, south, west)
        else -> throw IllegalArgumentException("All results of module covered but IDE is Clueless")
    }
}

fun round(nr: Int): Boolean {
    var changed = false
    val newElves = mutableListOf<Point>()
    for (elf in elves) {
        val neighbours = elf.adjacent().minus(elf)
        val (dir1, dir2, dir3, dir4) = elf.roundOrder(nr)
        if (neighbours.find { it in elves } == null) newElves.add(elf)
        else if (dir1.first.find { it in elves } == null) newElves.add(dir1.second)
        else if (dir2.first.find { it in elves } == null) newElves.add(dir2.second)
        else if (dir3.first.find { it in elves } == null) newElves.add(dir3.second)
        else if (dir4.first.find { it in elves } == null) newElves.add(dir4.second)
        else newElves.add(elf)
    }
    for ((idx, elf) in newElves.withIndex()) {
        if (newElves.count { it == elf } == 1) {
            if (elves[idx] != elf) changed = true
            elves[idx] = elf
        }
    }
    return changed
}

fun first(): Int = run {
    repeat(10) { round(it) }
    val n = elves.maxOf { it.first } - elves.minOf { it.first } + 1
    val m = elves.maxOf { it.second } - elves.minOf { it.second } + 1
    return n * m - elves.size
}

fun second(): Int = run {
    (10..100_000).forEach {
        if (!round(it)) return it + 1
    }
    0
}