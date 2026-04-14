package advent2017.day22

import utils.Direction
import utils.Input.readInput
import utils.Point
import utils.move

val input = readInput()
val grid = mutableSetOf<Point>()

fun main() {
  input.forEachIndexed { i, line ->
    line.forEachIndexed { j, char ->
      if (char == '#') {
        grid.add(i to j)
      }
    }
  }
  println(first())
  println(second())
}

fun first(): Int = run {
  val infected = grid.toMutableSet()
  var current = input.size / 2 to input[0].length / 2
  var direction = Direction.North
  var bursts = 0
  repeat(10000) {
    if (current in infected) {
      direction = direction.rotateRight()
      infected.remove(current)
    } else {
      direction = direction.rotateLeft()
      infected.add(current)
      bursts++
    }
    current = current.move(direction)
  }
  bursts
}

fun second(): Int = run {
  val infected = grid.toMutableSet()
  val weakened = mutableSetOf<Point>()
  val flagged = mutableSetOf<Point>()
  var current = input.size / 2 to input[0].length / 2
  var direction = Direction.North
  var bursts = 0
  repeat(10000000) {
    when (current) {
      in infected -> {
        direction = direction.rotateRight()
        infected.remove(current)
        flagged.add(current)
      }

      in weakened -> {
        weakened.remove(current)
        infected.add(current)
        bursts++
      }

      in flagged -> {
        direction = direction.rotateRight().rotateRight()
        flagged.remove(current)
      }

      else -> {
        direction = direction.rotateLeft()
        weakened.add(current)
      }
    }
    current = current.move(direction)
  }
  bursts
}
