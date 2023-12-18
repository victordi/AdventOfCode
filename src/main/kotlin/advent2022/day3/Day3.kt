package advent2022.day3

import utils.Input.withInput

fun Char.priority(): Int = if (isLowerCase()) code - 96 else code - 38

fun main() {
    println(first())
    println(second())
}

fun first(): Int = withInput { input ->
    var priority = 0
    input.forEach {
        val first = it.substring(0, it.length / 2).toCharArray()
        val second = it.substring(it.length / 2).toCharArray()
        val common = first.intersect(second.toSet()).first()
        priority += common.priority()
    }
    priority
}

fun second(): Int = withInput { input ->
    var priority = 0
    input
        .toList()
        .map { it.toCharArray().toSet() }
        .chunked(3)
        .forEach { (elf1, elf2, elf3) ->
            val common = elf1.intersect(elf2).intersect(elf3).first()
            priority += common.priority()
        }
    priority
}