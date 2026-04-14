package advent2017.day09

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

val stream = readInput().first()

data class State(
  val score: Int,
  val depth: Int,
  val inGarbage: Boolean,
  val skipNext: Boolean
)

fun first(): Int = stream.fold(State(0, 0, false, skipNext = false)) { (s, d, g, skip), char ->
  if (skip) State(s, d, g, skipNext = false)
  else when (char) {
    '!' -> State(s, d, g, skipNext = true)
    '<' -> if (!g) State(s, d, true, skipNext = false) else State(s, d, g, skipNext = false)
    '>' -> if (g) State(s, d, false, skipNext = false) else State(s, d, g, skipNext = false)
    '{' -> if (!g) State(s, d + 1, g, skipNext = false) else State(s, d, g, skipNext = false)
    '}' -> if (!g) State(s + d, d - 1, g, skipNext = false) else State(s, d, g, skipNext = false)
    else -> State(s, d, g, skipNext = false)
  }
}.score

fun second(): Int = stream.fold(State(0, 0, false, skipNext = false)) { (s, d, g, skip), char ->
  if (skip) State(s, d, g, skipNext = false)
  else when (char) {
    '!' -> State(s, d, g, true)
    '<' -> if (!g) State(s, d, true, skipNext = false) else State(s + 1, d, g, false)
    '>' -> if (g) State(s, d, false, skipNext = false) else State(s, d, g, false)
    else -> State(if(g) s + 1 else s, d, g, false)
  }
}.score
