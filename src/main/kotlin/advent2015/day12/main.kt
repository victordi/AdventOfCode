package advent2015.day12

import utils.Input

val lines = Input.readInput()

fun main() {
  println(first())
  println(second())
}

fun String.sumOfNumbers(): Int = Regex("-?\\d+").findAll(this).sumOf { it.value.toInt() }
fun String.sumOfRedObjects(): Int = Regex("\\{.*?red.*?}").findAll(this).sumOf { it.value.sumOfNumbers() }

fun first(): Int = lines.first().sumOfNumbers()

fun second(): Int = lines.first().sumOfNumbers() - lines.first().sumOfRedObjects()
