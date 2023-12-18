package advent2023.day01

import utils.Input.withInput
import arrow.core.foldLeft

fun main() {
  println(first())
  println(second())
}

val numbers = mapOf(
  "one" to "o1e", "two" to "t2o", "three" to "t3e", "four" to "f4fr",
  "five" to "f5e", "six" to "s6x", "seven" to "s7n", "eight" to "e8t", "nine" to "n9e"
)

fun first(): Int = withInput { input ->
  input.toList().sumOf {
    val digits = it.filter { it.isDigit() }
    digits.first().digitToInt() * 10 + digits.last().digitToInt()
  }
}

fun second(): Int = withInput { input ->
  input
    .toList()
    .map {
      numbers.foldLeft(it) { acc, (key, value) ->
        acc.replace(key, value)
      }
    }.sumOf {
      val digits = it.filter { it.isDigit() }
      digits.first().digitToInt() * 10 + digits.last().digitToInt()
    }
}
