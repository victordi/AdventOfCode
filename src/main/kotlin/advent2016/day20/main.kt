package advent2016.day20

import utils.Input.withInput
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

val ranges = withInput { input ->
  input
    .map { it.splitTwo("-") }
    .map { it.first.toLong()..it.second.toLong() }
    .toList()
}

fun first(): Long = ranges
  .sortedBy { it.first }
  .fold(0L to false) { (currentStart, found), range ->
    if (found || range.first > currentStart) currentStart to true
    else maxOf(currentStart, range.last + 1) to false
  }
  .first

fun second(): Long = ranges
  .sortedBy { it.first }
  .fold(0L to 0L) { (currentStart, count), range ->
    val newCount = if (range.first > currentStart) {
      count + (range.first - currentStart)
    } else count
    val newStart = maxOf(currentStart, range.last + 1)
    newStart to newCount
  }
  .let { (finalStart, count) ->
    count + (4294967295L - finalStart + 1)
  }
