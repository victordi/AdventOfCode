package advent2023.day08

import AoCPart
import Input.withInput
import Part1
import Part2
import lcm

val map = withInput { input ->
  input.drop(2).map {
    val (key, nodes) = it.split(" = ")
    val (left, right) = nodes.drop(1).dropLast(1).filter { it != ' ' }.split(",")
    key to (left to right)
  }.toMap()
}

val steps = withInput { it.first().toCharArray().toList() }

fun main() {
  println(first())
  println(second())
}

fun first(): Int = "AAA".getSteps(Part1)

fun second(): Long = map.keys
  .filter { it.last() == 'A' }
  .map { it.getSteps(Part2).toLong() }
  .fold(1L) { acc, step -> lcm(acc, step) }

fun String.getSteps(part: AoCPart): Int = run {
  val (steps, node, isFound) = steps.fold(Triple(0, this, false)) { (step, node, isFound), direction ->
    if (isFound) Triple(step, node, true)
    else if (part == Part2 && node.last() == 'Z') Triple(step, node, true)
    else if (part == Part1 && node == "ZZZ") Triple(step, node, true)
    else {
      val next = if (direction == 'L') map[node]!!.first
      else map[node]!!.second
      Triple(step + 1, next, false)
    }
  }

  if (isFound) steps
  else steps + node.getSteps(part)
}
