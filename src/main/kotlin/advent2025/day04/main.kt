package advent2025.day04

import utils.Direction
import utils.Grid
import utils.Point
import utils.move

fun main() {
  println(first())
  println(second())
}

val paperRolls = Grid.fromInput().elements.filter { it.value == '@' }.keys
fun Set<Point>.clear(): Set<Point> =
  filter { point -> Direction.values().map { point.move(it) }.count { it in this } < 4 }.toSet()

fun first(): Int = paperRolls.clear().size

fun second(): Int = (1..Int.MAX_VALUE).fold(Triple(paperRolls, 0, true)) { (current, total, go), _ ->
  if (!go) return total
  val toClear = current.clear()
  Triple(current - toClear, total + toClear.size, toClear.isNotEmpty())
}.second
