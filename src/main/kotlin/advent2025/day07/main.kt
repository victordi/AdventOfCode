package advent2025.day07

import utils.Grid
import utils.Point

fun main() {
  println(first())
  println(second())
}

val grid = Grid.fromInput()

fun first(): Int = run {
  grid.lines.indices.fold(setOf(grid.indexOf('S')) to 0) { (beams, total), line ->
    val (newBeams, splits) = beams.fold(emptySet<Point>() to 0) { (set, splits), beam ->
      val next = beam.first + 1 to beam.second
      if (grid.getOrNull(next) == '^') {
        set + (setOf(next.first to next.second - 1, next.first to next.second + 1)) to splits + 1
      } else {
        set + setOf(next) to splits
      }
    }
    newBeams to total + splits
  }.second
}

val dp = mutableMapOf<Point, Long>()
fun timelines(pos: Point): Long {
  if (pos.first == grid.lines.size - 1) return 1L
  if (dp.containsKey(pos)) return dp[pos]!!
  val next = pos.first + 1 to pos.second
  val result = if (grid.getOrNull(next) == '^') {
    val left = next.first to next.second - 1
    val right = next.first to next.second + 1
    timelines(left) + timelines(right)
  } else {
    timelines(next)
  }
  dp[pos] = result
  return result
}

fun second(): Long = timelines(grid.indexOf('S'))
