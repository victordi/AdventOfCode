package advent2024.day23

import utils.Input.readInput
import utils.splitTwo

val edges: List<Pair<String, String>> = readInput().map { it.splitTwo("-") }
val graph = mutableMapOf<String, MutableList<String>>().apply {
  edges.forEach { (a, b) ->
    computeIfAbsent(a) { mutableListOf() }.add(b)
    computeIfAbsent(b) { mutableListOf() }.add(a)
  }
}

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  val cycles = mutableSetOf<Set<String>>()

  fun dfs(node: String, start: String, path: List<String>) {
    if (path.size == 3) {
      if (graph[node]?.contains(start) == true) {
        cycles.add(path.toSet())
      }
      return
    }
    graph[node]?.forEach { neighbor ->
      if (neighbor !in path) {
        dfs(neighbor, start, path + neighbor)
      }
    }
  }

  graph.keys.forEach { dfs(it, it, listOf(it)) }

  cycles.filter { pcs -> pcs.any { it.startsWith('t') } }.size
}

fun second(): String = run {
  fun Set<String>.isLan() = all { node -> all { it == node || graph[node]?.contains(it) ?: false } }
  fun Set<String>.removeRandom(): List<Set<String>> = run {
    val result = mutableListOf<Set<String>>()
    for (i in this.indices) {
      val newSet = this - this.elementAt(i)
      result.add(newSet)
    }
    result
  }

  for (nodesToRemove in 1..100_000_000) {
    graph.keys.forEach { node ->
      val connected = setOf(node) + graph[node]!!
      val lans = (1..nodesToRemove).fold(listOf(connected)) { acc, _ ->
        acc.flatMap { it.removeRandom() }
      }
      for (lan in lans) {
        if (lan.isLan()) return lan.sorted().joinToString(",")
      }
    }
  }

  return "Clique not found"
}
