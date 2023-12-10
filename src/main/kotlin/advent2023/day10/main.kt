package advent2023.day10

import Direction
import Direction.East
import Direction.North
import Direction.South
import Direction.West
import Input.withInput
import Point

fun main() {
  println(first())
  println(second())
}

val map = withInput { input ->
  val list = input.map { ".$it.".toList() }.toList()
  val padding = listOf(".".repeat(list.first().size).toList())
  val result = padding + list + padding
  result.map { it.toTypedArray() }.toTypedArray()
}

val visited = map.map { it.map { 0 }.toTypedArray() }.toTypedArray()

fun first(): Int = run {
//  map.prettyPrint()
  val startX = map.indexOfFirst { it.contains('S') }
  val startY = map[startX].indexOf('S')
  val end = startX to startY
  (startX to startY - 1).followPath(end, West) / 2
}

fun second(): Int = run {
//  visited.prettyPrint()
  visited.mapIndexed { i, ints ->
    ints.foldIndexed(0 to 0) { j, (nrb, cnt), vis ->
      if (vis == 1 && map[i][j] in listOf('|', 'L', 'J')) nrb + 1 to cnt
      else if (vis == 1) nrb to cnt
      else if (nrb % 2 == 1) nrb to cnt + 1
      else nrb to cnt
    }.second
  }.sum()
}

fun Point.followPath(end: Point, direction: Direction, size: Int = 1): Int = run {
  var (x, y) = this
  var steps = size
  var dir = direction
  while (true) {
    visited[x][y] = 1
    if (x to y == end) return steps
    steps += 1
    when (map[x][y]) {
      '|' -> {
        if (dir == South) x += 1
        else if (dir == North) x -= 1
        else return -1
      }

      '-' -> {
        if (dir == East) y += 1
        else if (dir == West) y -= 1
        else return -1
      }

      'L' -> {
        if (dir == West) {
          x -= 1; dir = North
        } else if (dir == South) {
          y += 1; dir = East
        } else return -1
      }

      'J' -> {
        if (dir == East) {
          x -= 1; dir = North
        } else if (dir == South) {
          y -= 1; dir = West
        } else return -1
      }

      '7' -> {
        if (dir == East) {
          x += 1; dir = South
        } else if (dir == North) {
          y -= 1; dir = West
        } else return -1
      }

      'F' -> {
        if (dir == West) {
          x += 1; dir = South
        } else if (dir == North) {
          y += 1; dir = East
        } else return -1
      }

      else -> return -1
    }
  }
  -1
}
