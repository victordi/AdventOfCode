package advent2025.day06

import utils.Input.readInput
import utils.extractLongs

val lines = readInput()
val numbers = lines.dropLast(1).map { extractLongs(it) }
val signs = lines.last().split(' ').map { it.trim() }.filter { it.isNotEmpty() }

fun main() {
  println(first())
  println(second())
}

fun first(): Long = signs.indices.fold(0L) { total, idx ->
  val nums = numbers.map { it[idx] }
  val current = when (signs[idx]) {
    "+" -> nums.sum()
    "*" -> nums.fold(1L) { acc, n -> acc * n }
    else -> throw IllegalArgumentException("Unknown sign ${signs[idx]}")
  }
  total + current
}

fun second(): Long = run {
  val range = (0..lines.size - 2)
  var total = 0L
  var i = 0
  while (i < lines.first().length) {
    val maxLen = range.map { lines[it].drop(i).takeWhile { c -> c != ' ' }}.maxOf { it.length }
    val nums = range.map { lines[it].drop(i).take(maxLen).replace(" ", "!") }
    val flippedNums = (0 until maxLen).map { pos ->
      nums.joinToString("") { it[pos].toString() }.filter { it != '!' }.toLong()
    }
    val current = when (lines.last()[i]) {
      '+' -> flippedNums.sum()
      '*' -> flippedNums.fold(1L) { acc, n -> acc * n }
      else -> throw IllegalArgumentException("Unknown sign ${lines.last()[i]}")
    }
    total += current
    i += maxLen + 1
  }
  total
}
