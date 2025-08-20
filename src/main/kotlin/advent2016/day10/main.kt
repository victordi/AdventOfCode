package advent2016.day10

import utils.Input.withInput
import utils.println

fun main() {
  solve()
}

sealed class Instruction
data class Bot(val id: Int, val low: Int, val high: Int, val isOutput: Boolean = false) : Instruction()
data class Value(val value: Int, val botId: Int) : Instruction()

val instructions = withInput { input ->
  val instr = input.toList().map { line ->
    when {
      line.startsWith("bot") -> {
        val parts = line.split(" ").map { it.toIntOrNull() ?: -1 }
        Bot(parts[1], parts[6], parts[11])
      }

      line.startsWith("value") -> {
        val parts = line.split(" ")
        Value(parts[1].toInt(), parts[5].toInt())
      }

      else -> throw IllegalArgumentException("Unknown instruction: $line")
    }
  }
  val bots = instr.filterIsInstance<Bot>()
  val values = instr.filterIsInstance<Value>()
  bots to values
}

fun solve() = run {
  val (bots, values) = instructions
  val botMap = mutableMapOf<Int, MutableList<Int>>()
  values.forEach { (value, botId) ->
    botMap.computeIfAbsent(botId) { mutableListOf() }.add(value)
  }
  val instructions = bots.toMutableSet()
  while (botMap.values.any { it.size > 1 }) {
    botMap
      .filter { it.value.size > 1 }
      .keys
      .forEach { activeBot ->
        val chips = botMap[activeBot]!!.sorted()
        if (chips.first() == 17 && chips.drop(1).first() == 61) {
          println("Bot $activeBot is comparing 17 and 61")
        }
        val botInstruction = instructions.first { it.id == activeBot }
        botMap.computeIfAbsent(botInstruction.low) { mutableListOf() }.add(chips.first())
        botMap.computeIfAbsent(botInstruction.high) { mutableListOf() }.add(chips.drop(1).first())
        botMap[activeBot]!!.clear()
      }
  }
  val prod = botMap[0]!!.first() * botMap[1]!!.first() * botMap[2]!!.first()
  println("Part2 : $prod")
}
