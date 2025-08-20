package advent2016.day01

import utils.Direction
import utils.Input.withInput
import kotlin.math.abs

fun main() {
  solve()
}

fun solve(): Unit = withInput { input ->
  val moves = input.toList().first().split(", ").map { it.first() to it.drop(1).toInt() }
  var dir = Direction.North
  var (x, y) = 0 to 0
  val visited = mutableSetOf<Pair<Int, Int>>()
  var first = 0

  fun move() {
    when (dir) {
      Direction.North -> y += 1
      Direction.East -> x += 1
      Direction.South -> y -= 1
      Direction.West -> x -= 1
      else -> throw IllegalArgumentException("Unknown direction: $dir")
    }
    if (x to y in visited && first == 0) {
      first = abs(x) + abs(y)
    }
    visited.add(x to y)
  }

  for ((turn, steps) in moves) {
    dir = if (turn == 'R') dir.rotateRight() else dir.rotateLeft()
    repeat(steps) { move() }
  }

  println(abs(x) + abs(y))
  println(first)
}
