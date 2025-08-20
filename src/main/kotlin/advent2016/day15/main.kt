package advent2016.day15

import utils.Input.withInput

fun main() {
  println(solve(discs))
  println(solve(discs + Disc(discs.size + 1, 11, 0, 0)))
}

data class Disc(
  val id: Int,
  val positions: Int,
  val time: Int,
  val startPosition: Int
) {
  fun isAligned(time: Int): Boolean = (startPosition + time + id) % positions == 0
}

val discs = withInput { input ->
  input.toList().map { line ->
    val regex = """Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()
    val matchResult = regex.matchEntire(line)
      ?: throw IllegalArgumentException("Invalid line format: $line")
    val (id, positions, startPosition) = matchResult.destructured
    Disc(id.toInt(), positions.toInt(), 0, startPosition.toInt())
  }
}

fun solve(discs: List<Disc>): Int =
  generateSequence(0) { it + 1 }
    .find { time -> discs.all { it.isAligned(time) } }
    ?: -1
