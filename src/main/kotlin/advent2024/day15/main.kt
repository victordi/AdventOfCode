package advent2024.day15

import utils.*
import utils.Input.withInput

val input = withInput { input ->
  val (grid, moves) = input.joinToString("\n").splitTwo("\n\n")
  val grid2 = grid.split("\n")
  val directions = moves.split("\n").joinToString(" ").mapNotNull {
    try {
      it.toDirection()
    } catch (_: Exception) {
      null
    }
  }
  val matrix = Array(grid2.size) { grid2[it].toCharArray().toTypedArray() }
  matrix to directions
}
val input2 = withInput { input ->
  val (grid, moves) = input.joinToString("\n").splitTwo("\n\n")
  val grid2 = grid.split("\n")
  val directions = moves.split("\n").joinToString(" ").mapNotNull {
    try {
      it.toDirection()
    } catch (_: Exception) {
      null
    }
  }
  val matrix = Array(grid2.size) {
    val expanded = grid2[it].map { c ->
      when (c) {
        '@' -> "@."
        'O' -> "[]"
        else -> c.toString() + c
      }
    }.joinToString("")
    expanded.toCharArray().toTypedArray()
  }
  matrix to directions
}

fun main() {
  println(first())
  println(second())
}

fun Array<Array<Char>>.score(c: Char): Int {
  var result = 0
  arrayForEach { (i, j) ->
    if (this[i][j] == c) result += 100 * i + j
  }
  return result
}

fun first(): Int = run {
  val (matrix, directions) = input
  var current = matrix.indexOf('@')
  matrix[current] = '.'
  directions.forEach { dir ->
    val next = current + dir.diff
    val nextSpot = matrix.getOrElse(next, '#')
    if (nextSpot == '.') {
      current = next
    } else if (nextSpot == 'O') {
      var final = next
      while (matrix.getOrElse(final, '#') == 'O') {
        final += dir.diff
      }
      if (matrix.getOrElse(final, '#') == '.') {
        current = next
        matrix[next] = '.'
        matrix[final] = 'O'
      }
    }
  }
  matrix.score('O')
}

fun Graph<Char>.canMove(box: Point, direction: Direction): Boolean {
  val lr = when (getOrElse(box, '#')) {
    '[' -> box + Direction.East.diff
    ']' -> box + Direction.West.diff
    else -> throw IllegalArgumentException()
  }

  val nextChar = getOrElse(box + direction.diff, '#')
  val nextMovable = nextChar == '.' || (nextChar in "[]" && canMove(box + direction.diff, direction))
  val sideChar = getOrElse(lr + direction.diff, '#')
  val sideMovable = sideChar == '.' || (sideChar in "[]" && canMove(lr + direction.diff, direction))

  return nextMovable && sideMovable
}

fun Graph<Char>.move(box: Point, direction: Direction): Boolean {
  if (!canMove(box, direction)) return false
  val lr = when (getOrElse(box, '#')) {
    '[' -> box + Direction.East.diff
    ']' -> box + Direction.West.diff
    else -> throw IllegalArgumentException()
  }

  if (getOrElse(box + direction.diff, '#') in "[]") move(box + direction.diff, direction)
  if (getOrElse(lr + direction.diff, '#') in "[]") move(lr + direction.diff, direction)

  this[box + direction.diff] = this[box]
  this[lr + direction.diff] = this[lr]
  this[box] = '.'
  this[lr] = '.'
  return true
}

fun second(): Int = run {
  val (matrix, directions) = input2
  var current = matrix.indexOf('@')
  matrix[current] = '.'

  directions.forEach { dir ->
    var next = current + dir.diff
    when (dir) {
      Direction.East, Direction.West -> {
        while (matrix.getOrElse(next, '#') in "[]") {
          next += dir.diff
        }
        if (matrix.getOrElse(next, '#') == '.') {
          var prev = next - dir.diff
          while (prev != current) {
            matrix[prev + dir.diff] = matrix[prev]
            matrix[prev] = '.'
            prev -= dir.diff
          }
          current += dir.diff
        }
      }

      else -> {
        if (matrix.getOrElse(next, '#') == '.' || (matrix.getOrElse(next, '#') in "[]" && matrix.move(next, dir))) {
          current += dir.diff
        }
      }
    }

  }
  matrix.score('[')
}