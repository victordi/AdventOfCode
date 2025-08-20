package advent2016.day06

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun first(): String = withInput { input ->
  val words = input.toList().map { it.toCharArray() }
  words.first().mapIndexed { index, _ ->
    words.map { it[index] }
      .groupingBy { it }
      .eachCount()
      .maxByOrNull { it.value }?.key ?: '-'
  }.joinToString("")
}

fun second(): String = withInput { input ->
  val words = input.toList().map { it.toCharArray() }
  words.first().mapIndexed { index, _ ->
    words.map { it[index] }
      .groupingBy { it }
      .eachCount()
      .minByOrNull { it.value }?.key ?: '-'
  }.joinToString("")
}
