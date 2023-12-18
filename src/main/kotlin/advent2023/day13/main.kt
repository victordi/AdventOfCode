package advent2023.day13

import utils.Grid
import utils.Input.readInput
import utils.splitList

fun main() {
  println(first())
  println(second())
}

val grids = readInput().splitList { it.isEmpty() }.map { Grid.from(it) }

fun Grid<Char>.getReflectionScore(maxFaults: Int): Int {
  val reflectionLine = yRange.toList().find { it.isReflectionLine(this, maxFaults) }
  val reflectionColumn = xRange.toList().find { it.isReflectionColumn(this, maxFaults) }
  return reflectionColumn ?: (100 * (reflectionLine ?: 0))
}

fun Int.isReflectionLine(grid: Grid<Char>, maxFaults: Int): Boolean = run {
  if (this == 0 || this == grid.height) return false
  val (reflected, faults) = (this - 1 downTo 0).zip(this until grid.height).fold(true to 0) { (acc, faults), (l, r) ->
    if (!acc) false to faults
    else if (faults != 0) (grid.lines[l] == grid.lines[r]) to faults
    else grid.lines[l].zip(grid.lines[r]).count { (c1, c2) -> c1 != c2 }.let { (it <= 1) to it }
  }
  reflected && faults == maxFaults
}

fun Int.isReflectionColumn(grid: Grid<Char>, maxFaults: Int): Boolean = run {
  if (this == 0 || this == grid.width) return false
  val (reflected, faults) = (this - 1 downTo 0).zip(this until grid.width).fold(true to 0) { (acc, faults), (l, r) ->
    if (!acc) false to faults
    else if (faults != 0) (grid.columns[l] == grid.columns[r]) to faults
    else grid.columns[l].zip(grid.columns[r]).count { (c1, c2) -> c1 != c2 }.let { (it <= 1) to it }
  }
  reflected && faults == maxFaults
}

fun first(): Int = grids.sumOf { it.getReflectionScore(0) }

fun second(): Int = grids.sumOf { it.getReflectionScore(1) }