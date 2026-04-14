package advent2017.day21

import utils.Input.readInput
import utils.println
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

val startPattern = listOf(
  ".#.",
  "..#",
  "###"
)

fun List<String>.splitPattern(): List<List<String>> {
  val size = if (this.size % 2 == 0) 2 else 3
  val patterns = mutableListOf<List<String>>()
  for (i in this.indices step size) {
    for (j in 0 until this[0].length step size) {
      val pattern = mutableListOf<String>()
      for (k in 0 until size) {
        pattern.add(this[i + k].substring(j, j + size))
      }
      patterns.add(pattern)
    }
  }
  return patterns
}

fun List<List<String>>.combinePatterns(): List<String> {
  val size = this[0].size
  if (this.size == 1) return first()
  val patternsPerRow = kotlin.math.sqrt(this.size.toDouble()).toInt()
  val combined = mutableListOf<String>()
  for (i in this.indices step patternsPerRow) {
    for (j in 0 until size) {
      val row = StringBuilder()
      for (k in 0 until patternsPerRow) {
        row.append(this[i + k][j])
      }
      combined.add(row.toString())
    }
  }
  return combined
}

fun List<String>.findMatchingRule(): List<String> {
  val permutations = permutations()
  if (size == 2) {
    for (perm in permutations) {
      val key = perm.joinToString("/")
      if (rulesTwo.containsKey(key)) {
        return rulesTwo[key]!!.split("/")
      }
    }
  } else if (size == 3) {
    for (perm in permutations) {
      val key = perm.joinToString("/")
      if (rulesThree.containsKey(key)) {
        return rulesThree[key]!!.split("/")
      }
    }
  }
  throw IllegalArgumentException("No matching rule found for pattern: ${this.joinToString("/")}")
}


fun List<String>.permutations(): List<List<String>> {
  val result = mutableListOf<List<String>>()

  fun rotate90(pattern: List<String>): List<String> {
    val n = pattern.size
    return List(n) { i ->
      List(n) { j ->
        pattern[n - 1 - j][i]
      }.joinToString("")
    }
  }

  fun flipHorizontal(pattern: List<String>): List<String> {
    return pattern.map { it.reversed() }
  }

  var current = this

  repeat(4) {
    result.add(current)
    current = rotate90(current)
  }

  current = flipHorizontal(this)
  repeat(4) {
    result.add(current)
    current = rotate90(current)
  }

  return result
}

val rulesTwo = readInput().take(6).associate { it.splitTwo(" => ") }
val rulesThree = readInput().drop(6).associate { it.splitTwo(" => ") }

fun List<String>.step(): List<String> {
  val patterns = this.splitPattern()
  val enhancedPatterns = patterns.map { it.findMatchingRule() }
  return enhancedPatterns.combinePatterns()
}

fun first(): Int = run {
  val finalPattern = generateSequence(startPattern) { it.step() }.take(6).last()
  finalPattern.sumOf { line -> line.count { it == '#' }}
}

fun second(): Int = run {
  val finalPattern = generateSequence(startPattern) { it.step() }.take(19).last()
  finalPattern.sumOf { line -> line.count { it == '#' }}
}
