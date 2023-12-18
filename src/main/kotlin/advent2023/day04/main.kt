package advent2023.day04

import utils.Input.withInput
import utils.splitTwo
import kotlin.math.pow

fun main() {
  println(first())
  println(second())
}

fun first(): Long = withInput { input ->
  var result = 0L
  input.toList().forEach {
    result += it.scratchScore()
  }
  result
}

fun second(): Int = withInput { input ->
  val counts = mutableMapOf<Int, Int>()
  input.toList().forEachIndexed { index, s ->
    counts[index] = 1 + (counts[index] ?: 0)
    (index + 1..index + s.winningNumbers()).forEach { i ->
      counts[i] = (counts[i] ?: 0) + counts[index]!!
    }
  }
  counts.values.sum()
}

fun String.scratchScore(): Int {
  val wins = winningNumbers()
  return if (wins == 0) 0
  else 2.toDouble().pow(wins - 1).toInt()
}

fun String.winningNumbers(): Int {
  val (win, own) = split(": ")[1].splitTwo(" | ")
  val winNrs = win.split(" ").map { it.trim().toIntOrNull() ?: -3 }
  val ownNrs = own.split(" ").map { it.trim().toIntOrNull() ?: -2 }
  return ownNrs.count { winNrs.contains(it) }
}
