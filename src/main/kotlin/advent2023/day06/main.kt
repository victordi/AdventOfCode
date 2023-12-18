package advent2023.day06

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun first(): Int = withInput { input ->
  val list = input.toList()
  val times = list.first().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }
  val distances = list[1].split(":")[1].trim().split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }
  val games = times.zip(distances)
  games.map { (time, distance) ->
    (1..time).count { it * (time - it) > distance }
  }.fold(1) { acc, nr -> acc * nr}
}

fun second(): Long = withInput { input ->
  val list = input.toList()
  val time = list[0].split(":")[1].filter { it.isDigit() }.toLong()
  val distance = list[1].split(":")[1].filter { it.isDigit() }.toLong()

  val start = (1..time).first { it * (time - it) > distance }
  val stop = (time downTo 1).first { it * (time - it) > distance }
  stop - start + 1
}
