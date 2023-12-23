package advent2023.day23

import utils.Direction
import utils.Grid
import utils.Point
import utils.move
import utils.toDirection
import java.util.*

fun main() {
  println(first())
  println(second())
}

val grid = Grid.fromInput()

data class State(val node: Point, val steps: Int, val visited: Set<Point>, val direction: Direction? = null)

fun first(): Int = run {
  val start = 0 to grid.lines.first().indexOf('.')
  val stop = (grid.height - 1) to grid.lines.last().indexOf('.')

  val stack = Stack<State>()
  stack.add(State(start, 0, setOf()))
  val routes = mutableListOf<Int>()
  while (stack.isNotEmpty()) {
    val (node, steps, visited, direction) = stack.pop()
    grid.getAdjacentPoints(node).forEach { (next, c) ->
      if (next == stop) routes.add(steps + 1)
      else if (next !in visited && (direction == null || next == node.move(direction))) {
        when (c) {
          '.' -> stack.add(State(next, steps + 1, visited + node))
          '<', '>', '^', 'v' -> stack.add(State(next, steps + 1, visited + node, c.toDirection()))
        }
      }
    }
  }
  routes.max()
}

fun second(): Int = run {
  val start = 0 to grid.lines.first().indexOf('.')
  val stop = (grid.height - 1) to grid.lines.last().indexOf('.')
  val crossroads = grid.indexes.filter { point ->
    grid[point] != '#' && grid.getAdjacent(point).filter { it != '#' }.size > 2
  } + stop + start

  val crossroadsDistances = crossroads.associateWith { mutableSetOf<Pair<Point, Int>>() }
  for (crossroad in crossroads) {
    val queue = mutableListOf(crossroad to 0)
    val visited = mutableSetOf<Point>()
    while (queue.isNotEmpty()) {
      val (node, dist) = queue.removeAt(0)
      if (node in visited) continue
      visited.add(node)
      if (node in crossroads && node != crossroad) {
        crossroadsDistances[crossroad]!!.add(node to dist)
        crossroadsDistances[node]!!.add(crossroad to dist)
        continue
      }
      grid.getAdjacentPoints(node).forEach { (next, c) ->
        if (c != '#') queue.add(next to dist + 1)
      }
    }
  }

  val stack = Stack<State>()
  stack.add(State(start, 0, setOf()))
  val routes = mutableListOf<Int>()
  while (stack.isNotEmpty()) {
    val (node, steps, visited) = stack.pop()
    if (node in visited) continue
    val nextCrossroads = crossroadsDistances[node]!!
    nextCrossroads.forEach { (next, dist) ->
      if (next == stop) routes.add(steps + dist)
      else if (next !in visited) stack.add(State(next, steps + dist, visited + node))
    }
  }
  routes.max()
}
