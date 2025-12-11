package advent2025.day10

import com.microsoft.z3.Status
import utils.Input.readInput
import utils.extractInts

fun main() {
  println(first())
  println(second())
}

data class Machine(
  val config: Int,
  val buttons: List<List<Int>>,
  val joltage: List<Int>
)

fun String.toMachine(): Machine {
  val parts = this.split(" ")
  val config = parts[0].drop(1).dropLast(1).foldIndexed(0) { idx, acc, c ->
    if (c == '#') acc or (1 shl idx) else acc
  }
  val buttons = parts.drop(1).dropLast(1).map { str ->
    extractInts(str.drop(1).dropLast(1))
  }
  val joltage = extractInts(parts.last())
  return Machine(config, buttons, joltage)
}

fun Machine.pressesNeeded(): Int {
  fun Int.press(button: List<Int>): Int {
    var result = this
    button.forEach { idx ->
      result = result xor (1 shl idx)
    }
    return result
  }

  val start = 0

  if (start == config) return 0

  val queue = ArrayDeque<Pair<Int, Int>>()
  val visited = mutableSetOf<Int>()

  queue.add(start to 0)
  visited.add(start)

  while (queue.isNotEmpty()) {
    val (state, steps) = queue.removeFirst()

    for (button in buttons) {
      val newState = state.press(button)

      if (newState == config) {
        return steps + 1
      }

      if (newState !in visited) {
        visited.add(newState)
        queue.add(newState to steps + 1)
      }
    }
  }

  return -1
}

fun Machine.pressesNeededJoltage(): Int {
  val ctx = com.microsoft.z3.Context()
  val optimizer = ctx.mkOptimize()

  val buttonVars = buttons.indices.map { i ->
    ctx.mkIntConst("button_$i")
  }

  buttonVars.forEach { v ->
    optimizer.Add(ctx.mkGe(v, ctx.mkInt(0)))
  }

  joltage.indices.forEach { lightIdx ->
    val presses = buttons.indices.map { btnIdx ->
      if (lightIdx in buttons[btnIdx]) {
        buttonVars[btnIdx]
      } else {
        ctx.mkInt(0)
      }
    }
    val sum = ctx.mkAdd(*presses.toTypedArray())
    optimizer.Add(ctx.mkEq(sum, ctx.mkInt(joltage[lightIdx])))
  }

  val totalPresses = ctx.mkAdd(*buttonVars.toTypedArray())
  optimizer.MkMinimize(totalPresses)

  val result = optimizer.Check()

  return if (result == Status.SATISFIABLE) {
    val model = optimizer.model
    buttonVars.sumOf { v ->
      model.eval(v, false).toString().toInt()
    }
  } else {
    -1
  }.also {
    ctx.close()
  }
}

val machines = readInput().map { it.toMachine() }

fun first(): Int = machines.sumOf { it.pressesNeeded() }

fun second(): Int = machines.sumOf { it.pressesNeededJoltage() }
