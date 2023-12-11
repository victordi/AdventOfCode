package advent2023.day11

import Input.withInput
import manhattanDistance

fun main() {
  println(solve(2))
  println(solve(1_000_000))
}

val map = withInput { input ->
  input.map { it.toList() }.toList()
}

val emptyLines = map.indices.mapIndexedNotNull { idx, i ->
  if (map[i].all { c -> c == '.' }) idx
  else null
}.toSet()

val emptyRows = map.indices.mapIndexedNotNull { idx, j ->
  val isEmpty = map.indices.fold(true) { acc, i ->
    acc && map[i][j] == '.'
  }
  if (isEmpty) idx else null
}.toSet()

fun solve(expansion: Int): Long = run {
  val galaxies = map.flatMapIndexed { i, chars ->
    chars.mapIndexedNotNull { j, c ->
      if (c == '#') i to j else null
    }
  }
  galaxies.mapIndexed { idx, (x1, y1) ->
    galaxies.drop(idx + 1).sumOf { (x2, y2) ->
      val p1 = x1 to y1
      val p2 = x2 to y2
      val xRange = if (x1 < x2) (x1..x2) else (x2..x1)
      val yRange = if (y1 < y2) (y1..y2) else (y2..y1)
      manhattanDistance(p1, p2).toLong() +
        yRange.intersect(emptyRows).size * (expansion - 1) +
        xRange.intersect(emptyLines).size * (expansion - 1)
    }
  }.sum()
}