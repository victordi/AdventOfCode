package advent2016.day17

import advent2016.day05.md5
import utils.Input.withInput
import utils.Point
import utils.plus

fun main() {
  println(first())
  println(second())
}

val password = withInput { it.first() }
val start = 0 to 0
val vault = 3 to 3
val paths = mutableListOf<String>()

fun String.openDirections(): List<Pair<Point, Char>> = run {
  val hash = this.md5().take(4)
  val directions = mutableListOf<Pair<Point, Char>>()
  if (hash[0] in 'b'..'f') directions.add((-1 to 0) to 'U')
  if (hash[1] in 'b'..'f') directions.add((1 to 0) to 'D')
  if (hash[2] in 'b'..'f') directions.add((0 to -1) to 'L')
  if (hash[3] in 'b'..'f') directions.add((0 to 1) to 'R')
  directions
}

fun traverse(current: Point, path: String): Unit = run {
  if (current.first < 0 || current.second < 0 || current.first > 3 || current.second > 3) return
  if (current == vault) {
    paths.add(path)
    return
  }
  val openDoors = (password + path).openDirections()
  openDoors.forEach { (next, dir) ->
    val nextPath: String = path + dir
    val nextPoint: Point = current + next
    traverse(nextPoint, nextPath)
  }
}

fun first(): String = run {
  traverse(start, "")
  paths.minByOrNull { it.length }!!
}

fun second(): Int = run {
  paths.maxOf { it.length }
}
