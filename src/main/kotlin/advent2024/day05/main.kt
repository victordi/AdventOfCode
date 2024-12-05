package advent2024.day05

import utils.Input.withInput
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

val input = withInput { input ->
  val list = input.toList()
  list.takeWhile { it != "" }.map { it.splitTwo("|") { it.toInt() } } to
      list.dropWhile { it != "" }.drop(1).map { it.split(',').map { it.toInt() } }
}

fun List<Pair<Int, Int>>.getAfter(n: Int): List<Int> = run {
  val (rules, _) = input
  rules.filter { it.first == n }.map { it.second }
}

fun List<Int>.isValidUpdate(rules: List<Pair<Int, Int>>): Boolean = run {
  (size - 1 downTo 1).fold(true) { acc, i ->
    acc && !take(i).any { it in rules.getAfter(this[i]) }
  }
}

fun first(): Int = run {
  val (rules, updates) = input
  updates
    .filter { it.isValidUpdate(rules) }
    .sumOf { it[it.size / 2] }
}

fun second(): Int = run {
  val (rules, updates) = input
  updates
    .filter { !it.isValidUpdate(rules) }
    .map { update -> update.sortedBy { rules.getAfter(it).filter { after -> after in update }.size }}
    .sumOf { it[it.size / 2] }
}
