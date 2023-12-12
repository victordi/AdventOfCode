package advent2023.day12

import Input.withInput
import arrow.core.replicate

fun main() {
  println(first())
  println(second())
}

val input: List<Pair<String, List<Int>>> = withInput { input ->
  input
    .map { line ->
      val (pattern, conditions) = line.split(" ")
      pattern to conditions.split(",").map { it.toInt() }
    }
    .toList()
}

val state = mutableMapOf<Triple<Int, Int, Int>, Long>()

fun first(): Long = input
  .sumOf {
    state.clear()
    it.getValidArrangements()
  }

fun second(): Long = input
  .sumOf { (pattern, conditions) ->
    state.clear()
    ("$pattern?".repeat(5).dropLast(1) to conditions.replicate(5).flatten()).getValidArrangements()
  }

fun Pair<String, List<Int>>.getValidArrangements(i: Int = 0, j: Int = 0, hashtags: Int = 0): Long {
  val key = Triple(i, j, hashtags)

  if (key in state) return state[key]!!
  if (i == first.length) {
    if (j == second.size && hashtags == 0) return 1
    if (j == second.size - 1 && second[j] == hashtags) return 1
    return 0
  }

  val scoreIfDot = {
    when (hashtags) {
      0 -> getValidArrangements(i + 1, j, 0)
      second.getOrNull(j) -> getValidArrangements(i + 1, j + 1, 0)
      else -> 0
    }
  }
  val scoreIfHashtag = { getValidArrangements(i + 1, j, hashtags + 1) }

  return when (first[i]) {
    '.' -> scoreIfDot()
    '#' -> scoreIfHashtag()
    '?' -> scoreIfDot() + scoreIfHashtag()
    else -> 0
  }.also { state[key] = it }
}