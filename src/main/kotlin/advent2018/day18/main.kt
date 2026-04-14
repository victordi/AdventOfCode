package advent2018.day18

import utils.Grid
import utils.Input.withInput
import utils.Point
import utils.println

fun main() {
  println(first())
  println(second())
}

const val OPEN = '.'
const val TREES = '|'
const val LUMBERYARD = '#'

val grid = Grid.fromInput()

fun Grid<Char>.evolve(point: Point): Char = run {
  val adjacent = getAdjacentWithCorners(point)
  when (this[point]) {
    OPEN -> if (adjacent.count { it == TREES } >= 3) TREES else OPEN
    TREES -> if (adjacent.count { it == LUMBERYARD } >= 3) LUMBERYARD else TREES
    LUMBERYARD -> if (adjacent.count { it == LUMBERYARD } >= 1 && adjacent.count { it == TREES } >= 1) LUMBERYARD else OPEN
    else -> error("Unknown cell")
  }
}

fun Grid<Char>.advance(): Grid<Char> = mapIndexed { point, _ -> evolve(point) }

fun first(): Int = run {
  val finalGrid = (1..10).fold(grid) { current, _ -> current.advance() }
  finalGrid.count { it == TREES } * finalGrid.count { it == LUMBERYARD }
}

fun second(): Int = run {
  val seen = mutableMapOf<String, Int>()
  var current = grid

  for (i in 0 until 1000000000) {
    val key = current.toString()
    if (key in seen) {
      val cycleStart = seen[key]!!
      val cycleLength = i - cycleStart
      val remaining = (1000000000 - cycleStart) % cycleLength

      current = (1..remaining).fold(current) { curr, _ -> curr.advance() }
      return current.count { it == TREES } * current.count { it == LUMBERYARD }
    }
    seen[key] = i
    current = current.advance()
  }
  -1
}

