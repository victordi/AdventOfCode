package advent2017.day01

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun first(): Int = withInput { input ->
  val line = input.first()
  (line + line[0]).map { it.digitToInt() }.zipWithNext().filter { (x, y) -> x == y }.sumOf { it.first }
}

fun second(): Int = withInput { input ->
  val digits = input.first().map { it.digitToInt() }
  val half = digits.size / 2
  digits.zip(digits.drop(half) + digits.take(half)).filter { (x, y) -> x == y }.sumOf { it.first }
}
