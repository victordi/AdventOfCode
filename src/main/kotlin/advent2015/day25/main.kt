package advent2015.day25

import utils.Input

val lines = Input.readInput()

fun main() {
  println(first())
}

fun first(): Long = run {
  var code = 20151125L
  val mult = 252533L
  val rem = 33554393L

  val row = 2978L
  val col = 3083L

  val rowFirst =  1 + row * (row - 1) / 2
  val colDiff = (col - 1) * (row + 1) + (col - 1) * (col - 2) / 2
  var n = rowFirst + colDiff
  while (n > 1) {
    code = (code * mult) % rem
    n--
  }

  code
}