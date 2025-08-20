package advent2016.day18

import utils.Input.withInput

fun main() {
  println(solve(40))
  println(solve(400000))
}

val startingRow = withInput { it.first() }

fun String.nextRow(): String = run {
  mapIndexed { idx, c ->
    val left = getOrElse(idx - 1) { '.' }
    val center = getOrElse(idx) { '.' }
    val right = getOrElse(idx + 1) { '.' }
    when ("$left$center$right") {
      "^^." -> '^'
      ".^^" -> '^'
      "^.." -> '^'
      "..^" -> '^'
      else -> '.'
    }
  }.joinToString("")
}

fun solve(rows: Int): Int = generateSequence(startingRow) { it.nextRow() }
  .take(rows)
  .sumOf { row -> row.count { it == '.' } }

