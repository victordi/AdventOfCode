package advent2024.day20

import utils.*

fun main() {
  println(first())
  println(solve(20))
}

val grid = readArrayFromInput()
val start = grid.indexOf('S')
val end = grid.indexOf('E')

val path: MutableList<Point> = mutableListOf()
fun Graph<Char>.dijkstra(start: Point): Graph<Int> {
  val dist = Array(size) { Array(size) { 100000 } }
  dist[start] = 0
  val priorityQueue: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()
  priorityQueue.add(start to 0)
  while (priorityQueue.isNotEmpty()) {
    val (pair, weight) = priorityQueue.first()
    path.add(pair)
    priorityQueue.removeAt(0)
    this.getAdjacent(pair).filter { getOrElse(it, '#') != '#' }.forEach { next ->
      if (dist[next] > weight + 1) {
        dist[next] = weight + 1
        priorityQueue.add(next to dist[next])
      }
    }
  }
  return dist
}

fun first(): Int = run {
  val distances = grid.dijkstra(start)
  val revDistances = grid.dijkstra(end)
  var cnt = 0
  val skips = mutableSetOf<Pair<Point, Point>>()
  val noCheatDist = distances[end]
  path.forEach {
    listOf(Direction.North, Direction.South, Direction.East, Direction.West).forEach { dir ->
      val next = it + dir.diff
      if (grid.getOrElse(next, '!') == '#') {
        val skip = next + dir.diff
        if (distances[it] + 2 + revDistances.getOrElse(skip, 100000) <= noCheatDist - 100) {
          skips.add(it to skip)
          cnt++
        }
      }
    }
  }
  skips.size
}

fun solve(maxSkips: Int): Int = run {
  val distances = grid.dijkstra(start)
  val revDistances = grid.dijkstra(end)
  val skips = mutableSetOf<Pair<Point, Point>>()
  val noCheatDist = distances[end]
  path.forEach { current ->
    grid.indexes()
      .filter { manhattanDistance(current, it) <= maxSkips }
      .filter { distances[current] + manhattanDistance(current, it) + revDistances[it] <= noCheatDist - 100 }
      .forEach { skips.add(current to it) }
  }
  skips.size
}
