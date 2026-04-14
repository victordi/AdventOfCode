package advent2018.day06

import utils.Input.readInput
import utils.extractInts
import utils.manhattanDistance

fun main() {
  println(first())
  println(second())
}

val points = readInput().map { line -> extractInts(line).let { it[0] to it[1] } }
val minX = points.minOf { it.first }
val maxX = points.maxOf { it.first }
val minY = points.minOf { it.second }
val maxY = points.maxOf { it.second }

fun first(): Int = run {
  val areaCounts = mutableMapOf<Int, Int>()
  val infiniteAreas = mutableSetOf<Int>()

  for (x in minX..maxX) {
    for (y in minY..maxY) {
      val distances = points.mapIndexed { index, point ->
        index to manhattanDistance(point, x to y)
      }
      val (owner, dist) = distances.minBy { it.second }
      if (distances.count { it.second == dist } == 1) {
        areaCounts[owner] = areaCounts.getOrDefault(owner, 0) + 1

        if (x == minX || x == maxX || y == minY || y == maxY) {
          infiniteAreas.add(owner)
        }
      }
    }
  }

  return areaCounts.filterKeys { it !in infiniteAreas }.values.max()
}

fun second(): Int = run {
  var safeRegionSize = 0

  for (x in minX..maxX) {
    for (y in minY..maxY) {
      val totalDistance = points.sumOf { point -> manhattanDistance(point, x to y) }
      if (totalDistance < 10000) {
        safeRegionSize++
      }
    }
  }

  return safeRegionSize
}
