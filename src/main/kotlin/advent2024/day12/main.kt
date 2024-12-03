package advent2024.day12

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun first(): Int = withInput { input ->
  input.toList().size
}

fun second(): Int = withInput { input ->
  input.toList().size
}
