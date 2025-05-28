package advent2015.day18

import utils.*

val lines = Input.readInput()
val matrix = lines.parseArray { if (it == '#') 1 else 0 }

fun Graph<Int>.step(turnCorners: Boolean = false): Graph<Int> {
  val copy = this.map { it.copyOf() }.toTypedArray()
  arrayForEach { (x, y) ->
    val neighbours = this.getAdjacentWithCorners(x, y)
    if (this[x][y] == 1) {
      if (neighbours.count { (i, j) -> this[i][j] == 1 } !in 2..3) copy[x][y] = 0
    } else {
      if (neighbours.count { (i, j) -> this[i][j] == 1 } == 3) copy[x][y] = 1
    }
  }
  if (turnCorners) {
    copy[0][0] = 1
    copy[0][copy[0].size - 1] = 1
    copy[copy.size - 1][0] = 1
    copy[copy.size - 1][copy[0].size - 1] = 1
  }
  return copy
}

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  var current = matrix
  repeat(100) {
    current = current.step()
  }
  current.count { (i, j) -> current[i][j] == 1 }
}

fun second(): Int = run {
  var current = matrix
  repeat(100) {
    current = current.step(true)
  }
  current.count { (i, j) -> current[i][j] == 1 }
}