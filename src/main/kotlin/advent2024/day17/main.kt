package advent2024.day17

import arrow.core.Tuple4
import utils.Input.withInput
import utils.extractInts
import utils.extractLongs
import utils.pow

fun main() {
  first()
  println(second())
}

fun List<Int>.runProgram(a: Long, b: Long, c: Long): List<Int> = run {
  var A = a
  var B = b
  var C = c
  var instrPointer = 0
  var output = ""

  while (instrPointer < this.size) {
    val instr = this[instrPointer]
    val literalOperand = this[instrPointer + 1].toLong()
    val comboOperand = when (this[instrPointer + 1]) {
      0, 1, 2, 3 -> this[instrPointer + 1]
      4 -> A
      5 -> B
      6 -> C
      else -> throw IllegalArgumentException("Invalid combo operand")
    }.toLong()
    instrPointer += 2
    when (instr) {
      0 -> A /= 2.pow(comboOperand.toInt())
      1 -> B = B.xor(literalOperand)
      2 -> B = comboOperand % 8
      3 -> if (A != 0L) instrPointer = literalOperand.toInt()
      4 -> B = B.xor(C)
      5 -> output += (comboOperand % 8).toString() + ","
      6 -> B = A / 2.pow(comboOperand.toInt())
      7 -> C = A / 2.pow(comboOperand.toInt())
      else -> throw IllegalArgumentException()
    }
  }

  output.dropLast(1).split(",").map { it.toInt() }
}

val input = withInput { input ->
  val list = input.toList()
  Tuple4(
    extractLongs(list[0]).first(), extractLongs(list[1]).first(), extractLongs(list[2]).first(), extractInts(list[4])
  )
}

fun first(): Unit = run {
  val (a, b, c, prog) = input
  val output = prog.runProgram(a, b, c)
  println(output.joinToString(","))
}

fun second(): Long = run {
  // B = (A % 8 xor 5) xor (A / (2 ^ B)) xor 6
  // A = A / 8
  // B % 8 needs to be 2,4,1,5,7,5,4,3,1,6,0,3,5,5,3,0
  // reverse -> each step takes just last 3 bits and then discards anyway -> build A from the back

  val (_, b, c, prog) = input
  var A = 0L
  var matchedNumbers = 0
  while (matchedNumbers < prog.size) {
    A *= 8
    while(prog.runProgram(A, b, c) != prog.takeLast(matchedNumbers + 1)) {
      A++
    }
    matchedNumbers += 1
  }
  A
}
