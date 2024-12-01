package advent2024.day01

import utils.Input.withInput
import arrow.core.foldLeft
import utils.splitTwo
import kotlin.math.abs

fun main() {
  println(first())
  println(second())
}

fun parseInput(): Pair<List<Int>, List<Int>> = withInput { input ->
  val list = input.map { it.splitTwo("   ") { nr -> nr.toInt() } }.toList()
  list.map { it.first }.sorted() to list.map { it.second }.sorted()
}

val sortedInput = parseInput()

fun first(): Int = run {
  val (first, second) = sortedInput
  first.zip(second).sumOf {
    abs(it.first - it.second)
  }
}

fun second(): Int = run {
  val (first, second) = sortedInput
  first.sumOf { x ->
    x * second.count { it == x }
  }
}
