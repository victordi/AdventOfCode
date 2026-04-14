package advent2017.day10

import utils.Input.readInput
import utils.extractInts

fun main() {
  println(first())
  println(second())
}

val lengths = extractInts(readInput().first())

fun first(): Int = run {
  val list = (0..255).toMutableList()
  var position = 0
  var skip = 0
  for (length in lengths) {
    val sublist = List(length) { list[(position + it) % list.size] }.reversed()
    for (i in sublist.indices) {
      list[(position + i) % list.size] = sublist[i]
    }
    position = (position + length + skip) % list.size
    skip++
  }
  list[0] * list[1]
}

fun String.knotHash(): String {
  val lengths = this.map { it.code } + listOf(17, 31, 73, 47, 23)
  val list = (0..255).toMutableList()
  var position = 0
  var skipSize = 0
  repeat(64) {
    for (length in lengths) {
      val sublist = List(length) { list[(position + it) % list.size] }.reversed()
      for (i in sublist.indices) {
        list[(position + i) % list.size] = sublist[i]
      }
      position = (position + length + skipSize) % list.size
      skipSize++
    }
  }
  return list.chunked(16).joinToString("") { chunk ->
    chunk.reduce { acc, i -> acc xor i }.toString(16).padStart(2, '0')
  }
}

fun second(): String = readInput().first().knotHash()
