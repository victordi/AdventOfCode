package advent2023.day08

import utils.AoCPart
import utils.Input.withInput
import utils.Part1
import utils.Part2
import utils.lcm

val map = withInput { input ->
  input.drop(2).map { path ->
    val (key, left, right) = Regex("[A-Z]{3}")
      .findAll(path)
      .map { it.value }
      .toList()
    key to (left to right)
  }.toMap()
}

val steps = withInput { it.first().toList() }

fun main() {
  println(first())
  println(second())
}

fun first(): Int = "AAA".getSteps(Part1)

fun second(): Long = map.keys
  .filter { it.last() == 'A' }
  .map { it.getSteps(Part2).toLong() }
  .fold(1L) { acc, step -> lcm(acc, step) }

fun String.getSteps(part: AoCPart): Int = steps.size +
  steps
    .foldIndexed(this) { idx, node, direction ->
      if (node.isFinalNode(part)) return idx
      map.nextNode(node, direction)
    }
    .getSteps(part)

fun String.isFinalNode(part: AoCPart): Boolean = when (part) {
  Part1 -> equals("ZZZ")
  Part2 -> last() == 'Z'
}

fun Map<String, Pair<String, String>>.nextNode(node: String, direction: Char): String =
  if (direction == 'L') get(node)!!.first
  else get(node)!!.second
