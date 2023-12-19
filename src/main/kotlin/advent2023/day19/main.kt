package advent2023.day19

import advent2023.day12.state
import utils.Input.withInput
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

fun main() {
  println(first())
  println(second())
}

sealed class Condition(open val nextState: String)
data class Comparable(val term: Char, val sign: Char, val value: Int, override val nextState: String) :
  Condition(nextState)

data class Fallback(override val nextState: String) : Condition(nextState)

fun String.toCondition(): Condition =
  if (contains(':')) {
    val (first, second) = this.split(':')
    Comparable(first[0], first[1], first.drop(2).toInt(), second)
  } else {
    Fallback(this)
  }


val instructions = withInput {
  it.takeWhile { it.isNotEmpty() }.map {
    val (instr, condition) = it.dropLast(1).split('{')
    instr to condition.split(',').map { it.toCondition() }
  }.toMap()
}

val workflows = withInput { input ->
  input.dropWhile { it.isNotEmpty() }.drop(1).map { line ->
    line.drop(1).dropLast(1).split(',').associate {
      it.first() to it.drop(2).toInt()
    }
  }.toList()
}

fun Map<Char, Int>.score(): Int {
  var instr = "in"
  while (true) {
    if (instr == "A") return values.sum()
    if (instr == "R") return 0
    val conditions = instructions[instr]!!
    var changed = false
    for (cond in conditions) {
      when (cond) {
        is Fallback -> {
          changed = true
          instr = cond.nextState
          break
        }

        is Comparable -> {
          val value = this[cond.term]!!
          if (cond.sign == '<' && value < cond.value) {
            changed = true
            instr = cond.nextState
            break
          }
          if (cond.sign == '>' && value > cond.value) {
            changed = true
            instr = cond.nextState
            break
          }
        }
      }
    }
    if (!changed) return 0
  }
}

fun first(): Int = workflows.sumOf { it.score() }

data class State(
  val instr: String,
  val xRange: IntRange,
  val mRange: IntRange,
  val aRange: IntRange,
  val sRange: IntRange
) {
  fun score(): BigInteger =
    if (instr == "A") xRange.score() * mRange.score() * aRange.score() * sRange.score()
    else BigInteger.ZERO
}

fun second(): BigInteger = run {
  var score = BigInteger.ZERO

  val queue: MutableList<State> = mutableListOf(State("in", 1..4000, 1..4000, 1..4000, 1..4000))
  while (queue.isNotEmpty()) {
    val state = queue.first()
    var (instr, xRange, mRange, aRange, sRange) = state
    queue.removeAt(0)

    if (instr == "A" || instr == "R") {
      score += state.score()
      continue
    }

    for (cond in instructions[instr]!!) {
      when (cond) {
        is Fallback -> {
          queue.add(State(cond.nextState, xRange, mRange, aRange, sRange))
          break
        }

        is Comparable -> {
          when (cond.term) {
            'x' -> {
              val split = splitRange(cond.sign, cond.value, xRange)
              queue.add(State(cond.nextState, split.first, mRange, aRange, sRange))
              xRange = split.second
            }

            'm' -> {
              val split = splitRange(cond.sign, cond.value, mRange)
              queue.add(State(cond.nextState, xRange, split.first, aRange, sRange))
              mRange = split.second
            }

            'a' -> {
              val split = splitRange(cond.sign, cond.value, aRange)
              queue.add(State(cond.nextState, xRange, mRange, split.first, sRange))
              aRange = split.second
            }

            's' -> {
              val split = splitRange(cond.sign, cond.value, sRange)
              queue.add(State(cond.nextState, xRange, mRange, aRange, split.first))
              sRange = split.second
            }
          }
        }
      }
    }
  }
  score
}

fun splitRange(sign: Char, value: Int, range: IntRange): Pair<IntRange, IntRange> = when (sign) {
  '<' -> range.first..min(range.last, value - 1) to max(range.first, value)..range.last
  '>' -> max(range.first, value + 1)..range.last to range.first..min(range.last, value)
  else -> throw IllegalArgumentException("$sign can only be < or >")
}

fun IntRange.score(): BigInteger = (last - first + 1).toBigInteger()
