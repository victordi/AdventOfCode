package advent2015.day5

import zip3

val lines = Input.readInput()

private const val vowels = "aeiou"
private val banned = listOf('a' to 'b', 'c' to 'd', 'p' to 'q', 'x' to 'y')

fun main() {
    println(first())
    println(second())
}

fun first(): Int = lines.filter { it.isNice() }.size

fun second(): Int = lines.filter { it.isNicePart2() }.size

fun String.isNice(): Boolean =
    if (this.filter { it in vowels }.length < 3) false
    else if (this.zipWithNext().none { (c1, c2) -> c1 == c2 }) false
    else !this.zipWithNext().any { it in banned }

fun String.isNicePart2(): Boolean =
    this.toList().zip3().any { (c1, _, c3) -> c1 == c3 } && this.duplicatePair()

fun String.duplicatePair(): Boolean = run {
    for (i in 0 until this.length - 1) {
        val str = "${this[i]}${this[i + 1]}"
        if (this.take(i).contains(str) || this.drop(i + 2).contains(str)) return true
    }

    false
}
