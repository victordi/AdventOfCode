package advent2023.day20

import advent2023.day20.Type.Broadcaster
import advent2023.day20.Type.Conjunction
import advent2023.day20.Type.FlipFlop
import utils.Input.readInput
import utils.lcm
import utils.println

fun main() {
  initStates()
  solve()
}

val modules: Map<String, Pair<Type, List<String>>> = readInput().associate {
  val type = Type.from(it.first())
  val (current, destinations) = it.split(" -> ")
  val module = if (current == "broadcaster") current else current.drop(1)
  Pair(module, type to destinations.split(",").map { dest -> dest.trim() })
}

val states: MutableMap<String, Pair<Boolean, MutableMap<String, Boolean>>> = mutableMapOf()

var rxConnectors: List<String> = emptyList()
val previousLowConnector: MutableMap<String, Long> = mutableMapOf()
val connectorsCycles: MutableMap<String, Long> = mutableMapOf()

fun initStates() {
  modules.forEach { module, (type, _) ->
    when (type) {
      FlipFlop, Broadcaster -> states[module] = false to mutableMapOf()
      Conjunction -> {
        val incomingModules = modules
          .entries
          .filter { (_, dest) -> dest.second.contains(module) }
          .associate { it.key to false }
          .toMutableMap()
        states[module] = false to incomingModules
      }
    }
  }
  rxConnectors = states["zh"]!!.second.keys.toList()
}

enum class Type {
  Broadcaster, FlipFlop, Conjunction;

  companion object {
    fun from(c: Char) = when (c) {
      '%' -> FlipFlop
      '&' -> Conjunction
      else -> Broadcaster
    }
  }
}

var lowSignals = 0
var highSignals = 0

fun buttonPress(cnt: Long = 0): Boolean {
  if (connectorsCycles.size == rxConnectors.size) return true
  if (cnt == 1000L) println(lowSignals.toLong() * highSignals.toLong())

  val queue = mutableListOf(Triple("broadcaster", false, "button"))
  while (queue.isNotEmpty()) {
    val (module, signal, from) = queue.removeAt(0)
    if (signal) highSignals++ else lowSignals++
    if (!modules.contains(module)) continue

    if (module in rxConnectors && !signal) {
      if (module in previousLowConnector) connectorsCycles[module] = cnt - previousLowConnector[module]!!
      else previousLowConnector[module] = cnt
    }

    val (type, connectedModules) = modules[module]!!
    val emittedSignal = when (type) {
      Broadcaster -> signal
      FlipFlop -> {
        if (!signal) {
          val state = states[module]!!.first
          states[module] = !state to mutableMapOf()
          !state
        } else continue
      }
      Conjunction -> {
        states[module]!!.second[from] = signal
        !states[module]!!.second.values.all { it }
      }
    }
    queue.addAll(connectedModules.map { Triple(it, emittedSignal, module) })
  }
  return false
}

fun solve() {
  var cnt = 0L
  while(!buttonPress(cnt)) cnt++
  connectorsCycles.values.fold(1L) { acc, cycle -> lcm(acc, cycle) }.println
}
