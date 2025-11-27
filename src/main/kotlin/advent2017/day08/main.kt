package advent2017.day08

import utils.Input.withInput

fun main() {
  compute()
  println(first())
  println(second())
}

val registers: MutableMap<String, Int> = mutableMapOf()
var maxMet = 0

fun compute() {
  withInput { input ->
    input.toList().forEach { line ->
      val items = line.split(" ")
      val reg1 = items[0]
      val op = items[1]
      val nr = items[2].toInt()
      val reg2 = items[4]
      val cmp = items[5]
      val nr2 = items[6].toInt()

      registers.getOrPut(reg1) { 0 }
      val reg2Value = registers.getOrPut(reg2) { 0 }
      when (cmp) {
        ">" -> if (reg2Value > nr2) {
          if (op == "inc") registers[reg1] = registers[reg1]!! + nr
          else registers[reg1] = registers[reg1]!! - nr
        }
        "<" -> if (reg2Value < nr2) {
          if (op == "inc") registers[reg1] = registers[reg1]!! + nr
          else registers[reg1] = registers[reg1]!! - nr
        }
        ">=" -> if (reg2Value >= nr2) {
          if (op == "inc") registers[reg1] = registers[reg1]!! + nr
          else registers[reg1] = registers[reg1]!! - nr
        }
        "<=" -> if (reg2Value <= nr2) {
          if (op == "inc") registers[reg1] = registers[reg1]!! + nr
          else registers[reg1] = registers[reg1]!! - nr
        }
        "==" -> if (reg2Value == nr2) {
          if (op == "inc") registers[reg1] = registers[reg1]!! + nr
          else registers[reg1] = registers[reg1]!! - nr
        }
        "!=" -> if (reg2Value != nr2) {
          if (op == "inc") registers[reg1] = registers[reg1]!! + nr
          else registers[reg1] = registers[reg1]!! - nr
        }
      }
      maxMet = maxOf(maxMet, registers[reg1]!!)
    }
  }
}


fun first(): Int = registers.values.max()

fun second(): Int = maxMet