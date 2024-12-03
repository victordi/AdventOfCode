package advent2024.day03

import utils.Input.withInput
import arrow.core.foldLeft

fun main() {
  println(first())
  println(second())
}

data class Instruction(val action: Int?, val a: Int, val b: Int)

val regex = Regex("""mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""")
val instructions = regex.findAll(withInput { line -> line.toList().joinToString { it } })
  .map {
    val action = it.groupValues[0]
    when (action) {
      "do()" -> Instruction(1, 0, 0)
      "don't()" -> Instruction(0, 0, 0)
      else -> Instruction(null, it.groupValues[1].toInt(), it.groupValues[2].toInt())
    }
  }

fun first(): Int = run {
  instructions.sumOf { (_, a, b) -> a * b }
}


fun second(): Int = run {
  instructions
    .fold(1 to 0) { (enabled, sum), instruction ->
      when (instruction.action) {
        null -> enabled to sum + enabled * instruction.a * instruction.b
        else -> instruction.action to sum
      }
    }
    .second

}
