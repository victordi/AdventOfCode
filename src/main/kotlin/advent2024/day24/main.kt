package advent2024.day24

import utils.Input.withInput
import utils.println
import kotlin.random.Random

fun main() {
  println(first())
  println(second())
}

data class GateRule(val gate1: String, val gate2: String, val operation: String, val output: String)

fun String.toGateRule(): GateRule {
  val (gates, output) = this.split(" -> ")
  val (gate1, operation, gate2) = gates.split(" ")
  return GateRule(gate1, gate2, operation, output)
}

val gateValues: MutableMap<String, Int> = mutableMapOf()
val input = withInput { input ->
  val list = input.toList()
  val first = list.takeWhile { it.isNotBlank() }
  val second = list.drop(first.size + 1)
  first.forEach {
    val (gate, value) = it.split(": ")
    gateValues[gate] = value.toInt()
  }
  second.map { it.toGateRule() }
}

fun first(): Long = run {
  input.solve().third
}

fun List<GateRule>.solve(): Triple<Long, Long, Long> = run {
  val gates = mutableMapOf<String, Int>()
  gates.putAll(gateValues)

  val visited = mutableSetOf<GateRule>()
  var changed = true
  while (visited.size != this.size && changed) {
    changed = false
    this.filter { it !in visited }.forEach { (gate1, gate2, operation, output) ->
      if (gate1 in gates && gate2 in gates) {
        changed = true
        val value1 = gates[gate1]!!
        val value2 = gates[gate2]!!
        gates[output] = when (operation) {
          "AND" -> value1 and value2
          "OR" -> value1 or value2
          "XOR" -> value1 xor value2
          else -> error("Unknown operation")
        }
        visited.add(GateRule(gate1, gate2, operation, output))
      }
    }
  }

  val result = listOf("x", "y", "z")
    .map { key ->
      gates.keys
        .filter { it.startsWith(key) }
        .sortedDescending()
        .joinToString("") { gates[it].toString() }
    }
    .map { it.toLong(2) }

  Triple(result[0], result[1], result[2])
}

fun second(): String = run {
  val (x, y, z) = input.solve()
  val correctZ = java.lang.Long.toBinaryString(x + y)
  val actualZ = java.lang.Long.toBinaryString(z)
  actualZ.println
  correctZ.println
//  val diffs = correctZ.reversed().zip(actualZ.reversed()).count { it.first != it.second }
//  println(diffs)
  // z06(169) z31(200) z37(149) are wrong
  // cgr(101) -> z37, hwk(4) -> z06 wrong -> only in 2 GateRule

  // => swap 169 with 4 and 149 with 101
  val swappedInput = input.indices.map {
    if (it == 169) input[it].copy(output = input[4].output)
    else if (it == 4) input[it].copy(output = input[169].output)
    else if (it == 149) input[it].copy(output = input[101].output)
    else if (it == 101) input[it].copy(output = input[149].output)
    else input[it]
  }
  val (_, _, z1) = swappedInput.solve()
  val swappedZ = java.lang.Long.toBinaryString(z1)
  swappedZ.println

//  val diffs = correctZ.reversed().zip(swappedZ.reversed()).count { it.first != it.second }
//  println(diffs)
  // z25 is first wrong bit -> tnt(172) wrong
  // swap tnt with z25 (x25 xor y25 = qmd(16))
  val swappedInput2 = swappedInput.indices.map {
    if (it == 172) swappedInput[it].copy(output = swappedInput[16].output)
    else if (it == 16) swappedInput[it].copy(output = swappedInput[172].output)
    else swappedInput[it]
  }
  val (_, _, z2) = swappedInput2.solve()
  val swappedZ2 = java.lang.Long.toBinaryString(z2)
  swappedZ2.println
  // z31(200) and one last swap one of inputs used for z31 or z32 should be the wrong one -> try them all
  // mjr(70) hgw(196) hpc(205) qrw(38)
  listOf(70, 196, 205, 38).forEach { swap ->
    val swappedInput3 = swappedInput2.indices.map {
      if (it == 200) swappedInput2[it].copy(output = swappedInput2[swap].output)
      else if (it == swap) swappedInput2[it].copy(output = swappedInput2[200].output)
      else swappedInput2[it]
    }
    val (_, _, z3) = swappedInput3.solve()
    if (z3 == x + y) println(swap)
  }
  // hpc wrong
  listOf("z06", "z31", "z37", "cgr", "hwk", "tnt", "qmd", "hpc").sorted().joinToString(",")
}
