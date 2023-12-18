package advent2023.day18

import Direction
import Direction.East
import Direction.North
import Direction.South
import Direction.West
import Input.readInput
import shoelaceArea

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
  val start = 0L to 0L

  val (_, visited, points) = fold(Triple(start, setOf(start), 0L)) { (currentPos, visited, points), (meters, dir) ->
    val nextPos = currentPos.first + dir.diff.first * meters to currentPos.second + dir.diff.second * meters
    Triple(nextPos, visited + nextPos, points + meters)
  }

  return visited.shoelaceArea() + points / 2 + 1
}
