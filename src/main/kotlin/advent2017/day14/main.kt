package advent2017.day14

import advent2017.day10.knotHash
import utils.Input.readInput
import utils.Point
import utils.neighbours
import java.util.Stack

fun main() {
  println(first())
  println(second())
}

val key = readInput().first()

fun String.hexToBinary(): String = map {
  it.digitToInt(16).toString(2).padStart(4, '0')
}.joinToString("")

val used = mutableSetOf<Pair<Int, Int>>()

fun first(): Int = run {
  (0..127).sumOf { row ->
    val hashInput = "$key-$row"
    val knotHash = hashInput.knotHash()
    val binary = knotHash.hexToBinary()
    binary.forEachIndexed { index, c ->
      if (c == '1') {
        used.add(row to index)
      }
    }
    binary.count { it == '1' }
  }
}

fun second(): Int = run {
  var groups = 0
  val visited = mutableSetOf<Point>()
  while (visited.size < used.size) {
    val start = used.first { it !in visited }
    val stack = Stack<Point>()
    stack.add(start)
    visited.add(start)
    while (stack.isNotEmpty()) {
      val node = stack.pop()
      val neighbors = listOf(
        node.first - 1 to node.second,
        node.first + 1 to node.second,
        node.first to node.second - 1,
        node.first to node.second + 1
      ).filter { it in used }
      neighbors.reversed().forEach { next ->
        if (next !in visited) {
          stack.add(next)
          visited.add(next)
        }
      }
    }
    groups++
  }
  groups
}
