package advent2024.day18

import utils.*
import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

val n = 71
val bytes = withInput { input ->
  input.toList().map { it.splitTwo(",").let { (a, b) -> a.toInt() to b.toInt() } }
}
val start = 0 to 0
val end = n - 1 to n - 1
val matrix = Array(n) { Array(n) { '.' } }

fun Array<Array<Char>>.bfs(start: Point, end: Point): Int {
  val visited = mutableSetOf<Point>()
  val stack = mutableListOf(start to 0)

  while (stack.isNotEmpty()) {
    val (current, steps) = stack.removeAt(0)
    if (current == end) return steps
    if (current in visited) continue
    visited.add(current)
    current.getAdjacent()
      .filter { (x, y) -> (x to y) !in visited && getOrElse(x to y, '#') != '#' }
      .forEach { stack.add(it to steps + 1) }
  }
  return -1
}

fun first(): Int = run {
  for ((x, y) in bytes.take(1024)) {
    matrix[x][y] = '#'
  }
  matrix.bfs(start, end)
}

fun second(): Int = run {
  var fallingByte = 1024
  while(true) {
    matrix[bytes[fallingByte]] = '#'
    if (matrix.bfs(start, end) == -1) {
      break
    }
    fallingByte++
  }
  println(bytes[fallingByte])
  fallingByte
}
