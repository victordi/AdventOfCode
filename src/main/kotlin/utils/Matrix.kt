package utils

import arrow.core.identity
import utils.Input.readInput
import kotlin.math.abs

inline fun <reified T> List<String>.parseArray(mapper: (Char) -> T): Array<Array<T>> =
  Array(size) { this[it].map(mapper).toTypedArray() }

fun readArrayFromInput(): Array<Array<Char>> = readInput().parseArray(::identity)

fun manhattanDistance(p1: Point, p2: Point) = abs(p1.first - p2.first) + abs(p1.second - p2.second)
operator fun Point.plus(other: Point): Point = Pair(this.first + other.first, this.second + other.second)
operator fun Point.minus(other: Point): Point = Pair(this.first - other.first, this.second - other.second)
fun <T> Array<Array<T>>.getOrElse(point: Point, default: T): T =
  try {
    this[point.first][point.second]
  } catch (e: ArrayIndexOutOfBoundsException) {
    default
  }

operator fun <T> Array<Array<T>>.get(point: Point): T = this[point.first][point.second]
operator fun <T> Array<Array<T>>.set(point: Point, value: T) {
  this[point.first][point.second] = value
}

fun <T> Array<Array<T>>.getAdjacent(point: Point): List<Pair<Int, Int>> = getAdjacent(point.first, point.second)
fun <T> Array<Array<T>>.getAdjacent(x: Int, y: Int): List<Pair<Int, Int>> {
  val result = mutableListOf<Pair<Int, Int>>()
  val n = this[0].size - 1
  val m = this.size - 1
  if (x < m) result.add(x + 1 to y)
  if (x > 0) result.add(x - 1 to y)
  if (y < n) result.add(x to y + 1)
  if (y > 0) result.add(x to y - 1)
  return result
}

fun <T> Array<Array<T>>.getAdjacentWithCorners(point: Point): List<Pair<Int, Int>> =
  getAdjacentWithCorners(point.first, point.second)

fun <T> Array<Array<T>>.getAdjacentWithCorners(x: Int, y: Int): List<Pair<Int, Int>> {
  val result = mutableListOf<Pair<Int, Int>>()
  for (x_fact in -1..1) {
    for (y_fact in -1..1) {
      if (x + x_fact in this.indices && y + y_fact in this[0].indices) {
        result.add(x + x_fact to y + y_fact)
      }
    }
  }
  result.remove(x to y)
  return result
}

fun <T> Array<Array<T>>.prettyPrint() {
  for (i in this.indices) {
    var line = ""
    for (j in this[0].indices) {
      line += this[i][j]
      line += " "
    }
    println(line.dropLast(1))
  }
}

fun <T> Array<Array<T>>.arrayForEach(f: (Pair<Int, Int>) -> Unit) {
  for (i in this.indices)
    for (j in this[0].indices)
      f(i to j)
}

infix fun <T> Array<Array<T>>.arrayEquals(other: Array<Array<T>>): Boolean {
  if (this.size != other.size) return false
  if (this[0].size != other[0].size) return false
  return this.foldIndexed(true) { i, acc, line ->
    acc && line.foldIndexed(true) { j, acc2, e -> acc2 && e == other[i][j] }
  }
}

fun <T> Array<Array<T>>.sumOf(transformer: (Pair<Int, Int>) -> Int): Int {
  var result = 0
  arrayForEach { result += transformer(it) }
  return result
}

fun <T> Array<Array<T>>.count(predicate: (Pair<Int, Int>) -> Boolean): Int = sumOf { if (predicate(it)) 1 else 0 }

fun <T> Array<Array<T>>.indexes(): List<Point> = mutableListOf<Point>().apply { arrayForEach { add(it) } }
fun <T> Array<Array<T>>.indexOf(value: T): Point = indexesOf(value).firstOrNull() ?: (-1 to -1)
fun <T> Array<Array<T>>.indexesOf(value: T): List<Point> = indexes().filter { this[it] == value }

inline fun <reified T> Array<Array<T>>.copy(): Array<Array<T>> = map { it.copyOf() }.toTypedArray()
