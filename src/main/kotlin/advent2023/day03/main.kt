package advent2023.day03

import Input.withInput
import Point
import getAdjacentWithCorners

fun main() {
  println(first())
  println(second())
}

fun List<Point>.isPartNumber(matrix: Array<Array<Char>>): Boolean = fold(false) { acc, point ->
  acc || matrix.getAdjacentWithCorners(point).any { (x, y) -> !matrix[x][y].isDigit() && matrix[x][y] != '.' }
}

fun first(): Long = withInput { input ->
  var result = 0L
  val matrix = input.map { it.toCharArray().toTypedArray() }.toList().toTypedArray()
  matrix.forEachIndexed { idx, line ->
    var nr = 0
    val points = mutableListOf<Point>()

    fun clear() {
      if (points.isPartNumber(matrix)) result += nr
      nr = 0
      points.clear()
    }

    line.forEachIndexed { index, c ->
      if (c.isDigit()) {
        nr = nr * 10 + c.digitToInt()
        points += idx to index
      } else clear()
    }
    if (nr != 0) clear()
  }
  result
}

fun List<Point>.getGears(matrix: Array<Array<Char>>): Set<Point> = fold(emptySet()) { acc, point ->
  acc + matrix.getAdjacentWithCorners(point).filter { (x, y) -> matrix[x][y] == '*' }
}

fun second(): Long = withInput { input ->
  val gears = mutableMapOf<Point, List<Int>>()
  val matrix = input.map { it.toCharArray().toTypedArray() }.toList().toTypedArray()
  matrix.forEachIndexed { idx, line ->
    var nr = 0
    val indexes = mutableListOf<Point>()

    fun clear() {
      indexes.getGears(matrix).forEach { (x, y) ->
        gears[x to y] = gears[x to y]?.plus(nr) ?: listOf(nr)
      }
      nr = 0
      indexes.clear()
    }

    line.forEachIndexed { index, c ->
      if (c.isDigit()) {
        nr = nr * 10 + c.digitToInt()
        indexes += idx to index
      } else clear()
    }
    if (nr != 0) clear()
  }
  gears.filter { (_ ,v) -> v.size == 2 }.mapValues { (_, v) -> v.fold(1L) {acc, i -> acc * i.toLong()} }.values.sum()
}
