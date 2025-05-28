package advent2015.day19

import utils.Input
import utils.splitTwo

val lines = Input.readInput()
val transformations = lines.dropLast(2).map { it.splitTwo(" => ") }
val molecule = lines.last()

fun transform(molecule: String): Set<String> {
  val result = mutableSetOf<String>()
  for ((from, to) in transformations) {
    var index = 0
    while (index < molecule.length) {
      val nextIndex = molecule.indexOf(from, index)
      if (nextIndex == -1) break
      result.add(molecule.substring(0, nextIndex) + to + molecule.substring(nextIndex + from.length))
      index = nextIndex + 1
    }
  }
  return result
}

fun main() {
  println(first())
  println(second())
}

fun first(): Int = transform(molecule).size

fun second(): Int = run {
  val elements = molecule.filter { it.isUpperCase() }.count()
  val endsCount = molecule.zipWithNext().map { "${it.first}${it.second}" }.count { it == "Rn" || it == "Ar" }
  val midCount = molecule.count { it == 'Y' }
  elements - endsCount - 2 * midCount - 1
}