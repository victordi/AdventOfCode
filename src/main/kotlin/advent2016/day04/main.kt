package advent2016.day04

import utils.Input.withInput
import utils.println

fun main() {
  println(first())
  println(second())
}

val regex = Regex("""([a-z-]+)-(\d+)\[([a-z]+)\]""")

fun String.isRealRoom(): Pair<Boolean, Int> {
  val match = regex.matchEntire(this) ?: return false to -1
  val (name, sectorId, checksum) = match.destructured
  val letterCounts = name.replace("-", "").groupingBy { it }.eachCount()
  val sortedLetters = letterCounts.entries.sortedWith(compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key })
  val calculatedChecksum = sortedLetters.take(5).joinToString("") { it.key.toString() }
  return (calculatedChecksum == checksum) to sectorId.toInt()
}

fun String.decrypt(): Pair<String, Int> {
  val match = regex.matchEntire(this) ?: return this to -1
  val (name, sectorId, _) = match.destructured
  return name.map { char ->
    if (char == '-') ' ' else {
      val shifted = (char - 'a' + sectorId.toInt()) % 26 + 'a'.code
      shifted.toChar()
    }
  }.joinToString("") to sectorId.toInt()
}

fun first(): Int = withInput { input ->
  input.toList().sumOf {
    val (isReal, sectorId) = it.isRealRoom()
    if (isReal) sectorId else 0
  }
}

fun second(): Int = withInput { input ->
  input.toList().map { it.decrypt() }.filter { it.first.contains("north") && it.first.contains("pole") }
    .maxByOrNull { it.second }?.second ?: 0
}
