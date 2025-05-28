package advent2015.day20

import utils.Input
import utils.println

val lines = Input.readInput()

fun Int.divisors(limit: Int = 0): Set<Int> = (1..this).filter {
  if (limit > 0) {
    this % it == 0 && it * limit >= this
  } else this % it == 0
}.toSet()
fun Int.housePresents(limit: Int = 0, multiplier: Int = 10): Int = divisors(limit).sum() * multiplier

fun main() {
  println(second())
}

fun first(): Int = run {
  val target = 34000000
  var house = 1
  while (house.housePresents() < target) {
    house++
  }
  house
}

fun second(): Int = run {
  val target = 34000000
  var house = first()
  house.println
  while (house.housePresents(50, 11) < target) {
    house++
  }
  house
}