package advent2018.day10

import utils.Input.readInput
import utils.extractInts
import utils.prettyPrint

fun main() {
  println(solve())
}

data class Point(val x: Int, val y: Int, val vx: Int, val vy: Int) {
  fun move() = Point(x + vx, y + vy, vx, vy)
}

val initialPoints = readInput().map { line ->
  val nums = extractInts(line)
  Point(nums[0], nums[1], nums[2], nums[3])
}

data class BoundingBox(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
  val area: Long get() = (maxX - minX).toLong() * (maxY - minY).toLong()
}

fun List<Point>.getBoundingBox() = BoundingBox(
  minOf { it.x },
  maxOf { it.x },
  minOf { it.y },
  maxOf { it.y }
)

fun List<Point>.getBoundingBoxArea(): Long = getBoundingBox().area

fun List<Point>.display() {
  val box = getBoundingBox()
  val pointSet = map { (it.x to it.y) }.toSet()

  val grid = (box.minY..box.maxY).map { y ->
    (box.minX..box.maxX).map { x ->
      if ((x to y) in pointSet) '#' else '.'
    }.toTypedArray()
  }.toTypedArray()

  grid.prettyPrint()
}

fun solve(): Int {
  data class State(val points: List<Point>, val seconds: Int)

  val states = generateSequence(State(initialPoints, 0)) { state ->
    State(state.points.map { it.move() }, state.seconds + 1)
  }

  val (messageState, seconds) = states
    .zipWithNext()
    .first { (current, next) ->
      next.points.getBoundingBoxArea() > current.points.getBoundingBoxArea()
    }
    .first

  messageState.display()
  return seconds
}
