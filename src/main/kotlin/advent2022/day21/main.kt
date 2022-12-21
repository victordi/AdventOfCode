package advent2022.day21

import java.util.*

data class Monkey(val name: String, val x: String, val y: String, val op: Char)
val numbers = mutableMapOf<String, Long>()
fun parseInput(): List<Monkey> = Input.readInput().mapNotNull {
    val name = it.take(4)
    val rest = it.drop(6)
    if (rest.toIntOrNull() != null) {
        numbers[name] = rest.toLong()
        null
    } else {
        val x = rest.take(4)
        val y = rest.drop(7).take(4)
        val op = rest.drop(5)[0]
        Monkey(name, x, y, op)
    }
}

val monkeys = parseInput().toMutableList()

fun main() {
    println(first())
    println(second())
}

fun first(): Long = run {
    repeat(100_000) { cnt ->
        for (monkey in monkeys) {
            if (monkey.x in numbers && monkey.y in numbers) {
                val number = when (monkey.op) {
                    '+' -> numbers[monkey.x]!! + numbers[monkey.y]!!
                    '-' -> numbers[monkey.x]!! - numbers[monkey.y]!!
                    '*' -> numbers[monkey.x]!! * numbers[monkey.y]!!
                    '/' -> numbers[monkey.x]!! / numbers[monkey.y]!!
                    else -> throw IllegalArgumentException("Unsupported op: ${monkey.op}")
                }
                numbers[monkey.name] = number
                if (monkey.name == "root") return number
            }
        }
    }
    -1
}

fun Char.inverseOp(): Char = when (this) {
    '+' -> '-'
    '-' -> '+'
    '*' -> '/'
    '/' -> '*'
    else -> throw IllegalArgumentException("Unsupported op: $this")
}

fun Long.applyOp(op: Char, other: Long): Long = when(op.inverseOp()) {
    '+' -> this + other
    '-' -> this - other
    '*' -> this * other
    '/' -> this / other
    else -> throw IllegalArgumentException("Unsupported op: $this")
}

fun second(): Long = run { // root : 21973580688943 == lccz
    numbers.clear()
    val monkeys = parseInput()
    numbers.remove("humn")
    repeat(38) { cnt ->
        for (monkey in monkeys) {
            if (monkey.x in numbers && monkey.y in numbers) {
                val number = when (monkey.op) {
                    '+' -> numbers[monkey.x]!! + numbers[monkey.y]!!
                    '-' -> numbers[monkey.x]!! - numbers[monkey.y]!!
                    '*' -> numbers[monkey.x]!! * numbers[monkey.y]!!
                    '/' -> numbers[monkey.x]!! / numbers[monkey.y]!!
                    else -> throw IllegalArgumentException("Unsupported op: ${monkey.op}")
                }
                numbers[monkey.name] = number
            }
        }
    }
    val current = LinkedList<String>()
    current.add("lccz")
    var humanRemainder = 21973580688943L
    while(current.isNotEmpty()) {
        val name = current.pop()
        if (name == "humn") continue
        val monkey = monkeys.find { it.name == name }!!
        humanRemainder = if (monkey.x in numbers) {
            current.add(monkey.y)
            if (monkey.op == '-') numbers[monkey.x]!!.applyOp('+', humanRemainder)
            else humanRemainder.applyOp(monkey.op, numbers[monkey.x]!!)
        } else humanRemainder.applyOp(monkey.op, numbers[monkey.y]!!).also { current.add(monkey.x) }
    }
    humanRemainder
}