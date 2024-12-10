package advent2024.day10

import utils.Direction
import utils.Grid
import utils.Point
import utils.plus

fun main() {
  println(first())
  println(second())
}

val grid = Grid.fromInput { it.map { c -> c.digitToInt() } }
val trailheads = grid.indexes.filter { grid[it] == 0 }.map { it.trailhead() }

fun Point.trailhead(): List<Point> =
  if (grid[this] == 9) listOf(this)
  else listOf(Direction.North, Direction.East, Direction.West, Direction.South).fold(emptyList()) { acc, direction ->
    val next = grid.getOrNull(this + direction.diff) ?: -1
    if (grid[this] + 1 == next) acc + (this + direction.diff).trailhead()
    else acc
  }

fun first(): Int = trailheads.sumOf { it.toSet().size }
fun second(): Int = trailheads.sumOf { it.size }
