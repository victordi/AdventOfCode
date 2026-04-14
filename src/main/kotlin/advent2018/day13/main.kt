package advent2018.day13

import utils.Direction
import utils.Input.withInput
import utils.Point
import utils.move
import utils.toDirection

data class Cart(val position: Point, val direction: Direction, val intersection: Int) {
  fun move(): Cart = run {
    val newPosition = position.move(direction)
    val newDirection = when (cartMap[newPosition]) {
      '/' -> when (direction) {
        Direction.North -> Direction.East
        Direction.South -> Direction.West
        Direction.East -> Direction.North
        Direction.West -> Direction.South
        else -> error("Unreachable")
      }

      '\\' -> when (direction) {
        Direction.North -> Direction.West
        Direction.South -> Direction.East
        Direction.East -> Direction.South
        Direction.West -> Direction.North
        else -> error("Unreachable")
      }

      '+' -> when (intersection % 3) {
        0 -> direction.rotateLeft()
        1 -> direction
        2 -> direction.rotateRight()
        else -> error("Unreachable")
      }

      else -> direction
    }
    Cart(newPosition, newDirection, if (cartMap[newPosition] == '+') intersection + 1 else intersection)
  }
}

val cartMap = mutableMapOf<Point, Char>()
val carts = mutableListOf<Cart>()

fun parseInput(): Unit = withInput { input ->
  input.forEachIndexed { i, line ->
    line.forEachIndexed { j, char ->
      if (char in "^v<>") {
        val direction = char.toDirection()
        carts.add(Cart(i to j, direction, 0))
        cartMap[i to j] = if (char in "^v") '|' else '-'
      } else {
        cartMap[i to j] = char
      }
    }
  }
}

fun main() {
  parseInput()
  println(first())
  println(second())
}

fun first(): Point = run {
  val current = carts.toMutableList()
  while (true) {
    current.sortWith(compareBy({ it.position.first }, { it.position.second }))

    current.forEachIndexed { i, cart ->
      val moved = cart.move()

      if (current.indices.any { j -> j != i && current[j].position == moved.position }) {
        return moved.position
      }

      current[i] = moved
    }
  }
  error("Unreachable")
}

fun second(): Point {
  val current = carts.toMutableList()

  while (current.size > 1) {
    current.sortWith(compareBy({ it.position.first }, { it.position.second }))
    val crashed = mutableSetOf<Point>()

    current.forEachIndexed { i, cart ->
      if (cart.position !in crashed) {
        val moved = cart.move()

        val collisionIndex = current.indices.firstOrNull { j ->
          j != i && current[j].position == moved.position && current[j].position !in crashed
        }

        if (collisionIndex != null) {
          crashed.add(moved.position)
          crashed.add(cart.position)
        } else {
          current[i] = moved
        }
      }
    }

    current.removeAll { it.position in crashed }
  }

  return current.first().position
}
