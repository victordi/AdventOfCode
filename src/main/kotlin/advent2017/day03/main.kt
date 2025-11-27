package advent2017.day03

fun main() {
  println(first())
  println(second())
}

const val nr = 265149

fun minDist(): Int = run {
  // 1 + 8 * 1 + 8 * 2 + 8 * 3 + ... + 8 * n > 265149
  // 1 + 8 * N * (n-1) / 2 > 265149
  // 8 * N * (n-1) / 2 > 265148
  // 4 * N * (n-1) > 265148
  // n^2 - n > 66287 -> n = 258 -> 66306
  val ring = 257
  val firstInRing = 8 * ring * (ring - 1) / 2 + 1
  val diff = (nr - firstInRing) % (ring * 2)
  diff
}

fun first(): Int = minDist()

fun second(): Int = run {
  0
}
