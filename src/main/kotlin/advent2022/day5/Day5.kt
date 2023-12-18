package advent2022.day5

import utils.Input.readInput
import java.util.*


fun main() {
    println(first())
    println(second())
}

fun first(): String = run {
    val list = readInput()
    val stacks = parseCrates(list)
    list
        .dropWhile { it.isNotEmpty() }.drop(1)
        .forEach {
            val split = it.split(" ")
            val toMove = split[1].toInt()
            val from = split[3].toInt() - 1
            val to = split[5].toInt() - 1

            repeat(toMove) {
                if (stacks[from].isNotEmpty()) {
                    stacks[to].push(stacks[from].pop())
                }
            }

        }
    var result = ""
    for(stack in stacks) {
        result += stack.pop()
    }

    result
}

fun second(): String = run {
    val list = readInput()
    val stacks = parseCrates(list)
    list
        .dropWhile { !it.isEmpty() }.drop(1)
        .forEach {
            val split = it.split(" ")
            val toMove = split[1].toInt()
            val from = split[3].toInt() - 1
            val to = split[5].toInt() - 1

            var moved = ""
            repeat(toMove) { moved += stacks[from].pop() }
            moved.reversed().forEach { crate -> stacks[to].push(crate) }
        }
    var result = ""
    for(stack in stacks) {
        result += stack.pop()
    }

    result
}

fun parseCrates(input: List<String>): List<Stack<Char>> {
    val columns = (input.first().length  + 1 ) / 4
    val stacks = mutableListOf<Stack<Char>>()
    repeat(columns) { stacks.add(Stack<Char>()) }

    input
        .takeWhile { !it.isEmpty() }
        .reversed()
        .forEach {
            val crates = it.chunked(4)
            crates.forEachIndexed { idx, crate ->
                if (crate.contains('[')) {
                    stacks[idx].push(crate[1])
                }
            }
        }
    return stacks
}