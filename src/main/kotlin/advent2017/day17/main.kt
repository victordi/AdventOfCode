package advent2017.day17

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

fun spin(stepSize: Int, rounds: Int): List<Int> {
  val buffer = mutableListOf(0)
  var currentPosition = 0
  for (i in 1..rounds) {
    currentPosition = (currentPosition + stepSize) % buffer.size + 1
    buffer.add(currentPosition, i)
  }
  return buffer
}

val steps: Int = 2015

fun first(): Int = run {
  val buffer = spin(steps, 2017)
  buffer[(buffer.indexOf(2017) + 1)]
}

fun second(): Int = run {
  var currentPosition = 0
  var valueAfterZero = 0
  for (i in 1..50_000_000) {
    currentPosition = (currentPosition + steps) % i + 1
    if (currentPosition == 1) {
      valueAfterZero = i
    }
  }
  valueAfterZero
}
