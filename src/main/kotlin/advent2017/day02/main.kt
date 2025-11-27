package advent2017.day02

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun first(): Int = withInput { input ->
  input.sumOf {
    val nrs = it.split("\t").mapNotNull { line -> line.trim().toIntOrNull() }
    nrs.max() - nrs.min()
  }
}

fun second(): Int = withInput { input ->
  input.sumOf {
    val nrs = it.split("\t").mapNotNull { line -> line.trim().toIntOrNull() }
    nrs.firstNotNullOf { nr -> nrs.find { nr2 -> nr != nr2 && nr % nr2 == 0 }?.let { nr2 -> nr / nr2 }}
  }
}
