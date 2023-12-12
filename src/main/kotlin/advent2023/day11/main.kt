package advent2023.day11

import Grid
import manhattanDistance

fun main() {
  println(solve(2))
  println(solve(1_000_000))
}

val grid = Grid.fromInput()

val emptyLines = grid.foldLinesIndexed(setOf<Int>()) { i, acc, line ->
  acc + if (line.all { it == '.' }) setOf(i) else emptySet()
}

val emptyColumns = grid.foldColumnsIndexed(setOf<Int>()) { i, acc, column ->
  acc + if (column.all { it == '.' }) setOf(i) else emptySet()
}

fun solve(expansion: Int): Long = run {
  val galaxies = grid.elements.filter { (_, c) -> c == '#' }.keys

  galaxies.mapIndexed { idx, (x1, y1) ->
    galaxies.drop(idx + 1).sumOf { (x2, y2) ->
      val p1 = x1 to y1
      val p2 = x2 to y2
      val xRange = if (x1 < x2) (x1..x2) else (x2..x1)
      val yRange = if (y1 < y2) (y1..y2) else (y2..y1)
      manhattanDistance(p1, p2).toLong() +
        yRange.intersect(emptyColumns).size * (expansion - 1) +
        xRange.intersect(emptyLines).size * (expansion - 1)
    }
  }.sum()
}