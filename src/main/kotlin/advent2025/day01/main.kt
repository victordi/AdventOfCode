package advent2025.day01

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun first(): Int = withInput { input ->
  input.toList()
    .map { it.first() to it.drop(1).toInt() }
    .fold(50 to 0) { (curr, step), (dir, dist) ->
      val next = when (dir) {
        'L' -> curr - dist
        'R' -> curr + dist
        else -> throw IllegalArgumentException("Unknown direction $dir")
      }
      val mod = next.mod(100)
      val inc = if (mod == 0) 1 else 0
      mod to step + inc
    }
    .second
}

fun second(): Int = withInput { input ->
  var current = 50
  var steps = 0
  input.toList()
    .map { it.first() to it.drop(1).toInt() }
    .forEach { (dir, dist) ->
      repeat(dist) {
        current = when (dir) {
          'L' -> current - 1
          'R' -> current + 1
          else -> throw IllegalArgumentException("Unknown direction $dir")
        }.mod(100)
        if (current == 0) {
          steps++
        }
      }
    }
  steps
}
