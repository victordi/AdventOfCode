package advent2024.day14

import utils.Input.withInput
import utils.prettyPrint

data class Robot(val x: Int, val y: Int, val speedX: Int, val speedY: Int)

fun extractInts(input: String): List<Int> {
  return Regex("""[+-]?\d+""").findAll(input).map { it.value.toInt() }.toList()
}

val robots = withInput { input ->
  input
    .map { extractInts(it) }
    .map { (x, y, speedX, speedY) -> Robot(x, y, speedX, speedY) }
    .toList()
}

fun Robot.move(): Robot {
  return Robot(Math.floorMod(x + speedX, n), Math.floorMod(y + speedY, m), speedX, speedY)
}

val n = 101
val m = 103

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  val state = (1..100).fold(robots) { acc, _ -> acc.map { it.move() } }
  val topLeft = state.filter { (x, y, _, _) -> x < n / 2 && y < m / 2 }.size
  val topRight = state.filter { (x, y, _, _) -> x < n / 2 && y > m / 2 }.size
  val botLeft = state.filter { (x, y, _, _) -> x > n / 2 && y < m / 2 }.size
  val botRight = state.filter { (x, y, _, _) -> x > n / 2 && y > m / 2 }.size
  topLeft * topRight * botRight * botLeft
}

fun List<Robot>.isTree(): Boolean {
  val grouped = groupBy { it.x }.mapValues { it.value.size }
  val iList = grouped.keys.sorted()
  var cnt = 0
  for (i in iList) {
    val nr = grouped[i]!!
    try {
      if (
        grouped[i + 1]!! >= nr + 1 &&
        grouped[i + 2]!! >= nr + 3 &&
        grouped[i + 3]!! >= nr + 5 &&
        grouped[i + 4]!! >= nr + 7
      ) {
        cnt++
      }
    } catch (_: Exception) { }
  }
  return cnt >= 5
}

fun second(): Int = run {
  var state = robots

  repeat(10000) {
    if (state.isTree()) {
      println("====================================")
      println("After $it seconds")
      val matrix = Array(n) { Array(m) { '.' } }
      state.forEach { (x, y, _, _) -> matrix[x][y] = '#' }
      matrix.prettyPrint()
      println("====================================")
    }
    state = state.map { it.move() }
  }
  val topLeft = state.filter { (x, y, _, _) -> x < n / 2 && y < m / 2 }.size
  val topRight = state.filter { (x, y, _, _) -> x < n / 2 && y > m / 2 }.size
  val botLeft = state.filter { (x, y, _, _) -> x > n / 2 && y < m / 2 }.size
  val botRight = state.filter { (x, y, _, _) -> x > n / 2 && y > m / 2 }.size
  topLeft * topRight * botRight * botLeft
}
