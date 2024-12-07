package advent2024.day07

import utils.AoCPart
import utils.Input.withInput
import utils.Part1
import utils.Part2

fun main() {
  println(solve(Part1))
  println(solve(Part2))
}

val input = withInput { input ->
  input.map {
    val split = it.split(":")
    split[0].toLong() to split[1].drop(1).split(" ").map { it.toLong() }
  }
    .toList()
}

fun Pair<Long, List<Long>>.isValidEquation(current: Long, part: AoCPart): Boolean {
  val (total, rest) = this
  if (rest.isEmpty()) return total == current
  return (total to rest.drop(1)).isValidEquation(current + rest.first(), part) ||
      (total to rest.drop(1)).isValidEquation(current * rest.first(), part) ||
      (part == Part2 && (total to rest.drop(1)).isValidEquation(
        (current.toString() + rest.first().toString()).toLong(), part
      ))
}

fun solve(part: AoCPart): Long = run {
  input.filter { (total, list) ->
    (total to list.drop(1)).isValidEquation(list.first(), part)
  }.sumOf { it.first }
}
