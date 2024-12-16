package utils

import utils.Direction.East
import utils.Direction.North
import utils.Direction.South
import utils.Direction.West

enum class Direction(val diff: Point) {
  North(-1 to 0),
  NorthEast(-1 to 1),
  East(0 to 1),
  SouthEast(1 to 1),
  South(1 to 0),
  SouthWest(1 to -1),
  West(0 to -1),
  NorthWest(-1 to -1);

  private fun rotateRightOnce(): Direction = when (this) {
    North -> NorthEast
    NorthEast -> East
    East -> SouthEast
    SouthEast -> South
    South -> SouthWest
    SouthWest -> West
    West -> NorthWest
    NorthWest -> North
  }

  fun rotate(degree: Int = 45): Direction = run {
    require(degree % 45 == 0) { "Only accepting degrees that are multiple of 45" }
    (1..degree / 45).fold(this) { acc, _ -> acc.rotateRightOnce() }
  }

  fun rotateRight(): Direction = rotate(90)

  fun rotateLeft(): Direction = rotate(270)
}

fun Char.toDirection() = when (this) {
  '^' -> North
  'v' -> South
  '>' -> East
  '<' -> West
  else -> throw IllegalArgumentException("Not a valid direction $this")
}

fun Point.move(direction: Direction, steps: Int = 1): Point =
  (first + direction.diff.first * steps) to (second + direction.diff.second * steps)

fun Pair<Long, Long>.move(direction: Direction, steps: Long = 1): Pair<Long, Long> =
  (first + direction.diff.first * steps) to (second + direction.diff.second * steps)