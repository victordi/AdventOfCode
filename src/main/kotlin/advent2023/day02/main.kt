package advent2023.day02

import utils.Input.withInput
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

fun first(): Int = withInput { input ->
  input
    .toList()
    .mapIndexed { index, game ->
      val (maxRed, maxBlue, maxGreen) = game.maxColoredCubes()
      if (maxRed <= 12 && maxGreen <= 13 && maxBlue <= 14) index + 1 else 0
    }
    .sum()
}

fun second(): Int = withInput { input ->
  input
    .toList()
    .mapIndexed { _, game ->
      val (maxRed, maxBlue, maxGreen) = game.maxColoredCubes()
      maxRed * maxBlue * maxGreen
    }
    .sum()
}

fun String.maxColoredCubes(): Triple<Int, Int, Int> {
  val subGames = this.split(": ")[1].replace(";", ",").split(",").map { it.trim().splitTwo(" ") }
  val maxRed = subGames.maxCubes("red")
  val maxBlue = subGames.maxCubes("blue")
  val maxGreen = subGames.maxCubes("green")
  return Triple(maxRed, maxBlue, maxGreen)
}

fun List<Pair<String, String>>.maxCubes(color: String): Int = filter { it.second == color }.maxOf { it.first.toInt() }
