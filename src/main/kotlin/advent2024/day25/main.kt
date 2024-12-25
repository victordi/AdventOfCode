package advent2024.day25

import arrow.core.identity
import utils.Input.withInput
import utils.parseArray

fun main() {
  println(first())
  println(second())
}

fun Array<Array<Char>>.isKey() = this[0].all { it == '.' }
fun Array<Array<Char>>.heights(): List<Int> = this[0].indices.fold(listOf()) { acc, j ->
  acc + indices.fold(-1) { acc2, i -> acc2 + if (this[i][j] == '#') 1 else 0 }
}
fun isFitting(key: List<Int>, lock: List<Int>): Boolean = key.zip(lock).all { it.first + it.second <= 5 }

fun first(): Int = withInput { input ->
  val schematics = input
    .joinToString("\n")
    .split("\n\n")
    .map { it.split("\n") }
    .map { it.parseArray(::identity) }
  val keys = schematics.filter { it.isKey() }.map { it.heights() }
  val locks = schematics.filter { !it.isKey() }.map { it.heights() }

  keys.sumOf { key ->
    locks.count { lock -> isFitting(key, lock) }
  }
}

fun second() = "Merry Christmas!"
