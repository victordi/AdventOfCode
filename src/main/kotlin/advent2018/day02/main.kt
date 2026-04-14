package advent2018.day02

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

val input = readInput()

fun first(): Int = run {
  val nrs = input.map { it.groupBy { it }.values.map { it.size } }
  nrs.count { it.contains(2) } * nrs.count { it.contains(3) }
}

fun String.diff(other: String): Int {
  return this.zip(other).count { it.first != it.second }
}

fun second(): String = readInput().find { str ->
  input.any { other ->
    str != other && str.diff(other) == 1
  }
}?.let { str ->
  val other = input.first { str.diff(it) == 1 }
  str.zip(other).filter { it.first == it.second }.map { it.first }.joinToString("")
} ?: "N/A"
