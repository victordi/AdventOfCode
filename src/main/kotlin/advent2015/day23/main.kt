package advent2015.day23

import utils.Input
import utils.println
import utils.splitTwo

val lines = Input.readInput()

data class Instruction(val type: String, val value: Int? = null, val register: String? = null)
val instructions = lines.map {
  val (type, rest) = it.split(" ", limit = 2)
  when (type) {
    "jmp" -> Instruction(type, rest.toInt())
    "jie", "jio" -> {
      val (register, value) = rest.splitTwo(", ")
      Instruction(type, value.toInt(), register)
    }
    else -> {
      val register = rest
      Instruction(type, null, register)
    }
  }
}

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  var a = 0
  var b = 0
  var i = 0
  while (i < instructions.size) {
    val (type, value, register) = instructions[i]
    when (type) {
      "hlf" -> {
        if (register == "a") a /= 2
        if (register == "b") b /= 2
        i++
      }
      "tpl" -> {
        if (register == "a") a *= 3
        if (register == "b") b *= 3
        i++
      }
      "inc" -> {
        if (register == "a") a++
        if (register == "b") b++
        i++
      }
      "jmp" -> i += value!!
      "jie" -> {
        if ((if (register == "a") a else b) % 2 == 0) i += value!!
        else i++
      }
      "jio" -> {
        if ((if (register == "a") a else b) == 1) i += value!!
        else i++
      }
    }
  }
  b
}

fun second(): Int = run {
  var a = 1
  var b = 0
  var i = 0
  while (i < instructions.size) {
    val (type, value, register) = instructions[i]
    when (type) {
      "hlf" -> {
        if (register == "a") a /= 2
        if (register == "b") b /= 2
        i++
      }
      "tpl" -> {
        if (register == "a") a *= 3
        if (register == "b") b *= 3
        i++
      }
      "inc" -> {
        if (register == "a") a++
        if (register == "b") b++
        i++
      }
      "jmp" -> i += value!!
      "jie" -> {
        if ((if (register == "a") a else b) % 2 == 0) i += value!!
        else i++
      }
      "jio" -> {
        if ((if (register == "a") a else b) == 1) i += value!!
        else i++
      }
    }
  }
  b
}