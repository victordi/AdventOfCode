package advent2025.day08

import utils.Input.readInput
import utils.Point
import utils.extractLongs
import utils.pow


data class JunctionBox(val x: Long, val y: Long, val z: Long)

fun JunctionBox.distance(other: JunctionBox): Long =
  (x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2)

fun main() {
  val boxes = readInput().map { line -> extractLongs(line).let { (x, y, z) -> JunctionBox(x, y, z) } }
  val distances = mutableListOf<Triple<Int, Int, Long>>()
  boxes.forEachIndexed { i, boxA ->
    boxes.forEachIndexed { j, boxB ->
      if (i < j) {
        distances.add(Triple(i, j, boxA.distance(boxB)))
      }
    }
  }
  distances.sortBy { it.third }

  var steps = 1
  val taken = mutableSetOf<Point>()
  val graph = mutableMapOf<Int, MutableList<Int>>()
  while (true) {
    val current = distances[steps - 1]
    taken.add(current.first to current.second)
    graph.getOrPut(current.first) { mutableListOf() }.add(current.second)
    graph.getOrPut(current.second) { mutableListOf() }.add(current.first)

    val visited = mutableSetOf<Int>()
    val groups = mutableListOf<Set<Int>>()
    fun dfs(node: Int, group: MutableSet<Int>) {
      if (node in visited) return
      visited.add(node)
      group.add(node)
      graph[node]?.forEach { neighbor ->
        dfs(neighbor, group)
      }
    }

    graph.keys.forEach { node ->
      if (node !in visited) {
        val group = mutableSetOf<Int>()
        dfs(node, group)
        groups.add(group)
      }
    }

    if (steps == 1000) {
      groups.sortByDescending { it.size }
      groups.take(3).map { it.size }.take(3)
      val result = groups.take(3).fold(1L) { acc, list -> acc * list.size }
      println("Part 1 complete at step 1000: $result")
    }

    if (groups[0].size == boxes.size) {
      val (i, j) = taken.last()
      val result = boxes[i].x * boxes[j].x
      println("Part 2 completed at step $steps: $result")
      break
    }
    steps++
  }
}
