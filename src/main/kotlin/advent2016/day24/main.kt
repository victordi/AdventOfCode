package advent2016.day24

import utils.Input.withInput
import utils.Point
import utils.getAdjacent
import utils.permutations

fun main() {
  println(first())
  println(second())
}

val points = mutableMapOf<Int, Point>()

val matrix = withInput { input ->
  input.toList().mapIndexed { i, line ->
    val row = line.toCharArray().toTypedArray()
    row.forEachIndexed { idx, c ->
      if (c.isDigit()) {
        points[c.digitToInt()] = Point(i, idx)
      }
    }
    row
  }.toTypedArray()
}

fun bfs(start: Point, end: Point): Int {
  val visited = mutableSetOf<Point>()
  val queue = ArrayDeque<Pair<Point, Int>>()
  queue.add(start to 0)

  while (queue.isNotEmpty()) {
    val (current, steps) = queue.removeFirst()
    if (current == end) return steps
    if (current in visited) continue
    visited.add(current)
    matrix.getAdjacent(current).forEach { next ->
      if (matrix[next.first][next.second] != '#') queue.add(next to steps + 1)
    }
  }
  return -1
}

val distances = mutableMapOf<Pair<Int, Int>, Int>().let {
  points.forEach { (id1, pos1) ->
    points.forEach { (id2, pos2) ->
      if (pos1 != pos2) it[id1 to id2] = bfs(pos1, pos2)
    }
  }
  it
}



fun List<Int>.distance(): Int = run {
  var total = 0
  for (i in 0 until size - 1) {
    total += distances[this[i] to this[i + 1]]!!
  }
  total
}

fun first(): Int = run {
  val permutations = points.keys.filter { it != 0 }.permutations()
  permutations.minOf { (listOf(0) + it.toList()).distance() }
}

fun second(): Int = run {
  val permutations = points.keys.filter { it != 0 }.permutations()
  permutations.minOf { (listOf(0) + it.toList() + listOf(0)).distance() }
}
