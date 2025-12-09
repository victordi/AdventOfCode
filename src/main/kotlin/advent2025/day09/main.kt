package advent2025.day09

import utils.Input.readInput
import utils.Point
import utils.extractInts

val red = readInput().map { line -> extractInts(line).let { it[0] to it[1] } }
val pairs = red.zipWithNext() + (red.first() to red.last())
val green = mutableSetOf<Point>()
val edgesY = mutableMapOf<Int, MutableList<Int>>()
val edgesX = mutableMapOf<Int, MutableList<Int>>()

fun main() {
  pairs.forEach { (p1, p2) ->
    val (x1, y1) = p1
    val (x2, y2) = p2
    if (x1 == x2) {
      val range = if (y1 < y2) y1 + 1..y2 - 1 else y2 + 1..y1 - 1
      green.addAll(range.map { x1 to it })
    } else {
      val range = if (x1 < x2) x1 + 1..x2 - 1 else x2 + 1..x1 - 1
      green.addAll(range.map { it to y1 })
    }
  }

  (green + red).forEach { (x, y) ->
    edgesY.getOrPut(y) { mutableListOf() }.add(x)
    edgesX.getOrPut(x) { mutableListOf() }.add(y)
  }

  println(first())
  println(second())
}

fun area(p1: Point, p2: Point): Long =
  (kotlin.math.abs(p1.first - p2.first).toLong() + 1) * (kotlin.math.abs(p1.second - p2.second).toLong() + 1)

val pointMap = mutableMapOf<Point, Boolean>()
fun Point.enclosed(): Boolean = run {
  if (pointMap.containsKey(this)) return pointMap[this]!!
  val (x, y) = this
  if (this in red || this in green) {
    pointMap[this] = true
    return@run true
  }

  val hasLeft = edgesY[y]?.any { it < x } ?: false
  val hasRight = edgesY[y]?.any { it > x } ?: false
  val hasTop = edgesX[x]?.any { it < y } ?: false
  val hasBot = edgesX[x]?.any { it > y } ?: false

  val result = hasLeft && hasRight && hasTop && hasBot
  pointMap[this] = result
  result
}

fun enclosed(p1: Point, p2: Point): Boolean = run {
  val (x1, y1) = p1
  val (x2, y2) = p2

  val xRange = if (x1 < x2) x1..x2 else x2..x1
  val yRange = if (y1 < y2) y1..y2 else y2..y1

  xRange.forEach { x ->
    if (!(x to y1).enclosed()) return@run false
    if (!(x to y2).enclosed()) return@run false
  }

  yRange.forEach { y ->
    if (!(x1 to y).enclosed()) return@run false
    if (!(x2 to y).enclosed()) return@run false
  }

  true
}

fun first(): Long = run {
  var max = 0L
  for (i in red.indices) {
    for (j in i + 1 until red.size) {
      val p1 = red[i]
      val p2 = red[j]
      val area = area(p1, p2)
      if (area > max) {
        max = area
      }
    }
  }
  max
}

fun second(): Long = run {
  var max = 10_000_000L
  for (i in red.indices) {
    for (j in i + 1 until red.size) {
      val p1 = red[i]
      val p2 = red[j]
      val area = area(p1, p2)

      if (area <= max) continue
      if (!enclosed(p1, p2)) continue

      max = area
    }
  }
  max
}
