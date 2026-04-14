package advent2017.day18

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

sealed class Instruction {
  data class Snd(val x: String) : Instruction()
  data class Set(val x: String, val y: String) : Instruction()
  data class Add(val x: String, val y: String) : Instruction()
  data class Mul(val x: String, val y: String) : Instruction()
  data class Mod(val x: String, val y: String) : Instruction()
  data class Rcv(val x: String) : Instruction()
  data class Jgz(val x: String, val y: String) : Instruction()
}

val instructions = readInput().map { line ->
  val parts = line.split(" ")
  when (parts[0]) {
    "snd" -> Instruction.Snd(parts[1])
    "set" -> Instruction.Set(parts[1], parts[2])
    "add" -> Instruction.Add(parts[1], parts[2])
    "mul" -> Instruction.Mul(parts[1], parts[2])
    "mod" -> Instruction.Mod(parts[1], parts[2])
    "rcv" -> Instruction.Rcv(parts[1])
    "jgz" -> Instruction.Jgz(parts[1], parts[2])
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
  is Instruction.Snd -> state.copy(
    ip = state.ip + 1,
    lastSound = getValue(instr.x, state.registers)
  )
  is Instruction.Set -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to getValue(instr.y, state.registers))
  )
  is Instruction.Add -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to (state.registers.getOrDefault(instr.x, 0L) + getValue(instr.y, state.registers)))
  )
  is Instruction.Mul -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to (state.registers.getOrDefault(instr.x, 0L) * getValue(instr.y, state.registers)))
  )
  is Instruction.Mod -> state.copy(
    ip = state.ip + 1,
    registers = state.registers + (instr.x to (state.registers.getOrDefault(instr.x, 0L) % getValue(instr.y, state.registers)))
  )
  is Instruction.Rcv -> state.copy(ip = state.ip + 1) // For part 1, we'll handle termination separately
  is Instruction.Jgz -> {
    val xValue = getValue(instr.x, state.registers)
    if (xValue > 0) {
      val yValue = getValue(instr.y, state.registers)
      state.copy(ip = state.ip + yValue.toInt())
    } else {
      state.copy(ip = state.ip + 1)
    }
  }
}

tailrec fun runUntilRcv(state: State): Long {
  if (state.ip !in instructions.indices) return -1L

  val instr = instructions[state.ip]
  return if (instr is Instruction.Rcv && getValue(instr.x, state.registers) != 0L) {
    state.lastSound
  } else {
    runUntilRcv(executeInstruction(state, instr))
  }
}

fun first(): Long = runUntilRcv(State())

data class ProgramState(
  val ip: Int = 0,
  val registers: Map<String, Long> = emptyMap(),
  val inbox: List<Long> = emptyList(),
  val sendCount: Int = 0,
  val waiting: Boolean = false
)

data class DualState(
  val program0: ProgramState,
  val program1: ProgramState,
  val current: Int = 0
) {
  fun currentProgram() = if (current == 0) program0 else program1
  fun otherProgram() = if (current == 0) program1 else program0
  fun withCurrent(p: ProgramState) = if (current == 0) copy(program0 = p) else copy(program1 = p)
  fun withOther(p: ProgramState) = if (current == 0) copy(program1 = p) else copy(program0 = p)
  fun switch() = copy(current = 1 - current)
}

fun executeInstructionDual(state: DualState, instr: Instruction): DualState = run {
  val current = state.currentProgram()
  val other = state.otherProgram()

  when (instr) {
    is Instruction.Snd -> {
      val value = getValue(instr.x, current.registers)
      state.withCurrent(current.copy(ip = current.ip + 1, sendCount = current.sendCount + 1, waiting = false))
        .withOther(other.copy(inbox = other.inbox + value, waiting = false))
    }
    is Instruction.Set -> {
      val value = getValue(instr.y, current.registers)
      state.withCurrent(current.copy(
        ip = current.ip + 1,
        registers = current.registers + (instr.x to value),
        waiting = false
      ))
    }
    is Instruction.Add -> {
      val value = getValue(instr.y, current.registers)
      state.withCurrent(current.copy(
        ip = current.ip + 1,
        registers = current.registers + (instr.x to (current.registers.getOrDefault(instr.x, 0L) + value)),
        waiting = false
      ))
    }
    is Instruction.Mul -> {
      val value = getValue(instr.y, current.registers)
      state.withCurrent(current.copy(
        ip = current.ip + 1,
        registers = current.registers + (instr.x to (current.registers.getOrDefault(instr.x, 0L) * value)),
        waiting = false
      ))
    }
    is Instruction.Mod -> {
      val value = getValue(instr.y, current.registers)
      state.withCurrent(current.copy(
        ip = current.ip + 1,
        registers = current.registers + (instr.x to (current.registers.getOrDefault(instr.x, 0L) % value)),
        waiting = false
      ))
    }
    is Instruction.Rcv -> {
      if (current.inbox.isNotEmpty()) {
        val value = current.inbox.first()
        state.withCurrent(current.copy(
          ip = current.ip + 1,
          registers = current.registers + (instr.x to value),
          inbox = current.inbox.drop(1),
          waiting = false
        ))
      } else {
        state.withCurrent(current.copy(waiting = true)).switch()
      }
    }
    is Instruction.Jgz -> {
      val xValue = getValue(instr.x, current.registers)
      val newIp = if (xValue > 0) {
        current.ip + getValue(instr.y, current.registers).toInt()
      } else {
        current.ip + 1
      }
      state.withCurrent(current.copy(ip = newIp, waiting = false))
    }
  }
}

tailrec fun runDualPrograms(state: DualState): DualState {
  if ((state.program0.waiting && state.program1.waiting) ||
      (state.program0.ip !in instructions.indices && state.program1.ip !in instructions.indices)) {
    return state
  }

  val current = state.currentProgram()

  if (current.ip !in instructions.indices) {
    return runDualPrograms(state.switch())
  }

  if (current.waiting && current.inbox.isEmpty()) {
    return runDualPrograms(state.switch())
  }

  val instr = instructions[current.ip]
  return runDualPrograms(executeInstructionDual(state, instr))
}

fun second(): Int = run {
  val initialState = DualState(
    program0 = ProgramState(registers = mapOf("p" to 0L)),
    program1 = ProgramState(registers = mapOf("p" to 1L))
  )
  val finalState = runDualPrograms(initialState)
  finalState.program1.sendCount
}
