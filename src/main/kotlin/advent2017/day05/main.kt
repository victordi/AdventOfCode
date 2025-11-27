package advent2017.day05

import utils.Input.withInput
import kotlin.sequences.toMutableList

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  val jumps = withInput { input ->
    input.map { it.toInt() }.toMutableList()
  }
  var pos = 0
  var steps = 0
  while (pos in (0..jumps.size - 1)) {
    val jmp = jumps[pos]
    jumps[pos] = jumps[pos] + 1
    pos += jmp
    steps++
  }
  steps
}

fun second(): Int = run {
  val jumps = withInput { input ->
    input.map { it.toInt() }.toMutableList()
  }
  var pos = 0
  var steps = 0
  while (pos in (0..jumps.size - 1)) {
    val jmp = jumps[pos]
    jumps[pos] = if (jmp >= 3) jumps[pos] - 1 else jumps[pos] + 1
    pos += jmp
    steps++
  }
  steps
}
