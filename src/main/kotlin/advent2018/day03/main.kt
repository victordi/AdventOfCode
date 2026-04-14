package advent2018.day03

import utils.Input.readInput
import utils.Point
import utils.extractInts

fun main() {
  println(first())
  println(second())
}

data class Claim(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int)

val claims = readInput().map { extractInts(it).let { (id, x, y, width, height) -> Claim(id, x, y, width, height) } }

val claimCounts = mutableMapOf<Point, Int>()

fun first(): Int = run {
  claims.forEach { claim ->
    for (i in claim.x until claim.x + claim.width) {
      for (j in claim.y until claim.y + claim.height) {
        val point = i to j
        claimCounts[point] = claimCounts.getOrDefault(point, 0) + 1
      }
    }
  }

  claimCounts.count { it.value >= 2 }
}

fun second(): Int = run {
  claims.forEach { claim ->
    var overlaps = false
    for (i in claim.x until claim.x + claim.width) {
      for (j in claim.y until claim.y + claim.height) {
        val point = i to j
        if (claimCounts[point]!! > 1) {
          overlaps = true
        }
      }
    }
    if (!overlaps) return claim.id
  }
  -1
}
