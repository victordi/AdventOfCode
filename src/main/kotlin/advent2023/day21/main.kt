package advent2023.day21

import arrow.core.replicate
import utils.Grid
import utils.Point
import utils.getAdjacent
import utils.println

val grid = Grid.fromInput()
val start = grid.elements.filter { it.value == 'S' }.keys.first()
val distances = mutableMapOf(start to 0)

fun main() {
  println(part1())
  println(part2())
}

fun part1(): Long = run {
  grid.bfs(start)
  distances.values.filter { it <= 64 && it % 2 == 0 }.size.toLong()
}

/*
  let f be the function such that f(x) = number of plots that can be reached in the infinite grid by doing
  baseSteps + x * grid.width steps

  this means that the response for the second part is the result of f(26501365 / grid.width)

  thanks to the specific from of the input (square shape, start in middle, no walls in row and column of start) we can
  find the growth of f and then calculate f(n)
 */

fun part2(): Long = run {
  val totalSteps = 26501365
  val n = totalSteps / grid.width.toLong()
  val baseStep = generateSequence(1) { it + 1 }.first { it % grid.height == totalSteps % grid.height }
  grid.bfs(start, baseStep + 2 * grid.width)

  val (f0, f1, f2) = listOf(baseStep, baseStep + grid.width, baseStep + 2 * grid.width).map { steps ->
    distances.values.filter { it <= steps && it % 2 == steps % 2 }.size.toLong()
  }

  f0 + (f1 - f0) * n + (f2 - 2 * f1 + f0) * (n * (n - 1) / 2)
}

fun slowerPart2(): Long = run {
  grid.bfs(start, 65)
  val noExpansion = distances.values.filter { it <= 65 && it % 2 == 1 }.size.toLong()
  noExpansion.println // 3921
  distances.clear()

  val (expandedGrid1, expandedStart1) = grid.expand(start)
  expandedGrid1.bfs(expandedStart1, 65 + grid.width)
  val oneExpansion = distances.values.filter { it <= 65 + grid.width && it % 2 == 0 }.size.toLong()
  oneExpansion.println // 34908
  distances.clear()

  val (expandedGrid2, expandedStart2) = expandedGrid1.expand(expandedStart1)
  expandedGrid2.bfs(expandedStart2, 65 + 2 * grid.width)
  val twoExpansions = distances.values.filter { it <= 65 + 2 * grid.width && it % 2 == 1 }.size.toLong()
  twoExpansions.println // 96749

  val n = 26501365 / grid.width.toLong()
  noExpansion + (oneExpansion - noExpansion) * n + (twoExpansions - 2 * oneExpansion + noExpansion) * (n * (n - 1) / 2)
}

fun Grid<Char>.bfs(start: Point, maxDistance: Int = 64) {
  val visited = mutableSetOf<Point>()
  val stack = mutableListOf(start to 0)

  while (stack.isNotEmpty()) {
    val (current, steps) = stack.removeAt(0)
    if (current in visited) continue
    visited.add(current)
    if (steps > maxDistance) continue
    distances[current] = steps
    current.getAdjacent()
      .filter { (x, y) -> (x to y) !in visited && grid[x.mod(height) to y.mod(width)] != '#' }
      .forEach { stack.add(it to steps + 1) }
  }
}

fun Grid<Char>.expand(start: Point): Pair<Grid<Char>, Point> = run {
  val expandedStart = start.first + height to start.second + width
  lines
    .map { it.map { c -> if (c == 'S') '.' else c }.replicate(3).flatten() }
    .replicate(3)
    .flatten()
    .let { Grid.fromGrid(it) }
    .mapIndexed { pair, c -> if (pair == expandedStart) 'S' else c } to expandedStart
}
