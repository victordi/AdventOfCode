package advent2018.day05

import utils.Input.readInput
import utils.Input.withInput
import utils.println

fun main() {
  println(solve(polymer))
  println(second())
}

val polymer = readInput().first()
fun String.reduceOnce(): String = run {
  val sb = StringBuilder()
  var i = 0
  while (i < this.length) {
    if (i < this.length - 1 && this[i] != this[i + 1] && this[i].equals(this[i + 1], ignoreCase = true)) {
      i += 2
    } else {
      sb.append(this[i])
      i++
    }
  }
  sb.toString()
}

fun solve(poly: String): Int = run {
  var next = poly.reduceOnce()
  while (true) {
    val reduced = next.reduceOnce()
    if (reduced.length == next.length) {
      return reduced.length
    }
    next = reduced
  }
  -1
}

fun second(): Int = (0..25).minOfOrNull { i ->
  solve(polymer.filter { it != 'a' + i && it != 'A' + i })
} ?: -1
