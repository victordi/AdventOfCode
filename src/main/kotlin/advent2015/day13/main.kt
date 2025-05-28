package advent2015.day13

import utils.Input
import utils.permutations

val lines = Input.readInput()

fun Iterable<String>.happiness(happinessMap: Map<Pair<String, String>, Int>): Int = this
  .zipWithNext()
  .sumOf { happinessMap.getValue(it.first to it.second) + happinessMap.getValue(it.second to it.first) }
  .let { it + happinessMap.getValue(this.first() to this.last()) + happinessMap.getValue(this.last() to this.first()) }

val happinessMap = lines.associate {
  val regex = Regex("(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+).")
  val (a, gain, happiness, b) = regex.find(it)!!.destructured
  Pair(a, b) to (if (gain == "gain") happiness.toInt() else -happiness.toInt())
}

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  val seatingPermutations = happinessMap.keys.map { it.first }.distinct().permutations()
  seatingPermutations.maxOf { it.happiness(happinessMap) }
}

fun second(): Int = run {
  val guests = happinessMap.keys.map { it.first }.distinct()
  val finalMap = happinessMap +
      guests.map { Pair("Me" to it, 0) } +
      guests.map { Pair(it to "Me", 0) }

  val seatingPermutations = finalMap.keys.map { it.first }.distinct().permutations()
  seatingPermutations.maxOf { it.happiness(finalMap) }
}