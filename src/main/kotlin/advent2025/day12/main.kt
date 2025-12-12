package advent2025.day12

import utils.Input.readInput
import utils.splitList
import utils.splitTwo

fun main() {
  println(solve())
}

data class Region(val width: Int, val height: Int, val shapes: List<Int>)
data class Point(val x: Int, val y: Int)

val input = readInput().splitList { it.isEmpty() }
val shapeSizes = input.dropLast(1).map { line -> line.sumOf { it.count { c -> c == '#' } } }
val regions = input.last().map { line ->
  val parts = line.split(": ")
  val (w, h) = parts[0].splitTwo("x")
  val shapes = parts[1].split(" ").map { it.toInt() }
  Region(w.toInt(), h.toInt(), shapes)
}

fun Region.isValid(): Boolean = run {
  val neededArea = shapes.mapIndexed { idx, count -> count * shapeSizes[idx] }.sum()
  neededArea <= width * height
}

fun solve(): Int = regions.count { it.isValid() }
