package advent2023.day03

import Input.withInput
import Point
import getAdjacentWithCorners

fun main() {
  println(first())
  println(second())
}

fun first(): Long = withInput { input ->
  var result = 0L
  val list = input.map { it.toCharArray().toTypedArray() }.toList().toTypedArray()
  list.forEachIndexed { idx, line ->
    var nr = 0
    val indexes = mutableListOf<Int>()
    line.forEachIndexed { index, c ->
      if (c.isDigit()) {
        nr = nr * 10 + c.digitToInt()
        indexes += index
      } else if (c == '.') {
        var stop = false
        indexes.forEach { idx2 ->
          if (!stop) {
            if (list.getAdjacentWithCorners(idx, idx2).any { (x, y) -> !list[x][y].isDigit() && list[x][y] != '.' }) {
              result += nr
              stop = true
            }
          }
        }
        nr = 0
        indexes.clear()
      } else {
        result += nr
        nr = 0
        indexes.clear()
      }
    }
    if (nr != 0) {
      var stop = false
      indexes.forEach { idx2 ->
        if (!stop) {
          if (list.getAdjacentWithCorners(idx, idx2).any { (x, y) -> !list[x][y].isDigit() && list[x][y] != '.' }) {
            result += nr
            stop = true
          }
        }
      }
      nr = 0
      indexes.clear()
    }
  }
  result
}

fun second(): Long = withInput { input ->
  val gears = mutableMapOf<Point, List<Int>>()
  val list = input.map { it.toCharArray().toTypedArray() }.toList().toTypedArray()
  list.forEachIndexed { idx, line ->
    var nr = 0
    val indexes = mutableListOf<Int>()
    line.forEachIndexed { index, c ->
      if (c.isDigit()) {
        nr = nr * 10 + c.digitToInt()
        indexes += index
      } else {
        var stop = false
        indexes.forEach { idx2 ->
          if(!stop) {
            val adjSymbols = list.getAdjacentWithCorners(idx, idx2)
              .filter { (x, y) -> !list[x][y].isDigit() && list[x][y] != '.' }
            if (adjSymbols.isNotEmpty()) {
              adjSymbols.first().let { (x, y) ->
                if (list[x][y] == '*') {
                  gears[x to y] = gears[x to y]?.plus(nr) ?: listOf(nr)
                  stop = true
                }
              }
            }
          }
        }
        nr = 0
        indexes.clear()
      }
    }
    if (nr != 0) {
      var stop = false
      indexes.forEach { idx2 ->
        if(!stop) {
          val adjSymbols = list.getAdjacentWithCorners(idx, idx2)
            .filter { (x, y) -> !list[x][y].isDigit() && list[x][y] != '.' }
          if (adjSymbols.isNotEmpty()) {
            adjSymbols.first().let { (x, y) ->
              if (list[x][y] == '*') {
                gears[x to y] = gears[x to y]?.plus(nr) ?: listOf(nr)
                stop = true
              }
            }
          }
        }
      }
      nr = 0
      indexes.clear()
    }
  }
  gears.filter { (_ ,v) -> v.size == 2 }.mapValues { (_, v) -> v.fold(1L) {acc, i -> acc * i.toLong()} }.values.sum()
}