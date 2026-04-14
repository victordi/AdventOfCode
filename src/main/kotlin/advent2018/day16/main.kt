package advent2018.day16

import utils.Input.readInput
import utils.extractInts

fun main() {
  println(first())
  println(second())
}

data class Sample(
  val before: State,
  val instruction: Instruction,
  val after: State
)

data class State(val r0: Int, val r1: Int, val r2: Int, val r3: Int) {
  operator fun get(index: Int): Int = when (index) {
    0 -> r0
    1 -> r1
    2 -> r2
    3 -> r3
    else -> error("Invalid register index: $index")
  }

  fun set(index: Int, value: Int): State = when (index) {
    0 -> copy(r0 = value)
    1 -> copy(r1 = value)
    2 -> copy(r2 = value)
    3 -> copy(r3 = value)
    else -> error("Invalid register index: $index")
  }

  companion object {
    fun fromList(list: List<Int>): State = State(list[0], list[1], list[2], list[3])
  }
}

data class Instruction(val opcode: Int, val a: Int, val b: Int, val c: Int) {
  companion object {
    fun fromList(list: List<Int>): Instruction = Instruction(list[0], list[1], list[2], list[3])
  }
}

enum class Operation(val func: (State, Instruction) -> State) {
  ADDR({ state, instr -> state.set(instr.c, state[instr.a] + state[instr.b]) }),
  ADDI({ state, instr -> state.set(instr.c, state[instr.a] + instr.b) }),
  MULR({ state, instr -> state.set(instr.c, state[instr.a] * state[instr.b]) }),
  MULI({ state, instr -> state.set(instr.c, state[instr.a] * instr.b) }),
  BANR({ state, instr -> state.set(instr.c, state[instr.a] and state[instr.b]) }),
  BANI({ state, instr -> state.set(instr.c, state[instr.a] and instr.b) }),
  BORR({ state, instr -> state.set(instr.c, state[instr.a] or state[instr.b]) }),
  BORI({ state, instr -> state.set(instr.c, state[instr.a] or instr.b) }),
  SETR({ state, instr -> state.set(instr.c, state[instr.a]) }),
  SETI({ state, instr -> state.set(instr.c, instr.a) }),
  GTIR({ state, instr -> state.set(instr.c, if (instr.a > state[instr.b]) 1 else 0) }),
  GTRI({ state, instr -> state.set(instr.c, if (state[instr.a] > instr.b) 1 else 0) }),
  GTRR({ state, instr -> state.set(instr.c, if (state[instr.a] > state[instr.b]) 1 else 0) }),
  EQIR({ state, instr -> state.set(instr.c, if (instr.a == state[instr.b]) 1 else 0) }),
  EQRI({ state, instr -> state.set(instr.c, if (state[instr.a] == instr.b) 1 else 0) }),
  EQRR({ state, instr -> state.set(instr.c, if (state[instr.a] == state[instr.b]) 1 else 0) });
}

val samples = readInput().chunked(4)
  .takeWhile { it.first().startsWith("Before:") }
  .map { chunk ->
    val before = extractInts(chunk[0])
    val instruction = extractInts(chunk[1])
    val after = extractInts(chunk[2])
    Sample(State.fromList(before), Instruction.fromList(instruction), State.fromList(after))
  }

val instructions = readInput()
  .drop(samples.size * 4 + 2)
  .map { Instruction.fromList(extractInts(it)) }

fun findMatchingOps(sample: Sample): Set<Operation> =
  Operation.values().filter { op ->
    op.func(sample.before, sample.instruction) == sample.after
  }.toSet()

fun first(): Int = samples.count { sample -> findMatchingOps(sample).size >= 3 }

fun second(): Int = run {
  val initialPossibilities = samples
    .groupBy { it.instruction.opcode }
    .mapValues { (_, samplesForOpcode) ->
      samplesForOpcode
        .map { findMatchingOps(it) }
        .reduce { acc, ops -> acc intersect ops }
    }

  fun resolve(possibilities: Map<Int, Set<Operation>>): Map<Int, Operation> {
    tailrec fun loop(remaining: Map<Int, Set<Operation>>, resolved: Map<Int, Operation>): Map<Int, Operation> {
      if (remaining.isEmpty()) return resolved

      val (opcode, ops) = remaining.entries.first { it.value.size == 1 }
      val op = ops.first()

      val newRemaining = remaining
        .filterKeys { it != opcode }
        .mapValues { (_, opsSet) -> opsSet - op }

      return loop(newRemaining, resolved + (opcode to op))
    }

    return loop(possibilities, emptyMap())
  }

  val opcodeMapping = resolve(initialPossibilities)

  instructions
    .fold(State(0, 0, 0, 0)) { state, instr ->
      opcodeMapping[instr.opcode]!!.func(state, instr)
    }
    .r0
}
