package advent2024.day19

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

val input = withInput { input ->
  val list = input.toList()
  val towels = list[0].split(",").map { it.trim() }
  towels to list.drop(2)
}

val memo = mutableMapOf<String, Long>()
fun String.countValid(towels: List<String>): Long =
  if (this in memo) memo[this]!!
  else if (isEmpty()) 1L
  else towels.fold(0L) { acc, towel ->
    acc + if (this.startsWith(towel)) drop(towel.length).countValid(towels) else 0
  }.also { memo[this] = it }


fun first(): Long = run {
  val (towels, rules) = input
  rules.sumOf { if (it.countValid(towels) != 0L) 1L else 0L }
}

fun second(): Long = run {
  val (towels, rules) = input
  rules.sumOf { it.countValid(towels) }
}
