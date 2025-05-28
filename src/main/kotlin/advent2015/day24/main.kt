package advent2015.day24

import utils.Input

val numbers = Input.readInput().map { it.toInt() }

fun List<Int>.combinations(size: Int): List<List<Int>> {
  val result = mutableListOf<List<Int>>()

  fun generate(current: List<Int>, remaining: List<Int>) {
    if (current.size > size) return
    if (remaining.isEmpty()) {
      result.add(current)
      return
    }

    val element = remaining.first()
    val rest = remaining.drop(1)

    generate(current + element, rest)
    generate(current, rest)
  }

  generate(emptyList(), this)
  return result
}

fun main() {
  println(solve(numbers.sum() / 3))
  println(solve(numbers.sum() / 4))
}

fun solve(target: Int): Long = (1..numbers.size)
  .asSequence()
  .map { size -> numbers.combinations(size).filter { it.sum() == target } }
  .first { it.isNotEmpty() }
  .minOf { it.fold(1L) { acc, i -> acc * i } }
