package advent2025.day11

import utils.AoCPart
import utils.Input.readInput
import utils.Part1
import utils.Part2

fun main() {
  val graph = parseGraph()
  println(solve(graph, "you", "out"))
  println(solve(graph, "svr", "out", Part2))
}

fun parseGraph(): Map<String, List<String>> {
  val input = readInput()
  val graph = mutableMapOf<String, MutableList<String>>()

  input.forEach { line ->
    val parts = line.split(": ")
    val node = parts[0]
    val connections = parts[1].split(" ")

    graph.computeIfAbsent(node) { mutableListOf() }.addAll(connections)
  }

  return graph
}

data class State(val current: String, val hasVisitedDac: Boolean, val hasVisitedFft: Boolean)

fun solve(graph: Map<String, List<String>>, start: String, end: String, part: AoCPart = Part1): Long {
  val memo = mutableMapOf<State, Long>()
  fun dfs(current: String, visited: Set<String>, hasVisitedDac: Boolean, hasVisitedFft: Boolean): Long {
    val state = State(current, hasVisitedDac, hasVisitedFft)
    if (memo.containsKey(state)) return memo[state]!!
    if (current == end) {
      if (part == Part2) {
        return if (visited.contains("dac") && visited.contains("fft")) 1
        else 0L
      }
      return 1L
    }

    val neighbors = graph[current] ?: return 0
    var pathCount = 0L

    for (neighbor in neighbors) {
      if (neighbor !in visited) {
        pathCount += dfs(
          neighbor, visited + current,
          hasVisitedDac || neighbor == "dac",
          hasVisitedFft || neighbor == "fft"
        )
      }
    }

    memo[state] = pathCount
    return pathCount
  }

  return dfs(start, emptySet(), hasVisitedDac = false, hasVisitedFft = false)
}
