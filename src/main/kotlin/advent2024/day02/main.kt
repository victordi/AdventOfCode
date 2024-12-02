package advent2024.day02

import utils.Input.withInput
import kotlin.math.abs

fun main() {
  println(first())
  println(second())
}

fun checkReport(report: List<Int>): Boolean {
  val zipped = report.zipWithNext()
  val maxDiff = zipped.maxOf { abs(it.first - it.second) }
  return maxDiff in 1..3 && (zipped.all { it.first < it.second } || zipped.all { it.first > it.second })
}

fun first(): Int = withInput { input ->
  input.map { report ->
    report.split(' ').map { it.toInt() }
  }.count { checkReport(it) }
}

fun permutations(list: List<Int>): List<List<Int>> =
  list.indices.map { index ->
    list.filterIndexed { i, _ -> i != index }
  }

fun second(): Int = withInput { input ->
  input.map { report ->
    report.split(' ').map { it.toInt() }
  }
    .map { permutations(it) }
    .count { it.any { checkReport(it) } }
}
