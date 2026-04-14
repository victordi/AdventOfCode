package advent2017.day13

import utils.Input.readInput
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

val layers = readInput().associate { it.splitTwo(": ").let { (f, s) -> f.toInt() to s.toInt() } }

fun scannerPosition(depth: Int, time: Int): Int = run {
  val cycle = 2 * (depth - 1)
  val posInCycle = time % cycle
  if (posInCycle < depth) posInCycle else cycle - posInCycle
}

fun first(): Int = layers.entries.sumOf { (layer, depth) ->
  if (scannerPosition(depth, layer) == 0) layer * depth else 0
}

fun second(): Int = generateSequence(0) { it + 1 }.first { delay ->
  layers.none { (layer, depth) ->
    scannerPosition(depth, layer + delay) == 0
  }
}
