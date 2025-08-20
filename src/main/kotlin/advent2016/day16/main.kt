package advent2016.day16

import utils.Input.withInput

fun main() {
  println(solve(272))
  println(solve(35651584))
}

private val initial = withInput { it.first() }

fun String.expand(): String = run {
  val reverse = this.reversed().map { if (it == '0') '1' else '0' }.joinToString("")
  "${this}0$reverse"
}

fun String.checksum(): String = chunked(2)
  .map { bits -> if (bits[0] == bits[1]) '1' else '0' }.joinToString("")
  .let { if (it.length % 2 == 0) it.checksum() else it }

fun solve(diskSize: Int): String =
  generateSequence(initial) { it.expand() }
    .filter { it.length >= diskSize }
    .take(1)
    .first()
    .take(diskSize)
    .checksum()
