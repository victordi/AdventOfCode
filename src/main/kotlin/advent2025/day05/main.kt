package advent2025.day05

import utils.Input.readInput
import utils.splitList
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

val input = readInput().splitList { it.isEmpty() }
val ranges = input.first().map { it.splitTwo("-") }.map { (a, b) -> a.toLong()..b.toLong() }
val ids = input.drop(1).first().map { it.toLong() }

fun first(): Int = ids.filter { id -> ranges.any { id in it } }.size

fun second(): Long = ranges
  .sortedBy { it.first }.fold(0L to -1L) { (total, leftMost), range ->
    val start = maxOf(range.first, leftMost + 1)
    val inc = if (start <= range.last) {
      range.last - start + 1
    } else 0L
    total + inc to maxOf(leftMost, range.last)
  }.first
