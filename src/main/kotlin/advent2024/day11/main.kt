package advent2024.day11

import arrow.core.foldLeft
import utils.Input.withInput

fun main() {
  println(solve(25))
  println(solve(75))
}

var stones = withInput { it.toList() }.first().split(" ").map { it.toLong() }

fun blink(stone: Long): List<Long> =
  if (stone == 0L) {
    listOf(1L)
  } else if (stone.toString().length % 2 == 0) {
    val stoneString = stone.toString()
    listOf(
      stoneString.take(stoneString.length / 2).toLong(),
      stoneString.takeLast(stoneString.length / 2).toLong()
    )
  } else {
    listOf(stone * 2024L)
  }

fun Map<Long, Long>.blinkAll(): Map<Long, Long> = this.foldLeft(emptyMap()) { acc, (stone, count) ->
  blink(stone).fold(acc) { acc2, newStone ->
    acc2 + (newStone to (acc2[newStone] ?: 0) + count)
  }
}

fun solve(count: Int): Long = run {
  val initialStones = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
  (1..count).fold(initialStones) { acc, _ -> acc.blinkAll() }.values.sum()
}
