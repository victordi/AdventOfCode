package advent2016.day22

import utils.Input.withInput
import utils.prettyPrint

data class Node(val size: Int, val used: Int, val avail: Int, val usePercent: Int)

val nodes: Array<Array<Node>> = Array(37) { Array(28) { Node(0, 0, 0, 0) } }

fun readInput() = withInput { input ->
  input.drop(2).forEach { line ->
    val regex = Regex("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)%")
    val matchResult = regex.matchEntire(line) ?: throw IllegalArgumentException("Invalid line format: $line")
    val (x, y, size, used, avail, usePercent) = matchResult.destructured
    nodes[x.toInt()][y.toInt()] = Node(size.toInt(), used.toInt(), avail.toInt(), usePercent.toInt())
  }
}

fun main() {
  readInput()
  println(first())
  println(second())
}

fun Array<Array<Node>>.viableNodes(x: Int, y: Int): Int = run {
  var count = 0
  for (i in nodes.indices) {
    for (j in nodes[i].indices) {
      if (i == x && j == y) continue
      if (nodes[x][y].used > 0 && nodes[x][y].used <= nodes[i][j].avail) count++
    }
  }
  count
}

fun first(): Int = run {
  var sum = 0
  for (i in nodes.indices) {
    for (j in nodes[i].indices) {
      sum += nodes.viableNodes(i, j)
    }
  }
  sum
}

fun second(): Int = run {
  nodes.map { it.map { node -> if (node.used == 0) 'X' else if (node.used <= 100) '.' else '|' }.toTypedArray() }.toTypedArray().prettyPrint()

  // from empty node to (35, 0) = above the end -> 14 steps up to be above the wall + 24 steps left to get to y = 0 + 28 steps down
  val fromEmpty = 14 + 24 + 28 + 1

  fromEmpty + 35 * 5
}
