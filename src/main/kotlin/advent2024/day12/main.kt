package advent2024.day12

import utils.*
import java.util.*

fun main() {
  println(first())
  println(second())
}

val grid = Grid.fromInput()

fun Point.dfs(garden: Char, globalVisited: MutableList<Point>): Pair<Int, Set<Point>> {
  var fences = 0
  val stack = Stack<Point>()
  val visited = mutableSetOf(this)
  val fencesV = mutableSetOf<Point>()
  stack.add(this)
  while (stack.isNotEmpty()) {
    val node = stack.pop()
    if (node !in fencesV) {
      fences += (4 - grid.getAdjacentPoints(node).count { (_, c) -> c == garden })
      fencesV.add(node)
    }
    grid.getAdjacentPoints(node).forEach { (next, c) ->
      if (c == garden && next !in globalVisited) {
        stack.add(next)
        globalVisited.add(next)
        visited.add(next)
      }
    }
  }
  return fences * visited.size to visited
}

fun first(): Int = run {
  var fences = 0
  val visited = mutableListOf<Point>()
  grid.indexes.forEach { point ->
    if (point !in visited) fences += point.dfs(grid[point], visited).first
  }
  fences
}

fun Set<Point>.sides(): Int = run {
  val edgesN = mutableListOf<Pair<Point, Int>>()
  val edgesS = mutableListOf<Pair<Point, Int>>()
  val edgesE = mutableListOf<Pair<Point, Int>>()
  val edgesW = mutableListOf<Pair<Point, Int>>()
  forEach { point ->
    val nextN = point + Direction.North.diff
    if (nextN !in this) edgesN.add(point to point.first - 1)

    val nextS = point + Direction.South.diff
    if (nextS !in this) edgesS.add(point to point.first + 1)

    val nextE = point + Direction.East.diff
    if (nextE !in this) edgesE.add(point to point.second + 1)

    val nextW = point + Direction.West.diff
    if (nextW !in this) edgesW.add(point to point.second - 1)
  }
  var extra = 0
  val e = edgesE.groupBy { it.second }
  e.values.forEach { list ->
    extra += list.sortedBy { it.first.first }.zipWithNext().count { (p1, p2) ->
      p1.first.first + 1 != p2.first.first
    }
  }

  val w = edgesW.groupBy { it.second }
  w.values.forEach { list ->
    extra += list.sortedBy { it.first.first }.zipWithNext().count { (p1, p2) ->
      p1.first.first + 1 != p2.first.first
    }
  }

  val n = edgesN.groupBy { it.second }
  n.values.forEach { list ->
    extra += list.sortedBy { it.first.second }.zipWithNext().count { (p1, p2) ->
      p1.first.second + 1 != p2.first.second
    }
  }

  val s = edgesS.groupBy { it.second }
  s.values.forEach { list ->
    extra += list.sortedBy { it.first.second }.zipWithNext().count { (p1, p2) ->
      p1.first.second + 1 != p2.first.second
    }
  }

  e.keys.size + w.keys.size + n.keys.size + s.keys.size + extra
}

fun second(): Int = run {
  var fences = 0
  val visited = mutableListOf<Point>()
  grid.indexes.forEach { point ->
    if (point !in visited) {
      val flowers = point.dfs(grid[point], visited).second
      fences += flowers.size * flowers.sides()
    }
  }
  fences
}
