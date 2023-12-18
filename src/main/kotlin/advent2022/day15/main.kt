package advent2022.day15

import utils.Input
import utils.Point
import utils.manhattanDistance
import utils.splitTwo

fun parseInput(): List<Pair<Point, Point>> =
    Input.readInput().fold(emptyList()) { acc, line ->
        val (first, second) = line.splitTwo(": ")
        val sensor = first.drop(10).splitTwo(", ").let { (x, y) -> x.drop(2).toInt() to y.drop(2).toInt() }
        val beacon = second.drop(21).splitTwo(", ").let { (x, y) -> x.drop(2).toInt() to y.drop(2).toInt() }
        acc + (sensor to beacon)
    }

val input = parseInput()
fun main() {
    println(first())
    println(second())
}

fun first(): Int = run {
    val destLine = 2_000_000
    val beacons: Set<Point> = input.map { it.second }.toSet()
    val visited: MutableSet<Point> = mutableSetOf()
    input.forEach { (sensor, beacon) ->
        val distance = manhattanDistance(sensor, beacon)
        for (x in sensor.first - distance..sensor.first + distance) {
                val new = x to destLine
                if (manhattanDistance(sensor, new) <= distance) visited.add(new)
            }
    }

    visited.filter { (x, y) -> y == destLine && !beacons.contains(x to y) }.size
}

fun second(): Long = run {
    val maxCord = 4_000_000
    val map: MutableMap<Int, MutableList<IntRange>> = mutableMapOf()

    input.forEach { (sensor, beacon) ->
        val distance = manhattanDistance(sensor, beacon)
        for (x in maxOf(0, sensor.first - distance)..minOf(maxCord, sensor.first + distance)) {
            val remainingDist = distance - kotlin.math.abs(sensor.first - x)
            val range = maxOf(0, sensor.second - remainingDist)..minOf(maxCord, sensor.second + remainingDist)
            if (map.contains(x)) {
                map[x]!!.add(range)
            } else {
                map[x] = mutableListOf(range)
            }
        }
    }

    var finalX = 0
    var finalY = 0
    for (x in 0..maxCord) {
        val ranges = map[x]!!
        ranges.sortBy { it.first }
        val start = ranges.first().first
        var end = ranges.first().last
        ranges.drop(1).forEach {
            if (it.first in start..end) {
                end = maxOf(end, it.last)
            } else {
                finalX = x
                finalY = end + 1
            }
        }
    }
    finalX.toLong() * maxCord.toLong() + finalY.toLong()
}