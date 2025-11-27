package advent2017.day06

import utils.Input.withInput

fun main() {
  println(solve())
  println(solve())
}

val banks = withInput { input ->
  input.first().split("\t").map { it.toInt() }.toMutableList()
}

fun redistribute() {
  val max = banks.max()
  val idx = banks.indexOf(max)
  banks[idx] = 0
  (1..max).forEach {
    banks[(idx + it) % banks.size]++
  }
}

fun solve(): Int = run {
  var steps = 0
  val configurations = mutableSetOf<List<Int>>()
  while (banks !in configurations) {
    configurations.add(banks.toList())
    redistribute()
    steps++
  }
  steps
}
