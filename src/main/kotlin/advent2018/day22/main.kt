package advent2018.day22

import utils.Input.readInput
import utils.Point
import utils.extractInts
import utils.getAdjacent

fun main() {
  println(first())
  println(second())
}

val indexes = mutableMapOf<Point, Long>()
val input = readInput().let {
  val d = extractInts(it.first()).first()
  val (x, y) = extractInts(it.drop(1).first())
  Triple(d, x, y)
}

fun Point.erosionLevel(depth: Int): Long = (geoIndex(depth) + depth) % 20183

fun Point.geoIndex(depth: Int): Long = indexes.getOrElse(this) {
  val (x, y) = this
  val result = when {
    x == 0 -> y * 48271L
    y == 0 -> x * 16807L
    else -> (x - 1 to y).erosionLevel(depth) * (x to y - 1).erosionLevel(depth)
  }
  indexes.put(x to y, result)
  result
}

fun first(): Long = run {
  val (depth, xLimit, yLimit) = input

  indexes.put(0 to 0, 0L)
  indexes.put(xLimit to yLimit, 0L)

  var risk = 0L
  for (x in 0..xLimit) {
    for (y in 0..yLimit) {
      val erosion = (x to y).erosionLevel(depth)
      val r = when (erosion % 3) {
        0L -> 0L
        1L -> 1L
        else -> 2L
      }
      risk += r
    }
  }

  risk
}

enum class Tool { TORCH, CLIMBING_GEAR, NEITHER }

fun Point.regionType(depth: Int): Int = (erosionLevel(depth) % 3).toInt()

fun Point.allowedTools(depth: Int): Set<Tool> = when (regionType(depth)) {
  0 -> setOf(Tool.TORCH, Tool.CLIMBING_GEAR) // rocky
  1 -> setOf(Tool.CLIMBING_GEAR, Tool.NEITHER) // wet
  2 -> setOf(Tool.TORCH, Tool.NEITHER) // narrow
  else -> error("Invalid region type")
}

fun second(): Int = run {
  val (depth, targetX, targetY) = input
  val target = targetX to targetY

  data class State(val pos: Point, val tool: Tool, val time: Int)

  val pq = java.util.PriorityQueue<State>(compareBy { it.time })
  val visited = mutableMapOf<Pair<Point, Tool>, Int>()

  pq.add(State(0 to 0, Tool.TORCH, 0))

  while (pq.isNotEmpty()) {
    val (pos, tool, time) = pq.poll()

    if (pos == target && tool == Tool.TORCH) {
      return@run time
    }

    val key = pos to tool
    if (visited[key]?.let { it <= time } == true) continue
    visited[key] = time

    for (newTool in pos.allowedTools(depth)) {
      if (newTool != tool) {
        val newState = State(pos, newTool, time + 7)
        if (visited[pos to newTool]?.let { it <= newState.time } != true) {
          pq.add(newState)
        }
      }
    }

    for ((newX, newY) in pos.getAdjacent()) {
      if (newX >= 0 && newY >= 0) {
        val newPos = newX to newY
        if (tool in newPos.allowedTools(depth)) {
          val newState = State(newPos, tool, time + 1)
          if (visited[newPos to tool]?.let { it <= newState.time } != true) {
            pq.add(newState)
          }
        }
      }
    }
  }

  -1
}
