package advent2024.day16

import utils.*
import utils.Input.withInput

fun main() {
  println(solve())
}

val graph = withInput { input ->
  val list = input.toList()
  Array(list.size) { list[it].toCharArray().toTypedArray() }
}

var minDist = Int.MAX_VALUE
fun Graph<Char>.distances(start: Point, end: Point, startDirs: List<Direction>): Map<Pair<Point, Direction>, Int> {
  val dist = mutableMapOf<Pair<Point, Direction>, Int>()
  val priorityQueue: MutableList<Triple<Pair<Int, Int>, Int, Direction>> = mutableListOf()
  startDirs.forEach { startDir ->
    priorityQueue.add(Triple(start, 0, startDir))
  }
  while (priorityQueue.isNotEmpty()) {
    priorityQueue.sortBy { it.second }
    val (current, d, dir) = priorityQueue.first()
    priorityQueue.removeAt(0)
    if (current == end) minDist = Math.min(minDist, d)
    if (current to dir in dist) continue
    if (current to dir !in dist) dist[current to dir] = d
    val next = current + dir.diff
    if (getOrElse(next, '#') != '#') {
      priorityQueue.add(Triple(next, d + 1, dir))
    }
    priorityQueue.add(Triple(current, d + 1000, dir.rotateRight()))
    priorityQueue.add(Triple(current, d + 1000, dir.rotateLeft()))
  }
  return dist
}

fun solve(): Int = run {
  val start = graph.indexOf('S')
  val end = graph.indexOf('E')

  val distances = graph.distances(start, end, listOf(Direction.East))
  val min = minDist
  val distancesReverse = graph.distances(end, start, listOf(Direction.East, Direction.West, Direction.North, Direction.South))
  val pathPoints = mutableSetOf(start, end)
  graph.arrayForEach { point ->
    listOf(Direction.East, Direction.West, Direction.North, Direction.South).forEach { dir ->
      val dist = distances[point to dir] ?: -1
      listOf(Direction.East, Direction.West, Direction.North, Direction.South).forEach { revDir ->
        val revDist = distancesReverse[point to revDir] ?: -1
        if (dist + revDist == min) {
          pathPoints.add(point)
        }
      }
    }
  }

  println(min)
  pathPoints.size
}
