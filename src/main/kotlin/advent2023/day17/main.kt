package advent2023.day17

import utils.AoCPart
import utils.Direction
import utils.Direction.East
import utils.Grid
import utils.Part1
import utils.Part2
import utils.Point
import utils.move

fun main() {
  println(solve(Part1))
  println(solve(Part2))
}

val grid = Grid.fromInput { it.map { c -> c.digitToInt() } }

data class State(val point: Point, val steps: Int, val direction: Direction, val score: Int)

fun solve(part: AoCPart): Int = run {
  val destination = grid.height - 1 to grid.width - 1
  val dist = mutableMapOf<State, Int>()
  grid.indexes.forEach { point ->
    Direction.values().forEach {
      (0..10).forEach { steps ->
        dist[State(point, steps, it, 0)] = Int.MAX_VALUE
      }
    }
  }
  val start = State((0 to 0), 0, East, 0)
  dist[start] = 0
  val priorityQueue: MutableList<State> = mutableListOf(start)
  while (priorityQueue.isNotEmpty()) {
    priorityQueue.sortBy { it.score }
    val (current, steps, direction, weight) = priorityQueue.first()
    priorityQueue.removeAt(0)
    if (current == destination) continue

    if (part == Part1 && steps < 3 || part == Part2 && steps < 10) {
      val next = current.move(direction)
      if (next in grid.indexes && dist[State(next, steps, direction, 0)]!! > grid[next] + weight) {
        dist[State(next, steps, direction, 0)] = grid[next] + weight
        priorityQueue.add(State(next, steps + 1, direction, dist[State(next, steps, direction, 0)]!!))
      }
    }
    if (part == Part1 || part == Part2 && steps >= 4) {
      listOf(direction.rotateRight(), direction.rotateLeft()).forEach { newDir ->
        val next = current.move(newDir)
        if (next in grid.indexes && dist[State(next, 0, newDir, 0)]!! > grid[next] + weight) {
          dist[State(next, 0, newDir, 0)] = grid[next] + weight
          priorityQueue.add(State(next, 1, newDir, dist[State(next, 0, newDir, 0)]!!))
        }
      }
    }
  }
  return dist.entries.filter { (state, _) ->
    state.point == destination
  }.minOf { (_, dist) -> dist }
}
