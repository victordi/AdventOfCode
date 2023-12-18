package AoC2021.Day24

import utils.Input.withInput

fun main() {
    println("Result of the first part : " + first().toString())
    println("Result of the second part: " + second().toString())
}

data class Registers(val x:Int, val y: Int, val z: Int, val w: Int)
data class Op(val op: String, val reg1: Char, val reg2: Int?)

val validNumbers = mutableListOf<List<Int>>()

var foundLargest = false
var foundSmallest = false

fun largestNumber(registers: Registers, actions: List<String>, number: List<Int>) {
    if (foundLargest) return
    if (actions.isEmpty()) {
        if (registers.z == 0) {
            foundLargest = true
            validNumbers.add(number)
        }
        return
    }
    val action = actions.first().parse(registers)
    when (action.op) {
        "add" -> largestNumber(registers.applyOp(action) { x, y -> x + y!! }, actions.drop(1), number)
        "mul" -> largestNumber(registers.applyOp(action) { x, y -> x * y!! }, actions.drop(1), number)
        "div" -> largestNumber(registers.applyOp(action) { x, y -> x / y!! }, actions.drop(1), number)
        "mod" -> largestNumber(registers.applyOp(action) { x, y -> x % y!! }, actions.drop(1), number)
        "eql" -> largestNumber(registers.applyOp(action) { x, y -> if(x == y) 1 else 0 }, actions.drop(1), number)
        else  -> {
            if (firstWPart1.isNotEmpty()) {
                val w = firstWPart1.first()
                firstWPart1.removeAt(0)
                largestNumber(registers.applyOp(action) { _, _ -> w }, actions.drop(1), number + w)
            } else {
                (9 downTo 1).forEach {
                    largestNumber(registers.applyOp(action) { _, _ -> it }, actions.drop(1), number + it)
                }
            }
        }
    }
}

fun smallestNumber(registers: Registers, actions: List<String>, number: List<Int>) {
    if (foundSmallest) return
    if (actions.isEmpty()) {
        if (registers.z == 0) {
            foundSmallest = true
            validNumbers.add(number)
        }
        return
    }
    val action = actions.first().parse(registers)
    when (action.op) {
        "add" -> smallestNumber(registers.applyOp(action) { x, y -> x + y!! }, actions.drop(1), number)
        "mul" -> smallestNumber(registers.applyOp(action) { x, y -> x * y!! }, actions.drop(1), number)
        "div" -> smallestNumber(registers.applyOp(action) { x, y -> x / y!! }, actions.drop(1), number)
        "mod" -> smallestNumber(registers.applyOp(action) { x, y -> x % y!! }, actions.drop(1), number)
        "eql" -> smallestNumber(registers.applyOp(action) { x, y -> if(x == y) 1 else 0 }, actions.drop(1), number)
        else  -> {
            if (firstWPart2.isNotEmpty()) {
                val w = firstWPart2.first()
                firstWPart2.removeAt(0)
                smallestNumber(registers.applyOp(action) { _, _ -> w }, actions.drop(1), number + w)
            } else {
                (1..9).forEach {
                    smallestNumber(registers.applyOp(action) { _, _ -> it }, actions.drop(1), number + it)
                }
            }
        }
    }
}

fun Registers.applyOp(op: Op, f: (Int, Int?) -> Int): Registers {
    return when (op.reg1) {
        'x' -> this.copy(x = f(x, op.reg2))
        'y' -> this.copy(y = f(y, op.reg2))
        'z' -> this.copy(z = f(z, op.reg2))
        else -> this.copy(w = f(w, op.reg2))
    }
}

fun String.parse(registers: Registers): Op {
    val split = this.split(' ')
    return if (split.size == 2) {
        Op(split[0], split[1][0], null)
    } else {
        val aux = split[2].toIntOrNull() ?: when (split[2][0]) {
            'x' -> registers.x
            'y' -> registers.y
            'z' -> registers.z
            else -> registers.w
        }
        Op(split[0], split[1][0], aux)
    }
}

val firstWPart1 = mutableListOf(9, 9, 9, 1 ,1, 9, 9, 3)
val firstWPart2 = mutableListOf(6, 2, 9, 1, 1, 9)

fun first(): String {
    var result = ""
    withInput { input ->
        val lines = input.toList()
        largestNumber(Registers(0, 0, 0, 0), lines, emptyList())
        result = validNumbers[0].fold("") { acc, nr -> acc + nr}
    }
    return result
}

fun second(): String {
    var result = ""
    withInput { input ->
        val lines = input.toList()
        smallestNumber(Registers(0, 0, 0, 0), lines, emptyList())
        result = validNumbers[1].fold("") { acc, nr -> acc + nr}
    }
    return result
}