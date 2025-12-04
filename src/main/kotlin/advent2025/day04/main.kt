package advent2025.day04

import utils.Grid
import utils.Point

fun main() {
  println(first())
  println(second())
}

var map = Grid.fromInput()

fun first(): Int = map.foldIndexed(0) { i, acc, _ ->
  if (map[i] == '@' && map.getAdjacentWithCorners(i.first, i.second).count { it == '@' } < 4) {
    acc + 1
  } else {
    acc
  }
}

fun second(): Int = run {
  var total = 0
  var changed = true
  while (changed) {
    val cleared: List<Point> = map.foldIndexed(listOf()) { i, acc, _ ->
      if (map[i] == '@' && map.getAdjacentWithCorners(i.first, i.second).count { it == '@' } < 4) {
        acc + i
      } else {
        acc
      }
    }
    changed = cleared.isNotEmpty()
    total += cleared.size
    map = map.mapIndexed { index, _ -> if (index in cleared) '.' else map[index] }
  }
  total
}
