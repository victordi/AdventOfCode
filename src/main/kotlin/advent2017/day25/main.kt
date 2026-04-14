package advent2017.day25

import utils.Input.readInput
import utils.extractInts
import utils.splitList

fun main() {
  println(first())
}

val ones = mutableSetOf<Int>()

data class State(val state: Char, val position: Int) {
  fun performAction(): State = run {
    val rule = actions.first { it.state == state }
    val currentValue = if (position in ones) 1 else 0
    val action = if (currentValue == 0) rule.on0 else rule.on1
    if (action.write == 1) {
      ones.add(position)
    } else {
      ones.remove(position)
    }
    State(action.nextState.first(), position + action.move)
  }
}
data class Action(val write: Int, val move: Int, val nextState: String)
data class Rule(val state: Char, val on0: Action, val on1: Action)

val input = readInput()
val actions = input.drop(3).splitList { it.isEmpty() }.map { block ->
  val state = block[0].trim().dropLast(1).last()
  val on0 = Action(
    write = block[2].trim().dropLast(1).last().digitToInt(),
    move = if (block[3].contains("right")) 1 else -1,
    nextState = block[4].trim().dropLast(1).last().toString()
  )
  val on1 = Action(
    write = block[6].trim().dropLast(1).last().digitToInt(),
    move = if (block[7].contains("right")) 1 else -1,
    nextState = block[8].trim().dropLast(1).last().toString()
  )
  Rule(state, on0, on1)
}

fun first(): Int = run {
  val state = State(input.first().dropLast(1).last(), 0)
  val steps = extractInts(input[1]).first()
  generateSequence(state) { it.performAction() }
    .drop(steps)
    .first()
  ones.size
}
