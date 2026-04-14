package advent2017.day19

import utils.Direction
import utils.Input.readInput
import utils.Point
import utils.move

fun main() {
  solve()
}

val diagram: Map<Point, Char> = readInput().flatMapIndexed { i, line ->
  line.mapIndexed { j, char -> (i to j) to char }
}.toMap()

fun solve(): Unit = run {
  var current = diagram.filterKeys { it.first == 0 }.filterValues { it == '|' }.keys.first()
  var direction = Direction.South
  var route = ""
  var steps = 0
  while (true) {
    current = current.move(direction)
    steps++
    val char = diagram[current] ?: break
    if (char == ' ') break
    if (char.isLetter()) {
      route += char
    } else if (char == '+') {
      val possibleDirections = listOf(direction.rotateLeft(), direction.rotateRight())
      direction = possibleDirections.first { dir ->
        val nextPoint = current.move(dir)
        diagram[nextPoint] != null && diagram[nextPoint] != ' '
      }
    }
  }
  println("Route: $route")
  println("Steps: $steps")
}
