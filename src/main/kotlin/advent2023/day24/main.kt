package advent2023.day24

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

data class Hailstone(val x: Long, val y: Long, val z: Long, val vx: Long, val vy: Long, val vz: Long)

val hailstones = readInput().map { line ->
  val (pos, speed) = line.filter { !it.isWhitespace() }.split('@')
  val (x, y, z) = pos.split(',').map { it.toLong() }
  val (vx, vy, vz) = speed.split(',').map { it.toLong() }
  Hailstone(x, y, z, vx, vy, vz)
}

fun getIntersection(h1: Hailstone, h2: Hailstone): Pair<Double, Double> = run {
  val a1 = h1.vy.toDouble()
  val b1 = -h1.vx.toDouble()
  val c1 = a1 * h1.x + b1 * h1.y

  val a2 = h2.vy.toDouble()
  val b2 = -h2.vx.toDouble()
  val c2 = a2 * h2.x + b2 * h2.y

  val determinant = a1 * b2 - a2 * b1
  if (determinant == 0.0) Double.POSITIVE_INFINITY to Double.POSITIVE_INFINITY // parallel lines
  else (b2 * c1 - b1 * c2) / determinant to (a1 * c2 - a2 * c1) / determinant
}

fun Hailstone.reaches(point: Pair<Double, Double>): Boolean =
  if (x < point.first && vx < 0) false
  else if (x > point.first && vx > 0) false
  else if (y < point.second && vy < 0) false
  else if (y > point.second && vy > 0) false
  else true

fun first(): Long = run {
  val range = 200000000000000.0..400000000000000.0
  var result = 0L
  for (i in hailstones.indices) {
    for (j in i + 1 until hailstones.size) {
      val h1 = hailstones[i]
      val h2 = hailstones[j]
      val (x, y) = getIntersection(h1, h2)
      if (x != Double.POSITIVE_INFINITY && x in range && y in range && h1.reaches(x to y) && h2.reaches(x to y)) {
        result++
      }
    }
  }
  result
}

fun second(): Long = run {
  val equations = hailstones.take(4).mapIndexed { idx, h ->
    val xEq = "x + vx * T$idx = ${h.x} + ${h.vx} * T$idx"
    val yEq = "y + vy * T$idx = ${h.y} + ${h.vy} * T$idx"
    val zEq = "z + vz * T$idx = ${h.z} + ${h.vz} * T$idx"
    listOf(xEq, yEq, zEq)
  }.flatten()
  equations.forEach { println(it) }
  // use equations and an online solver to find x, y, z
  val x = 454198524200037
  val y = 345331129547776
  val z = 234240489673806
  x + y + z
}
