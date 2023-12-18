package advent2023.day18

import Direction
import Direction.East
import Direction.North
import Direction.South
import Direction.West
import Input.readInput

fun main() {
  println(first())
  println(second())
}

val part1Input = readInput().map {
  val (d, m, _) = it.split(" ")
  val dir = when (d) {
    "R" -> East
    "L" -> West
    "U" -> North
    "D" -> South
    else -> throw IllegalArgumentException("Invalid direction")
  }
  m.toLong() to dir
}

fun first(): Long = part1Input.getArea()

val part2Input = readInput().map {
  val (_, _, c) = it.split(" ")
  val color = c.drop(1).dropLast(1)
  val dir = when (color.last()) {
    '0' -> East
    '1' -> South
    '2' -> West
    '3' -> North
    else -> throw IllegalArgumentException("Invalid direction")
  }
  color.drop(1).dropLast(1).toLong(radix = 16) to dir
}


fun second(): Long = part2Input.getArea()

fun List<Pair<Long, Direction>>.getArea(): Long {
  var start = 0L to 0L
  val visited = mutableSetOf(start)
  var points = 0L

  forEach { (meters, dir) ->
    start = start.first + dir.diff.first * meters to start.second + dir.diff.second * meters
    points += meters
    visited.add(start)
  }

  var area = 0L
  val reversed = listOf(start) + visited.reversed()
  reversed.zipWithNext().forEach { (p1, p2) ->
    val (x1, y1) = p1
    val (x2, y2) = p2
    area += x1 * y2 - x2 * y1
  }
  return (area + points) / 2 + 1
}
