package advent2024.day22

import arrow.core.Tuple4
import utils.Input.withInput
import utils.zip4

val pruneMod = 16777216L
val steps = 2000

typealias MonkeySequence = Tuple4<Long, Long, Long, Long>

fun Long.secretStep(): Long = run {
  val step1 = (this * 64L).xor(this) % pruneMod
  val step2 = (step1 / 32).xor(step1) % pruneMod
  val step3 = (step2 * 2048).xor(step2) % pruneMod
  step3
}

fun Long.priceSteps(): List<Pair<Long, Long>> = run {
  var current = this
  val result = mutableListOf(0L to this % 10)
  repeat(steps) {
    val next = current.secretStep()
    result.add((next % 10 - current % 10) to next % 10)
    current = next
  }
  result
}

fun main() {
  println(first())
  println(second())
}

fun first(): Long = withInput { input ->
  input.toList()
    .map { it.toLong() }
    .sumOf { (1..steps).fold(it) { acc, _ -> acc.secretStep() } }
}

fun second(): Long = withInput { input ->
  val priceSteps = input.toList().map { it.toLong().priceSteps() }
  val map = mutableMapOf<MonkeySequence, Long>()
  priceSteps.forEach { changes ->
    val visited = mutableSetOf<MonkeySequence>()
    val differences = changes.map { it.first }.zip4()
    for (i in differences.indices) {
      if (differences[i] in visited) continue
      map[differences[i]] = (map[differences[i]] ?: 0) + changes[i + 3].second
      visited.add(differences[i])
    }
  }
  map.values.max()
}
