package advent2015.day7

import utils.Input.withInput
import utils.splitTwo

sealed class Operation(val gate1: String?, val gate2: String?, val value: Int?) {
  class AND(g1: String, g2: String) : Operation(g1, g2, null)
  class OR(g1: String, g2: String) : Operation(g1, g2, null)
  class NOT(g1: String) : Operation(g1, null, null)
  class VALUE(value: Int?, g: String?) : Operation(g, null, value)
  class LSHIFT(g1: String, value: Int) : Operation(g1, null, value)
  class RSHIFT(g1: String, value: Int) : Operation(g1, null, value)
}

fun String.toOp(): Operation {
  val parts = this.split(" ")
  return when {
    parts.size == 1 -> {
      if (parts[0].toIntOrNull() != null) Operation.VALUE(parts[0].toInt(), null)
      else Operation.VALUE(null, parts[0])
    }
    parts.size == 2 -> {
      Operation.NOT(parts[1])
    }
    parts[1] == "AND" -> {
      Operation.AND(parts[0], parts[2])
    }
    parts[1] == "OR" -> {
      Operation.OR(parts[0], parts[2])
    }
    parts[1] == "LSHIFT" -> {
      Operation.LSHIFT(parts[0], parts[2].toInt())
    }
    parts[1] == "RSHIFT" -> {
      Operation.RSHIFT(parts[0], parts[2].toInt())
    }
    else -> error("Unknown operation")
  }
}

val wireValues = mutableMapOf<String, Int>()
val wires = withInput { input ->
  input.toList()
    .map { it.splitTwo(" -> ") }
    .map { (op, output) ->
      val operation = op.toOp()
      if (operation is Operation.VALUE && operation.value != null) wireValues[output] = operation.value
      operation to output
    }
}

fun main() {
  println(solve())
}

fun Operation.getValues(): Pair<Int?, Int?> =
  (gate1?.toIntOrNull() ?: wireValues[gate1]) to (gate2?.toIntOrNull() ?: wireValues[gate2])

fun solve(): Int = run {
  while ("a" !in wireValues) {
    wires.forEach { (operation, output) ->
      val (v1, v2) = operation.getValues()
      if (output in wireValues) return@forEach
      when (operation) {
        is Operation.AND -> {
          if (v1 != null && v2 != null) wireValues[output] = v1 and v2
        }
        is Operation.OR -> {
          if (v1 != null && v2 != null) wireValues[output] = v1 or v2
        }
        is Operation.NOT -> {
          if (v1 != null) wireValues[output] = v1.inv() and 0xffff
        }
        is Operation.LSHIFT -> {
          if (v1 != null) wireValues[output] = v1 shl operation.value!!
        }
        is Operation.RSHIFT -> {
          if (v1 != null) wireValues[output] = v1 shr operation.value!!
        }
        is Operation.VALUE -> {
          if (v1 != null) wireValues[output] = v1
        }
      }
    }
  }
  wireValues["a"] ?: -1
}
