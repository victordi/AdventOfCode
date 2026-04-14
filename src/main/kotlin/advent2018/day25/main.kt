package advent2018.day25

import arrow.core.Tuple4
import utils.Input.readInput
import utils.extractInts
import kotlin.math.abs

fun main() {
  println(solve())
}

fun distance(a: Tuple4<Int, Int, Int, Int>, b: Tuple4<Int, Int, Int, Int>): Int =
  listOf(
    abs(a.first - b.first),
    abs(a.second - b.second),
    abs(a.third - b.third),
    abs(a.fourth - b.fourth)
  ).sum()


fun solve(): Int = run {
  val points = readInput()
    .map {
      extractInts(it)
        .let { Tuple4(it[0], it[1], it[2], it[3]) }
    }
  val constellations = mutableListOf<MutableSet<Tuple4<Int, Int, Int, Int>>>()
  for (point in points) {
    val closeConstellations = constellations.filter { constellation ->
      constellation.any { distance(it, point) <= 3 }
    }
    if (closeConstellations.isEmpty()) {
      constellations.add(mutableSetOf(point))
    } else {
      val newConstellation = mutableSetOf(point)
      closeConstellations.forEach { newConstellation.addAll(it) }
      constellations.removeAll(closeConstellations)
      constellations.add(newConstellation)
    }
  }
  constellations.size
}
