package advent2016.day02

import utils.Input.withInput
import utils.get
import utils.getOrElse
import utils.parseArray

fun main() {
  println(first())
  println(second())
}

val keypad = listOf("123", "456", "789").parseArray { it.digitToInt() }
val keypad2 = listOf(
  "  1  ",
  " 234 ",
  "56789",
  " ABC ",
  "  D  "
).parseArray { it }

fun first(): Int = withInput { input ->
  var key = 0
  var (x, y) = 1 to 1
  input.toList().forEach { moves ->
    moves.forEach { move ->
      when (move) {
        'U' -> if (x > 0) x -= 1
        'D' -> if (x < 2) x += 1
        'L' -> if (y > 0) y -= 1
        'R' -> if (y < 2) y += 1
      }
    }
    key = key * 10 + keypad[x to y]
  }
  key
}

fun second(): String = withInput { input ->
  var key = ""
  var (x, y) = 2 to 0
  input.toList().forEach { moves ->
    moves.forEach { move ->
      val next = when (move) {
        'U' -> x - 1 to y
        'D' -> x + 1 to y
        'L' -> x to y - 1
        'R' -> x to y + 1
        else -> throw IllegalArgumentException("Unknown move: $move")
      }
      if (keypad2.getOrElse(next, ' ') != ' ') {
        x = next.first
        y = next.second
      }
    }
    key += keypad2[x to y]
  }

  key
}
