package advent2018.day12

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

val input = readInput()
val initialState = input.first().substringAfter(": ")
val rules = input.drop(2).associate { line ->
  val (pattern, result) = line.split(" => ")
  pattern to result[0]
}

data class State(val pots: Set<Int>, val offset: Int) {
  fun normalize(): State {
    val minPot = pots.minOrNull() ?: 0
    return State(pots.map { it - minPot }.toSet(), offset + minPot)
  }

  fun sum(): Long = pots.sumOf { (it + offset).toLong() }
}

fun State.evolve(): State {
  val minPot = (pots.minOrNull() ?: 0) - 2
  val maxPot = (pots.maxOrNull() ?: 0) + 2

  val newPots = (minPot..maxPot).filter { pot ->
    val pattern = (-2..2).map { delta ->
      if (pot + delta in pots) '#' else '.'
    }.joinToString("")

    rules[pattern] == '#'
  }.toSet()

  return State(newPots, offset)
}

fun String.toState(): State = State(
  indices.filter { this[it] == '#' }.toSet(),
  0
)

fun first(): Long {
  val initialPotState = initialState.toState()

  return generateSequence(initialPotState) { it.evolve() }
    .drop(20)
    .first()
    .sum()
}

fun second(): Long {
  val initialPotState = initialState.toState()

  val states = generateSequence(initialPotState) { it.evolve() }
    .take(1000)
    .map { it.normalize() }
    .toList()

  val (cycleStart, _) = states.withIndex().find { (idx, state) ->
    idx > 0 && states.drop(idx + 1).any { it.pots == state.pots }
  } ?: error("No cycle found")

  val (stateAtCycleStart, stateAtCycleEnd) = generateSequence(initialPotState) { it.evolve() }
    .drop(cycleStart)
    .take(2)
    .toList()

  val sumDiffPerGeneration = stateAtCycleEnd.sum() - stateAtCycleStart.sum()
  val generationsRemaining = 50_000_000_000L - cycleStart

  return stateAtCycleStart.sum() + sumDiffPerGeneration * generationsRemaining
}
