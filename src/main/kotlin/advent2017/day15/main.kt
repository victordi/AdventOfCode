package advent2017.day15

import utils.Input.readInput
import utils.extractLongs

fun main() {
  println(first())
  println(second())
}

val input = readInput()
val genA = extractLongs(input.first()).first()
val genB = extractLongs(input.drop(1).first()).first()
const val factorA = 16807
const val factorB = 48271
const val rem = 2147483647

fun genANext(value: Long): Long = (value * factorA) % rem
fun genBNext(value: Long): Long = (value * factorB) % rem
fun match(a: Long, b: Long): Boolean = (a and 0xFFFF) == (b and 0xFFFF)

fun first(): Int = generateSequence(Pair(genA, genB)) { (a, b) ->
  Pair(genANext(a), genBNext(b))
}
  .take(40_000_000)
  .count { (a, b) -> match(a, b) }


fun second(): Int = generateSequence(Pair(genA, genB)) { (a, b) ->
  val nextA = generateSequence(genANext(a)) { genANext(it) }.first { it % 4 == 0L }
  val nextB = generateSequence(genBNext(b)) { genBNext(it) }.first { it % 8 == 0L }
  nextA to nextB
}
  .take(5_000_000)
  .count { (a, b) -> match(a, b) }
