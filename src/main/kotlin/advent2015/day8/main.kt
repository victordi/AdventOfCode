package advent2015.day8

import utils.Input

val lines = Input.readInput()

fun String.memSize(): Int = run {
  var cnt = 0
  var i = 0
  while (i < this.length) {
    i += when (this[i]) {
      '\\' -> {
        when (this[i + 1]) {
          '\\' -> 2
          '\"' -> 2
          'x' -> 4
          else -> error("Unknown escape character")
        }
      }

      else -> 1
    }
    cnt++
  }
  cnt - 2
}

fun String.encodeSize(): Int = substring(1, length - 1).count { it == '\\' || it == '\"' } + length + 4


fun main() {
  println(first())
  println(second())
}

fun first(): Int = lines.sumOf {
  it.length - it.memSize()
}

fun second(): Int = lines.sumOf {
  it.encodeSize() - it.length
}