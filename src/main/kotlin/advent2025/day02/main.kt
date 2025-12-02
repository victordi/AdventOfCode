package advent2025.day02

import utils.Input.withInput

fun main() {
  println(solve { isInvalid() })
  println(solve { isInvalid2() })
}

fun solve(check: String.() -> Boolean): Long = withInput { input ->
  input.first().split(",").sumOf { range ->
    val (start, end) = range.split("-").map { it.toLong() }
    (start..end).filter { it.toString().check() }.sum()
  }
}

fun String.isInvalid(): Boolean = substring(0, length / 2) == substring(length / 2)
fun String.isInvalid2(): Boolean = (1..length / 2)
  .any { patternLen -> chunked(patternLen).all { it == take(patternLen) } }
