package advent2016.day13

import utils.Point

fun main() {
  println(first())
  println(second())
}

const val designerNumber = 1362
fun Point.isOpenSpace(): Boolean {
  val (x,y) = this
  if (x < 0 || y < 0) return false
  val value = x * x + 3 * x + 2 * x * y + y + y * y + designerNumber
  return value.countOneBits() % 2 == 0
}

fun minSteps(): Int = run {
  val start = Point(1, 1)
  val end = Point(31, 39)
  val visited = mutableSetOf<Point>()
  val queue = ArrayDeque<Pair<Point, Int>>()
  queue.add(start to 0)

  while (queue.isNotEmpty()) {
    val (current, steps) = queue.removeFirst()
    if (current == end) return steps
    if (current in visited || !current.isOpenSpace()) continue
    visited.add(current)
    val (x, y) = current

    listOf(
      x + 1 to y,
      x - 1 to y,
      x to y + 1,
      x to y - 1
    ).forEach { next ->
      queue.add(next to steps + 1)
    }
  }
  return -1
}

fun reachable(maxSteps: Int = 50): Int = run {
  val start = Point(1, 1)
  val visited = mutableSetOf<Point>()
  val queue = ArrayDeque<Pair<Point, Int>>()
  queue.add(start to 0)

  while (queue.isNotEmpty()) {
    val (current, steps) = queue.removeFirst()
    if (steps > maxSteps) continue
    if (current in visited) continue
    visited.add(current)
    val (x, y) = current

    listOf(
      x + 1 to y,
      x - 1 to y,
      x to y + 1,
      x to y - 1
    ).forEach { next ->
      if (next.isOpenSpace()) queue.add(next to steps + 1)
    }
  }
  return visited.size
}

fun first(): Int = minSteps()

fun second(): Int = reachable()
