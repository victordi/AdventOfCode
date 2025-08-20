package advent2016.day08

import arrow.core.identity
import utils.*
import utils.Input.withInput

sealed class Instruction
data class Rect(val width: Int, val height: Int) : Instruction()
data class RotateRow(val row: Int, val shift: Int) : Instruction()
data class RotateColumn(val column: Int, val shift: Int) : Instruction()

fun main() {
  instructions.forEach { it.apply() }
  println(first())
  println(second())
}

val instructions = withInput { input ->
  input.toList().map { line ->
    when {
      line.startsWith("rect") -> {
        val (width, height) = line.removePrefix("rect ").split("x").map { it.toInt() }
        Rect(width, height)
      }
      line.startsWith("rotate row") -> {
        val (row, shift) = line.removePrefix("rotate row y=").split(" by ").map { it.toInt() }
        RotateRow(row, shift)
      }
      line.startsWith("rotate column") -> {
        val (column, shift) = line.removePrefix("rotate column x=").split(" by ").map { it.toInt() }
        RotateColumn(column, shift)
      }
      else -> throw IllegalArgumentException("Unknown instruction: $line")
    }
  }
}

val matrix = (0..5).map { ".".repeat(50) }.parseArray(::identity)

fun Instruction.apply(): Unit = when (this) {
  is Rect -> {
    for (y in 0 until height) {
      for (x in 0 until width) {
        matrix[y][x] = '#'
      }
    }
  }
  is RotateRow -> matrix.shiftRow(row, shift)
  is RotateColumn -> matrix.shiftColumn(column, shift)
}

fun first(): Int = matrix.count { matrix.get(it) == '#' }
fun second(): Unit = matrix.prettyPrint()
