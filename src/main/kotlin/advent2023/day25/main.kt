package advent2023.day25

import utils.Input

fun main() {
  println(first())
  println(second())
}

val map = mutableMapOf<String, MutableSet<String>>().apply {
  Input.readInput().forEach { line ->
    val (key, values) = line.split(": ")
    values.split(' ').forEach { value ->
      this[key] = (this[key]?.let { it.apply { add(value) } } ?: mutableSetOf(value))
      this[value] = (this[value]?.let { it.apply { add(key) } } ?: mutableSetOf(key))
    }
  }
}

val nodes = mutableSetOf(map.keys).flatten()
val paths = mutableMapOf<Pair<String, String>, List<String>>()

fun getPath(src: String, dest: String): List<String> = run {
  if (src to dest in paths) return@run paths[src to dest]!!
  if (dest to src in paths) return@run paths[dest to src]!!
  val queue = mutableListOf(src to listOf(src))
  val visited = mutableSetOf<String>()

  while (queue.isNotEmpty()) {
    val (node, path) = queue.removeAt(0)
    paths[src to node] = path
    paths[node to src] = path
    if (node == dest) return@run path
    if (node in visited) continue
    visited.add(node)
    map[node]!!.forEach { queue.add(it to path + it) }
  }

  emptyList()
}

fun first(): Int = run {
  val usedEdges = mutableMapOf<Pair<String, String>, Int>()

  for (i in nodes.indices) {
    for (j in i + 1 until nodes.size) {
      val (src, dest) = nodes[i] to nodes[j]
      if (src == dest) continue
      val path = getPath(src, dest)
      path.zipWithNext().forEach { (src, dest) ->
        val edge = if (src < dest) src to dest else dest to src
        usedEdges[edge] = usedEdges[edge]?.let { it + 1 } ?: 1
      }
    }
  }

  val toRemove = usedEdges.entries.sortedByDescending { it.value }.take(3).map { it.key }

  toRemove.forEach { (from, to) ->
    map[from]!!.remove(to)
    map[to]!!.remove(from)
  }

  var current = map.keys.first()
  val visited = mutableSetOf<String>()
  val queue = mutableListOf(current)
  while (queue.isNotEmpty()) {
    current = queue.removeAt(0)
    if (current in visited) continue
    visited.add(current)
    map[current]!!.forEach { queue.add(it) }
  }

  visited.size * (nodes.size - visited.size)
}

fun second() = "Advent of Code is done! Merry Christmas!"