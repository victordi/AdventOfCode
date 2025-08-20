package advent2016.day12

import arrow.core.Either
import utils.Input.withInput

fun main() {
  println(solve(mutableMapOf('a' to 0, 'b' to 0, 'c' to 0, 'd' to 0)))
  println(solve(mutableMapOf('a' to 0, 'b' to 0, 'c' to 1, 'd' to 0)))
}

sealed class Instruction
data class Copy(val from: Either<Char, Int>, val to: Char) : Instruction()
data class Inc(val register: Char) : Instruction()
data class Dec(val register: Char) : Instruction()
data class Jump(val condition: Either<Char, Int>, val offset: Int) : Instruction()

val instructions = withInput { input ->
  input.toList().map { line ->
    when {
      line.startsWith("cpy") -> {
        val parts = line.split(" ")
        Copy(
          from = Either.catch { parts[1].toInt() }.mapLeft { parts[1][0] },
          to = parts[2][0]
        )
      }
      line.startsWith("inc") -> Inc(line[4])
      line.startsWith("dec") -> Dec(line[4])
      line.startsWith("jnz") -> {
        val parts = line.split(" ")
        val condition = Either.catch { parts[1].toInt() }.mapLeft { parts[1][0] }
        Jump(condition, parts[2].toInt())
      }
      else -> throw IllegalArgumentException("Unknown instruction: $line")
    }
  }

}

fun solve(registers: MutableMap<Char, Int>): Int = run {
  var i = 0
  while (i < instructions.size) {
    instructions[i]
    when (val instruction = instructions[i]) {
      is Copy -> {
        val value = when (instruction.from) {
          is Either.Left -> registers[instruction.from.value]!!
          is Either.Right -> instruction.from.value
        }
        registers[instruction.to] = value
        i++
      }
      is Inc -> {
        registers[instruction.register] = (registers[instruction.register]!!) + 1
        i++
      }
      is Dec -> {
        registers[instruction.register] = (registers[instruction.register]!!) - 1
        i++
      }
      is Jump -> {
        val value = when (instruction.condition) {
          is Either.Left -> registers[instruction.condition.value]!!
          is Either.Right -> instruction.condition.value
        }
        if (value != 0) {
          i += instruction.offset
        } else {
          i++
        }
      }
    }
  }
  registers['a']!!
}
