package advent2022.day1

import Input.withInput
import split

fun main() {
    println(first())
    println(second())
}

fun first(): Int = withInput { input ->
    input
        .toList()
        .split { it.isEmpty() }
        .map { elf -> elf.sumOf { it.toInt() } }
        .max()
}

fun second(): Int = withInput { input ->
    input
        .toList()
        .split { it.isEmpty() }
        .map { elf -> elf.sumOf { it.toInt() } }
        .sortedDescending()
        .take(3)
        .sum()
}