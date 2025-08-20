package advent2016.day19

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

val elves = withInput { input -> input.first().toInt() }

fun List<Int>.nextRound(): List<Int> = run {
  val next = filterIndexed { i, _ -> i % 2 == 0 }
  if (this.size % 2 == 1) next.drop(1)
  else next
}

fun first(): Int = generateSequence((1..elves).toList()) { it.nextRound() }
  .first { it.size == 1 }
  .first()

fun second(): Int = (1 until elves)
  .fold(1) { acc, i ->
    (acc % i + 1).let { next -> if (next > (i + 1) / 2) next + 1 else next }
  }
