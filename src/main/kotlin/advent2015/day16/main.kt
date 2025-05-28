package advent2015.day16

import utils.Input

val lines = Input.readInput()
val matches = mapOf(
  "children" to 3, "cats" to 7, "samoyeds" to 2, "pomeranians" to 3, "akitas" to 0, "vizslas" to 0,
  "goldfish" to 5, "trees" to 3, "cars" to 2, "perfumes" to 1
)

val matches2 = mapOf(
  "children" to { x: Int -> x == 3 },
  "cats" to { x: Int -> x > 7 },
  "samoyeds" to { x: Int -> x == 2 },
  "pomeranians" to { x: Int -> x < 3 },
  "akitas" to { x: Int -> x == 0 },
  "vizslas" to { x: Int -> x == 0 },
  "goldfish" to { x: Int -> x < 5 },
  "trees" to { x: Int -> x > 3 },
  "cars" to { x: Int -> x == 2 },
  "perfumes" to { x: Int -> x == 1 }
)

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  var aunt = -1
  lines.forEach { line ->
    val regex = Regex("Sue (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)")
    val (nr, p1, d1, p2, d2, p3, d3) = regex.find(line)!!.destructured
    if (matches[p1] == d1.toInt() && matches[p2] == d2.toInt() && matches[p3] == d3.toInt()) {
      aunt = nr.toInt()
    }
  }
  aunt
}

fun second(): Int = run {
  var aunt = -1
  lines.forEach { line ->
    val regex = Regex("Sue (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)")
    val (nr, p1, d1, p2, d2, p3, d3) = regex.find(line)!!.destructured
    if (matches2[p1]!!(d1.toInt()) && matches2[p2]!!(d2.toInt()) && matches2[p3]!!(d3.toInt())) {
      aunt = nr.toInt()
    }
  }
  aunt
}