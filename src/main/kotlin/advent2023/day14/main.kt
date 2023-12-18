package advent2023.day14

import utils.Grid
import utils.Point

val grid = Grid.fromInput()

fun main() {
  println(first())
  println(second())
}

fun first(): Int = grid.score()

fun Grid<Char>.score(): Int = columns.sumOf { it.score() }

fun List<Char>.score(): Int = run {
  var result = 0
  var lastWall = 0
  this.forEachIndexed { index, c ->
    when (c) {
      'O' -> {
        result += grid.height - lastWall
        lastWall += 1
      }

      '#' -> lastWall = index + 1
    }
  }
  result
}

fun second(): Int = run {
  val cycles = 1_000_000_000

  var final = grid

  val visited = mutableSetOf<Pair<List<List<Char>>, Int>>()

  var cyclesPerformed = 0
  val startOfCycle: Int
  while(true) {
    final = final.performCycle()
    cyclesPerformed++
    if (final.lines in visited.map { it.first }) {
      startOfCycle = visited.find { it.first == final.lines }?.second ?: -1
      break
    }
    visited.add(final.lines to cyclesPerformed)
  }

  val remainder = (cycles - startOfCycle) % (cyclesPerformed - startOfCycle)
  repeat(remainder) { final = final.performCycle() }

  final.foldIndexed(0) { (x, _), acc, c ->
    if (c == 'O') acc + final.height - x
    else acc
  }
}

fun Grid<Char>.performMovements(movements: List<Pair<Point, Point>>): Grid<Char> = mapIndexed { point, c ->
  if (point in movements.map { it.first } && point !in movements.map { it.second }) '.'
  else if (point in movements.map { it.second }) 'O'
  else c
}

fun Grid<Char>.performCycle(): Grid<Char> = run {
  var result = this
  val northMovements = mutableListOf<Pair<Point, Point>>()
  xRange.forEach {
    var lastWall = 0
    result.columns[it].forEachIndexed { index, c ->
      when (c) {
        'O' -> {
          if (index != lastWall) northMovements.add((index to it) to (lastWall to it))
          lastWall += 1
        }

        '#' -> lastWall = index + 1
      }
    }
  }
  result = result.performMovements(northMovements)

  val westMovements = mutableListOf<Pair<Point, Point>>()
  yRange.forEach {
    var lastWall = 0
    result.lines[it].forEachIndexed { index, c ->
      when (c) {
        'O' -> {
          if (index != lastWall) westMovements.add((it to index) to (it to lastWall))
          lastWall += 1
        }

        '#' -> lastWall = index + 1
      }
    }
  }
  result = result.performMovements(westMovements)

  val southMovements = mutableListOf<Pair<Point, Point>>()
  xRange.forEach {
    var lastWall = result.height - 1
    result.columns[it].reversed().forEachIndexed { i, c ->
      val index = height - 1 - i
      when (c) {
        'O' -> {
          if (index != lastWall) southMovements.add((index to it) to (lastWall to it))
          lastWall -= 1
        }

        '#' -> lastWall = index - 1
      }
    }
  }
  result = result.performMovements(southMovements)

  val eastMovements = mutableListOf<Pair<Point, Point>>()
  yRange.forEach {
    var lastWall = width - 1
    result.lines[it].reversed().forEachIndexed { i, c ->
      val index = width - i - 1
      when (c) {
        'O' -> {
          if (index != lastWall) eastMovements.add((it to index) to (it to lastWall))
          lastWall -= 1
        }

        '#' -> lastWall = index - 1
      }
    }
  }
  result.performMovements(eastMovements)
}