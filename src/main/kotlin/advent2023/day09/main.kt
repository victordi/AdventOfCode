package advent2023.day09

import utils.AoCPart
import utils.Input.withInput
import utils.Part1
import utils.Part2

fun main() {
  val entries = withInput { input -> input.toList().map { it.split(" ").map { nr -> nr.toInt() } } }
  println(entries.sumOf { it.getLast(Part1) })
  println(entries.sumOf { it.getLast(Part2) })
}

fun List<Int>.getLast(part: AoCPart): Int {
  val sequences = mutableListOf(this)
  while (true) {
    if (sequences.last().all { it == 0 }) break
    sequences.add(sequences.last().zipWithNext { a, b -> b - a })
  }
  return sequences.reversed().drop(1).fold(0) { acc, seq ->
    when(part) {
      Part1 -> seq.last() + acc
      Part2 -> seq.first() - acc
    }
  }
}
