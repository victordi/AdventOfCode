package advent2025.day03

import utils.Input.withInput
import utils.digits

fun main() {
  println(solve(2))
  println(solve(12))
}

fun solve(len: Int): Long = withInput { input ->
  input
    .map { it.digits() }
    .sumOf { batteries ->
      (len downTo 1).fold(0L to 0) { (sum, i), remaining ->
        val (idx, digit) = batteries.withIndex().drop(i).dropLast(remaining - 1).maxBy { it.value }
        (sum * 10 + digit) to (idx + 1)
      }.first
    }
}
