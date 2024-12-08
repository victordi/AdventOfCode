package advent2024.day08

import utils.*
import utils.Input.withInput

fun main() {
  println(solve(Part1))
  println(solve(Part2))
}

val matrix = withInput { input ->
  val list = input.toList()
  Array(list.size) { list[it].toCharArray().toTypedArray() }
}

fun solve(part: AoCPart): Int = run {
  val visited = mutableSetOf<Point>()
  val antennas = mutableMapOf<Char, MutableList<Point>>()
  matrix.arrayForEach { (i, j) ->
    val pastAntennas = antennas.getOrElse(matrix[i][j]) { mutableListOf() }
    for (antenna in pastAntennas) {
      val diffx = antenna.first - i
      val diffy = antenna.second - j
      listOf(Triple(i, j, -1), Triple(antenna.first, antenna.second, +1)).forEach { (a, b, factor) ->
        var (x, y) = a to b
        do {
          x += factor * diffx
          y += factor * diffy
          visited.add(x to y)
        } while (part == Part2 && matrix.getOrElse(x to y, '}') != '}')
      }
    }
    if (matrix[i][j] != '.') {
      antennas.getOrPut(matrix[i][j]) { mutableListOf() }.add(i to j)
    }
  }
  if (part == Part2) visited.addAll(antennas.values.flatten())
  visited.filter { (i, j) -> matrix.getOrElse(i to j, '}') != '}' }.size
}
