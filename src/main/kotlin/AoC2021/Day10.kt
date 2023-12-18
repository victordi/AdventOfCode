package AoC2021.day10

import utils.Input.withInput
import java.util.*

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Long {
    var result = 0L
    withInput { input ->
        val list = input.toList()
        for (line in list) {
            val stack = Stack<Char>()
            for (char in line) {
                if (char in "([{<") {
                    stack.add(char.toReverse())
                } else {
                    val c = stack.pop()
                    if (c != char) {
                        result += when(char) {
                            ')' -> 3
                            ']' -> 57
                            '}' -> 1197
                            '>' -> 25137
                            else -> 0
                        }
                        break
                    }
                }
            }
        }
    }
    return result
}

fun Char.toReverse(): Char = when(this) {
    '(' -> ')'
    '{' -> '}'
    '[' -> ']'
    '<' -> '>'
    else -> 'x'
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val results = mutableListOf<Long>()
        val list = input.toList()
        for (line in list) {
            var stack = Stack<Char>()
            for (char in line) {
                if (char in "([{<") {
                    stack.add(char.toReverse())
                } else {
                    val c = stack.pop()
                    if (c != char) {
                        stack = Stack()
                        break
                    }
                }
            }
            var current = 0L
            while (stack.size > 0) {
                val char = stack.pop()
                current = current * 5 + when(char) {
                    ')' -> 1
                    ']' -> 2
                    '}' -> 3
                    '>' -> 4
                    else -> 0
                }
            }
            if (current != 0L) results.add(current)
        }
        results.sort()
        result = results[results.size / 2]
    }
    return result
}