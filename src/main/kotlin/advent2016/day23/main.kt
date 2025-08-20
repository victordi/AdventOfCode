package advent2016.day23

import arrow.core.Either
import utils.Input.withInput

fun main() {
  println(solve(instructions.toMutableList(), mutableMapOf('a' to 7, 'b' to 0, 'c' to 0, 'd' to 0)))
  println(solve(instructions.toMutableList(), mutableMapOf('a' to 12, 'b' to 0, 'c' to 0, 'd' to 0)))
}

sealed class Instruction
data class Copy(val from: Either<Char, Int>, val to: Either<Char, Int>) : Instruction()
data class Jump(val condition: Either<Char, Int>, val offset: Either<Char, Int>) : Instruction()
data class Inc(val register: Char) : Instruction()
data class Dec(val register: Char) : Instruction()
data class Tgl(val register: Char) : Instruction()

val instructions = withInput { input ->
  input.toList().map { line ->
    when {
      line.startsWith("cpy") -> {
        val parts = line.split(" ")
        Copy(
          from = Either.catch { parts[1].toInt() }.mapLeft { parts[1][0] },
          to = Either.catch { parts[2].toInt() }.mapLeft { parts[2][0] })
      }

      line.startsWith("inc") -> Inc(line[4])
      line.startsWith("dec") -> Dec(line[4])
      line.startsWith("jnz") -> {
        val parts = line.split(" ")
        Jump(
          condition = Either.catch { parts[1].toInt() }.mapLeft { parts[1][0] },
          offset = Either.catch { parts[2].toInt() }.mapLeft { parts[2][0] })
      }

      line.startsWith("tgl") -> Tgl(line[4])
      else -> throw IllegalArgumentException("Unknown instruction: $line")
    }
  }
}

fun solve(instructions: MutableList<Instruction>, registers: MutableMap<Char, Int>): Int = run {
  var i = 0
  while (i < instructions.size) {
    when (val instruction = instructions[i]) {
      is Copy -> {
        val value = when (instruction.from) {
          is Either.Left -> registers[instruction.from.value]!!
          is Either.Right -> instruction.from.value
        }
        when (instruction.to) {
          is Either.Left -> registers[instruction.to.value] = value
          is Either.Right -> throw IllegalArgumentException("Can't copy to a literal")
        }
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
        val offset = when (instruction.offset) {
          is Either.Left -> registers[instruction.offset.value]!!
          is Either.Right -> instruction.offset.value
        }
        if (value != 0) {
          i += offset
        } else {
          i++
        }
      }

      is Tgl -> {
        val target = i + registers[instruction.register]!!
        if (target < 0 || target >= instructions.size) {
          i++
          continue
        }
        when (val targetInstruction = instructions[target]) {
          is Copy -> instructions[target] = Jump(targetInstruction.from, targetInstruction.to)
          is Inc -> instructions[target] = Dec(targetInstruction.register)
          is Dec -> instructions[target] = Inc(targetInstruction.register)
          is Jump -> instructions[target] = Copy(targetInstruction.condition, targetInstruction.offset)
          is Tgl -> instructions[target] = Inc(targetInstruction.register)
        }
        i++
      }
    }
  }
  registers['a']!!
}
