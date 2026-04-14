package advent2017.day16

import utils.Input.readInput
import utils.Input.withInput
import utils.println

fun main() {
  println(first())
  println(second())
}

sealed class Move
data class Spin(val size: Int) : Move()
data class Exchange(val posA: Int, val posB: Int) : Move()
data class Partner(val nameA: Char, val nameB: Char) : Move()

fun String.dance(move: Move): String = when (move) {
  is Spin -> {
    val splitIndex = this.length - move.size
    this.substring(splitIndex) + this.substring(0, splitIndex)
  }

  is Exchange -> {
    val chars = this.toCharArray()
    val temp = chars[move.posA]
    chars[move.posA] = chars[move.posB]
    chars[move.posB] = temp
    String(chars)
  }

  is Partner -> this
    .replace(move.nameA, '#')
    .replace(move.nameB, move.nameA)
    .replace('#', move.nameB)
}

val danceMoves = readInput().first().split(",").map { moveStr ->
  when (moveStr[0]) {
    's' -> Spin(moveStr.substring(1).toInt())
    'x' -> {
      val (a, b) = moveStr.substring(1).split("/").map { it.toInt() }
      Exchange(a, b)
    }
    'p' -> {
      val (a, b) = moveStr.substring(1).split("/")
      Partner(a[0], b[0])
    }

    else -> throw IllegalArgumentException("Unknown move: $moveStr")
  }
}

val startingPosition = ('a'..'p').joinToString("")

fun String.danceAll(): String = danceMoves.fold(this) { acc, move -> acc.dance(move) }

fun first(): String = startingPosition.danceAll()

fun second(): String = run {
  val seen = mutableMapOf<String, Int>()
  val (cycleEnd, arrangement) = generateSequence(0 to startingPosition) { (i, pos) ->
    val newPos = pos.danceAll()
    i + 1 to newPos
  }
    .onEach { (i, pos) ->  if (pos !in seen) seen[pos] = i }
    .first { (i, pos) -> pos in seen && seen[pos]!! < i }
  val cycleSize = cycleEnd - seen[arrangement]!!
  val remaining = (1_000_000_000 - cycleEnd) % cycleSize
  generateSequence(arrangement) { pos -> pos.danceAll() }
    .drop(remaining)
    .first()
}
