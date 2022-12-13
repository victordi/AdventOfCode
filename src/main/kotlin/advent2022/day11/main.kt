package advent2022.day11

import AoCPart
import Input.readInput
import Part1
import Part2
import splitList

fun main() {
    println(solve(20, Part1))
    println(solve(10_000, Part2))
}

enum class Op {
    Plus, Mul
}

val MOD = readInput()
    .filter { it.contains("Test: divisible by") }
    .map { it.trimIndent().removePrefix("Test: divisible by ").toLong() }
    .fold(1L) { acc, e -> acc * e }

fun Char.toOp(): Op = if (this == '+') Op.Plus else Op.Mul

data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val nextMonkey: (Long) -> Int
)

fun parseInput(part: AoCPart): MutableList<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    val input = readInput().splitList { it.isEmpty() }
    input.map { it.drop(1) }.forEach { monkeyDetails ->
        val items = monkeyDetails[0].trimIndent().removePrefix("Starting items: ").filter { it != ' ' }.split(",").map { it.toLong() }
        val (op, nr) = monkeyDetails[1].trimIndent().removePrefix("Operation: new = old ").filter { it != ' ' }.let {
            it[0].toOp() to it.drop(1)
        }
        val div = monkeyDetails[2].trimIndent().removePrefix("Test: divisible by ").toLong()
        val ifTrue = monkeyDetails[3].trimIndent().removePrefix("If true: throw to monkey ").toInt()
        val ifFalse = monkeyDetails[4].trimIndent().removePrefix("If false: throw to monkey ").toInt()
        val monkey = Monkey(
            items.toMutableList(),
            {
                val x = if (nr == "old") it else nr.toLong()
                val result = when (op) {
                    Op.Plus -> it + x
                    Op.Mul -> it * x
                }
                if (part == Part1) result / 3
                else result % MOD
            },
            { if (it % div == 0L) ifTrue else ifFalse }
        )
        monkeys.add(monkey)
    }
    return monkeys
}

fun solve(rounds: Int, part: AoCPart): Long = run {
    val monkeys = parseInput(part)
    val inspections = Array(monkeys.size) { 0L }
    (1..rounds).forEach { _ ->
        for ((idx, monkey) in monkeys.withIndex()) { with(monkey) {
            inspections[idx] += items.size.toLong()
            items.forEach {
                val next = operation(it)
                val nextMonkey = nextMonkey(next)
                monkeys[nextMonkey].items.add(next)
            }
            items.clear()
        } }
    }

    inspections
        .sortedDescending()
        .take(2)
        .let { it[0] * it[1] }
}
