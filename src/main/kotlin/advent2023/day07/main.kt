package advent2023.day07

import utils.AoCPart
import utils.Input.withInput
import utils.Part1
import utils.Part2

fun main() {
  println(solve(Part1))
  println(solve(Part2))
}

fun solve(part: AoCPart): Int = withInput { input ->
  val list = input.toList()
  val games = list.map { it.split(" ")[0] }.toMutableList()
  games.sortWith(getComparator(part))

  list.sumOf {
    val (hand, score) = it.split(" ")
    score.toInt() * (games.indexOf(hand) + 1)
  }
}

val getComparator = { part: AoCPart ->
  Comparator<String> { o1, o2 ->
    val (pairs1, pairs2) = listOf(o1, o2).map { it.getPairs(part).take(2) }
    pairs1.zip(pairs2).let {
      val (c1, c2) = it[0]
      if (c1 == c2) {
        if (c1 == 5) order(o1, o2, part)
        else {
          val (cc1, cc2) = it[1]
          if (cc1 == cc2) order(o1, o2, part)
          else cc1 - cc2
        }
      } else c1 - c2
    }
  }
}

fun String.getPairs(part: AoCPart): List<Int> {
  val sorted = toCharArray().apply { sort() }
  var nrJokers = if (part == Part1) 0 else sorted.count { it == 'J' }
  return if (nrJokers == 5) listOf(5)
  else sorted
    .filter { part == Part1 || it != 'J' }
    .map { c -> sorted.count { it == c } to c }
    .toSet()
    .sortedByDescending { it.first }
    .map { (c, _) ->
      c + nrJokers
        .also { nrJokers = kotlin.math.min(0, nrJokers - (5 - c)) }
    }
}

fun order(s1: String, s2: String, part: AoCPart): Int = s1.zip(s2)
  .firstOrNull { (c1, c2) -> c1 != c2 }
  ?.let { (c1, c2) -> getCardOrder(part).indexOf(c1) - getCardOrder(part).indexOf(c2) }
  ?: 0

val getCardOrder = { part: AoCPart ->
  when (part) {
    Part1 -> listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
    Part2 -> listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
  }
}