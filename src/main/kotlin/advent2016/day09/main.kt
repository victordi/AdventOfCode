package advent2016.day09

import utils.Input.withInput
import utils.splitTwo

fun main() {
  println(first())
  println(second())
}

fun String.decompress(): String = run {
  val result = StringBuilder()
  var i = 0
  while (i < this.length) {
    if (this[i] == '(') {
      val end = this.indexOf(')', i)
      val marker = this.substring(i + 1, end).split('x')
      val length = marker[0].toInt()
      val repeat = marker[1].toInt()
      val start = end + 1
      result.append(this.substring(start, start + length).repeat(repeat))
      i = start + length
    } else {
      result.append(this[i])
      i++
    }
  }
  result.toString()
}

fun first(): Int = withInput { input ->
  input.first().decompress().length
}

fun Map<Int, Int>.move(steps: Int): Map<Int,Int> = this
  .map { (left, mult) -> left - steps to mult}
  .filter { (left, _) -> left > 0 }
  .toMap()

fun second(): Long = withInput { input ->
  val str = input.first()
  var res = 0L
  var i = 0
  var mults = mapOf<Int, Int>()
  while (i < str.length) {
    if (str[i] == '(') {
      val end = str.indexOf(')', i)
      val (length, repeat) = str.substring(i + 1, end).splitTwo("x") { it.toInt() }
      mults = mults.move(end - i + 1).let { it + (length to repeat * (it[length] ?: 1))}
      i = end + 1
    } else {
      res += mults.values.fold(1) { acc, mult -> acc * mult }
      mults = mults.move(1)
      i++
    }
  }

  res
}
