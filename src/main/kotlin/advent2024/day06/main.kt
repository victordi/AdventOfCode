package advent2024.day06

import utils.*
import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

val matrix = withInput { input ->
  val list = input.toList()
  Array(list.size) { list[it].toCharArray().toTypedArray() }
}
val start = matrix.indexOf('^')

fun wardPath(): Set<Point> {
  val visited = mutableSetOf<Point>()
  var current = start
  var direction = Direction.North

  while (matrix.getOrElse(current, '|') != '|') {
    visited.add(current)
    if (matrix.getOrElse(current.plus(direction.diff), ' ') == '#') {
      direction = direction.rotateRight()
    } else {
      current += direction.diff
    }
  }

  return visited
}

fun Array<Array<Char>>.isCycle(): Boolean {
  val visitedBumps = mutableSetOf<Pair<Point, Direction>>()
  var current = start
  var direction = Direction.North

  while (this.getOrElse(current, '|') != '|') {
    if ((current to direction) in visitedBumps) return true
    visitedBumps.add(current to direction)
    if (this.getOrElse(current.plus(direction.diff), ' ') == '#') {
      direction = direction.rotateRight()
    } else {
      current += direction.diff
    }
  }

  return false
}

fun first(): Int = wardPath().size

fun second(): Int = matrix.count { (i, j) ->
  val copy = matrix.copy()
  copy[i][j] = '#'
  copy.isCycle()
}
