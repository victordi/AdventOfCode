package advent2017.day12

import utils.Input.readInput
import utils.addEdgeBi
import utils.dfs
import utils.newGraph

val graph = newGraph(2000)

fun main() {
  readInput().forEach { line ->
    val parts = line.split(" <-> ")
    val node = parts[0].toInt()
    val edges = parts[1].split(", ").map { it.toInt() }
    for (edge in edges) {
      graph.addEdgeBi(node, edge, 0)
    }
  }
  println(first())
  println(second())
}

fun first(): Int = run {
  val visited = mutableSetOf<Int>()
  graph.dfs(0, visited)
  visited.size
}

fun second(): Int = run {
  var groups = 0
  val visited = mutableSetOf<Int>()
  var current = 0
  while (true) {
    graph.dfs(current, visited)
    groups++
    current = (0 until 2000).firstOrNull { it !in visited } ?: break
  }
  groups
}
