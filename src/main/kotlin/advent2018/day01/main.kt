package advent2018.day01

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

val input = readInput().map { it.toInt() }

fun first(): Int = input.sum()

fun second(): Int = run{
  val seen = mutableSetOf<Int>()
  var currentFreq = 0
  seen.add(currentFreq)

  generateSequence { input }
    .flatten()
    .forEach { change ->
      currentFreq += change
      if (currentFreq in seen) {
        return currentFreq
      }
      seen.add(currentFreq)
    }

  -1
}
