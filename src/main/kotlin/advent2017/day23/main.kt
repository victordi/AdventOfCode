package advent2017.day23

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

sealed class Instruction {
  data class Set(val x: String, val y: String) : Instruction()
  data class Sub(val x: String, val y: String) : Instruction()
  data class Mul(val x: String, val y: String) : Instruction()
  data class Jnz(val x: String, val y: String) : Instruction()
}

val instructions = readInput().map { line ->
  val parts = line.split(" ")
  when (parts[0]) {
    "set" -> Instruction.Set(parts[1], parts[2])
    "sub" -> Instruction.Sub(parts[1], parts[2])
    "mul" -> Instruction.Mul(parts[1], parts[2])
    "jnz" -> Instruction.Jnz(parts[1], parts[2])
    else -> throw IllegalArgumentException("Unknown instruction: ${parts[0]}")
  }
}

data class State(
  val ip: Int = 0,
  val registers: Map<String, Long> = emptyMap(),
  val lastSound: Long = 0L
)

fun getValue(x: String, registers: Map<String, Long>): Long =
  x.toLongOrNull() ?: registers.getOrDefault(x, 0L)

fun executeInstruction(state: State, instr: Instruction): State = when (instr) {
  is Instruction.Set -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to getValue(instr.y, state.registers))
  )
  is Instruction.Sub -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to (state.registers.getOrDefault(instr.x, 0L) - getValue(instr.y, state.registers)))
  )
  is Instruction.Mul -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to (state.registers.getOrDefault(instr.x, 0L) * getValue(instr.y, state.registers)))
  )
  is Instruction.Jnz -> {
    val xValue = getValue(instr.x, state.registers)
    if (xValue != 0L) {
      val yValue = getValue(instr.y, state.registers)
      state.copy(ip = state.ip + yValue.toInt())
    } else {
      state.copy(ip = state.ip + 1)
    }
  }
}

tailrec fun runProgram(state: State, mulCount: Int = 0): Int {
  if (state.ip < 0 || state.ip >= instructions.size) {
    return mulCount
  }
  val instr = instructions[state.ip]
  val newState = executeInstruction(state, instr)
  val newMulCount = if (instr is Instruction.Mul) mulCount + 1 else mulCount
  return runProgram(newState, newMulCount)
}

fun first(): Int = runProgram(State())

fun second(): Int {
  // h is incremented for each non prime number between start and end
  val start = 81 * 100 + 100000
  val end = start + 17000
  val step = 17

  fun isComposite(n: Int): Boolean {
    if (n < 2) return true
    if (n == 2) return false
    if (n % 2 == 0) return true

    var i = 3
    while (i * i <= n) {
      if (n % i == 0) return true
      i += 2
    }
    return false
  }

  return (start..end step step).count { isComposite(it) }
}
