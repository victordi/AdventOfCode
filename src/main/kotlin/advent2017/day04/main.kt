package advent2017.day04

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun String.isValid(): Boolean = run {
  val words = split(" ")
  words.toSet().size == words.size
}

fun String.isValid2(): Boolean = run {
  val words = split(" ").map { it.toCharArray().sorted().joinToString("") }
  words.toSet().size == words.size
}

fun first(): Int = withInput { input ->
  input.toList().count { it.isValid() }
}

fun second(): Int = withInput { input ->
  input.toList().count { it.isValid2() }
}
