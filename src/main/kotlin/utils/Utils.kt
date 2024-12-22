package utils

import arrow.core.Tuple4

val Any?.println: Unit
  get() = println(this)

typealias Point = Pair<Int, Int>

fun Point.getAdjacent(): List<Point> = let { (x, y) ->
  listOf(x - 1 to y, x to y + 1, x + 1 to y, x to y - 1)
}

fun Int.modSkipZero(n: Int): Int = if (this % n == 0) n else this % n

fun String.splitTwo(delimiter: String): Pair<String, String> =
  splitTwo(delimiter) { it }

fun <T> String.splitTwo(delimiter: String, f: (String) -> T): Pair<T, T> =
  split(delimiter).let { f(it[0]) to f(it[1]) }

fun String.binaryToDecimal(): Long = this.reversed().fold(1L to 0L) { (pow2, result), c ->
  pow2 * 2 to result + if (c == '1') pow2 else 0
}.second

fun <T> Iterable<T>.zip3(): List<Triple<T, T, T>> {
  val iterator = iterator()
  if (!iterator.hasNext()) return emptyList()
  var current = iterator.next()
  if (!iterator.hasNext()) return emptyList()
  val result = mutableListOf<Triple<T, T, T>>()
  var next = iterator.next()
  while (iterator.hasNext()) {
    val aux = iterator.next()
    result.add(Triple(current, next, aux))
    current = next
    next = aux
  }
  return result
}

fun <T> Iterable<T>.zip4(): List<Tuple4<T, T, T, T>> {
  val iterator = iterator()
  if (!iterator.hasNext()) return emptyList()
  var first = iterator.next()
  if (!iterator.hasNext()) return emptyList()
  var second = iterator.next()
  if (!iterator.hasNext()) return emptyList()
  var third = iterator.next()
  if (!iterator.hasNext()) return emptyList()
  val result = mutableListOf<Tuple4<T, T, T, T>>()
  var fourth = iterator.next()
  while (iterator.hasNext()) {
    val aux = iterator.next()
    result.add(Tuple4(first, second, third, fourth))
    first = second
    second = third
    third = fourth
    fourth = aux
  }
  result.add(Tuple4(first, second, third, fourth))
  return result
}

fun <T> List<T>.splitList(delimiter: (T) -> Boolean): List<List<T>> {
  val result = mutableListOf<List<T>>()
  val current = mutableListOf<T>()

  this.forEach {
    if (delimiter(it)) {
      result.add(current.toList())
      current.clear()
    } else current.add(it)
  }
  result.add(current.toList())

  return result
}

fun <T> List<T>.every(n: Int): List<List<T>> {
  val result = mutableListOf<List<T>>()

  for (i in this.indices step n) {
    val current = mutableListOf<T>()
    for (j in 0 until n) {
      current += this[i + j]
    }
    result.add(current.toList())
  }

  return result
}

fun lcm(a: Int, b: Int): Int = lcm(a.toLong(), b.toLong()).toInt()

fun lcm(a: Long, b: Long): Long {
  val larger = if (a > b) a else b
  val maxLcm = a * b
  var lcm = larger
  while (lcm <= maxLcm) {
    if (lcm % a == 0L && lcm % b == 0L) {
      return lcm
    }
    lcm += larger
  }
  return maxLcm
}

fun Collection<Point>.shoelaceArea(): Int = map { (x, y) -> x.toLong() to y.toLong() }.shoelaceArea().toInt()

fun Collection<Pair<Long, Long>>.shoelaceArea(): Long = run {
  require(size > 2) { "polygon should contain at least 3 points" }
  val points = listOf(first()) + this.reversed()
  points.zipWithNext().fold(0L) { acc, (p1, p2) -> acc + p1.first * p2.second - p2.first * p1.second } / 2
}

fun extractInts(input: String): List<Int> {
  return Regex("""[+-]?\d+""").findAll(input).map { it.value.toInt() }.toList()
}

fun extractLongs(input: String): List<Long> {
  return Regex("""[+-]?\d+""").findAll(input).map { it.value.toLong() }.toList()
}

fun Int.pow(exp: Int): Int {
  var result = 1
  repeat(exp) {
    result *= this
  }
  return result
}

fun Long.pow(exp: Int): Long {
  var result = 1L
  repeat(exp) {
    result *= this
  }
  return result
}
