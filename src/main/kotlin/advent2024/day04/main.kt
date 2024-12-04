package advent2024.day04

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

fun first(): Int = matrix.sumOf { point ->
  listOf(0 to 1, 1 to 0, 1 to 1, 1 to -1)
    .map { (xInc, yInc) ->
      (0..3).fold("") { acc, n ->
        val (i, j) = point
        val x = i + n * xInc
        val y = j + n * yInc
        acc + matrix.getOrElse(x to y, '-')
      }
    }
    .count { it == "XMAS" || it == "SAMX" }
}

fun second(): Any = matrix.count { point ->
  val words = listOf("MAS", "SAM")
  val d1 =
    matrix.getOrElse(point + (-1 to -1), '-') + "A" + matrix.getOrElse(point + (1 to 1), '-')
  val d2 =
    matrix.getOrElse(point + (-1 to 1), '-') + "A" + matrix.getOrElse(point + (1 to -1), '-')
  matrix.getOrElse(point, '-') == 'A' && d1 in words && d2 in words
}
