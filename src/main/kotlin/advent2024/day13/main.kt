package advent2024.day13

import utils.Input.withInput
import utils.extractInts

data class Machine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val x: Long, val y: Long)

const val part2Nr = 10000000000000L
val machines = withInput { input ->
  input.toList().joinToString("\n").split("\n\n")
    .map {
      val (a, b, c) = it.split("\n").map { extractInts(it) }
      Machine(a[0].toLong(), a[1].toLong(), b[0].toLong(), b[1].toLong(), c[0].toLong(), c[1].toLong())
    }
}

fun main() {
  println(first())
  println(second())
}

fun first(): Long = run {
  var cost = 0L
  machines.forEach { machine ->
    for (i in 0..(machine.x / machine.ax)) {
      val xrem = machine.x - i * machine.ax
      if (xrem % machine.bx == 0L) {
        val j = xrem / machine.bx
        if (i * machine.ay + j * machine.by == machine.y) {
          cost += i * 3 + j
          break
        }
      }
    }
  }
  cost
}

fun second(): Long = run {
  machines.sumOf { (ax, ay, bx, by, px, py) ->
    // aPresses * ax + bPresses * bx = x && aPresses * ax + bPresses * by = y
    val x = px + part2Nr
    val y = py + part2Nr

    val aPresses = (x * by - y * bx) / (ax * by - ay * bx)
    if ((x * by - y * bx) % (ax * by - ay * bx) == 0L) {
      val bPresses = (y - ay * aPresses) / by
      if ((y - ay * aPresses) % by == 0L) {
        aPresses * 3 + bPresses
      } else {
        0
      }
    } else {
      0
    }
  }
}
