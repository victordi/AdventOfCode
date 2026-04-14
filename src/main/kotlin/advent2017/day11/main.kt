package advent2017.day11

import utils.Input.readInput

fun main() {
  solve()
}

val path = readInput().first()

fun solve() = run {
  var x = 0
  var y = 0
  var z = 0
  var max = 0
  var dist = 0
  for (dir in path.split(",")) {
    when (dir) {
      "n" -> {
        y += 1; z -= 1
      }

      "ne" -> {
        x += 1; z -= 1
      }

      "se" -> {
        x += 1; y -= 1
      }

      "sw" -> {
        x -= 1; z += 1
      }

      "nw" -> {
        x -= 1; y += 1
      }

      "s" -> {
        y -= 1; z += 1
      }
    }
    dist = (kotlin.math.abs(x) + kotlin.math.abs(y) + kotlin.math.abs(z)) / 2
    if (dist > max) {
      max = dist
    }
  }

  println(dist)
  println(max)
}
