package advent2016.day03

import utils.Input.withInput
import utils.println
import utils.zip3

fun main() {
  println(first())
  println(second())
}

fun List<Int>.isTriangle(): Boolean {
  if (size != 3) return false
  val (a, b, c) = this
  return a + b > c && a + c > b && b + c > a
}

fun first(): Int = withInput { input ->
  val triangles = input.toList()
    .map { it.split(" ").filter { l -> l.toIntOrNull() != null }.map { it.toInt() } }
  triangles.count { it.isTriangle() }
}

fun second(): Int = withInput { input ->
  val lines = input.toList()
    .map { it.split(" ").filter { l -> l.toIntOrNull() != null }.map { it.toInt() } }
  var count = 0
  lines.chunked(3).forEach {
    for (i in 0..2) {
      val triangle = listOf(it[0][i], it[1][i], it[2][i])
      if (triangle.isTriangle()) count++
    }
  }
  count
}
