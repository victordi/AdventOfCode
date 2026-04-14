package advent2017.day24

import utils.Input.readInput
import utils.Input.withInput
import utils.extractInts

fun main() {
  solve(0, emptySet(), 0)
  println(first())
  println(second())
}

val ports = readInput().map { line -> extractInts(line).let { it[0] to it[1] } }

fun Int.getNext(used: Set<Pair<Int, Int>>): List<Pair<Int, Int>> = ports.filter { port ->
  (port.first == this || port.second == this) && !used.contains(port)
}

val strengths = mutableMapOf<Int, Int>()

fun solve(current: Int, used: Set<Pair<Int, Int>>, strength: Int) {
  val nextPorts = current.getNext(used)
  if (nextPorts.isEmpty()) {
    strengths[used.size] = maxOf(strengths.getOrDefault(used.size, 0), strength)
    return
  }
  for (port in nextPorts) {
    val nextCurrent = if (port.first == current) port.second else port.first
    val nextUsed = used + port
    val nextStrength = strength + port.first + port.second
    solve(nextCurrent, nextUsed, nextStrength)
  }
}

fun first(): Int = strengths.values.max()

fun second(): Int = strengths.maxByOrNull { it.key }!!.value
