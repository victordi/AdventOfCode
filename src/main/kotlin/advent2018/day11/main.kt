package advent2018.day11

import utils.Input.readInput
import utils.Point
import utils.cartesianProduct

fun main() {
  println(first())
  println(second())
}

val serialNumber = readInput().first().toInt()

fun Point.score(): Int = run {
  val (x, y) = this
  val rackId = x + 10L
  val powerLevel = (rackId * y + serialNumber) * rackId
  val hundredsDigit = (powerLevel / 100) % 10
  hundredsDigit.toInt() - 5
}

val cache = mutableMapOf<Triple<Int, Int, Int>, Int>()

fun Point.totalScore(size: Int): Int = run {
  val prev = Triple(first, second, size - 1)
  val result = if (prev in cache) {
    val prevScore = cache[prev]!!
    val addedRow = (0 until size).fold(0) { acc, j -> acc + Point(first + size - 1, second + j).score() }
    val addedCol = (0 until size - 1).fold(0) { acc, i -> acc + Point(first + i, second + size - 1).score() }
    prevScore + addedRow + addedCol
  } else (0 until size).fold(0) { acc, i ->
    (0 until size).fold(acc) { acc2, j ->
      acc2 + Point(first + i, second + j).score()
    }
  }
  cache[Triple(first, second, size)] = result
  result
}

val range: (Int) -> IntRange = { size ->
  1..(300 - size + 1)
}

fun first(): Point = range(3).cartesianProduct(range(3)).maxBy { it.totalScore(3) }

fun second(): Triple<Int, Int, Int> = run {
  val (point, size, _) = (1..22).map { size -> // from size 23 on all scores are negative
    val maxScorePoint = range(size).cartesianProduct(range(size)).maxBy { it.totalScore(size) }
    Triple(maxScorePoint, size, maxScorePoint.totalScore(size))
  }.maxBy { it.third }
  Triple(point.first, point.second, size)
}
