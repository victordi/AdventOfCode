package advent2023.day16

import Direction
import Direction.East
import Direction.North
import Direction.South
import Direction.West
import Grid
import Point
import move

fun main() {
  println(first())
  println(second())
}

val grid = Grid.fromInput()

fun Grid<Char>.getEnergized(startingPoint: Pair<Point, Direction>): Int = run {
  val current = mutableListOf(startingPoint)
  val visited = mutableSetOf<Pair<Point, Direction>>()

  while(current.isNotEmpty()) {
    val copy = current.toList()
    for ((currentPos, dir) in copy) {
      val state = currentPos to dir
      current.remove(state)
      if (currentPos !in grid.indexes) break
      if (state in visited) break
      visited.add(state)
      when (grid.get(currentPos)) {
        '.' -> current.add(currentPos.move(dir) to dir)
        '/' -> {
          when (dir) {
            East -> current.add(currentPos.move(North) to North)
            West -> current.add(currentPos.move(South) to South)
            South -> current.add(currentPos.move(West) to West)
            North -> current.add(currentPos.move(East) to East)
          }
        }
        '\\' -> {
          when (dir) {
            East -> current.add(currentPos.move(South) to South)
            West -> current.add(currentPos.move(North) to North)
            South -> current.add(currentPos.move(East) to East)
            North -> current.add(currentPos.move(West) to West)
          }
        }
        '|' -> {
          when (dir) {
            East, West -> {
              current.add(currentPos.move(South) to South)
              current.add(currentPos.move(North) to North)
            }
            North, South -> current.add(currentPos.move(dir) to dir)
          }
        }
        '-' -> {
          when (dir) {
            North, South -> {
              current.add(currentPos.move(East) to East)
              current.add(currentPos.move(West) to West)
            }
            East, West -> current.add(currentPos.move(dir) to dir)
          }
        }
      }
    }
  }
  visited.map { it.first }.toSet().size
}

fun first(): Int = grid.getEnergized(Pair(0, 0) to East)

fun second(): Int = run {
  val starts = mutableListOf<Pair<Point, Direction>>()
  grid.yRange.forEach {
    starts.add(Pair(it, 0) to East)
    starts.add(Pair(it, grid.width - 1) to West)
  }

  grid.xRange.forEach {
    starts.add(Pair(0, it) to South)
    starts.add(Pair(grid.height - 1, it) to North)
  }

  starts.maxOf { grid.getEnergized(it) }
}