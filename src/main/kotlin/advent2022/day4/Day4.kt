package advent2022.day4

import AoC2021.Day22.kt.intersect
import Input.withInput
import splitTwo

data class Cleanup(
    val start1: Int,
    val end1: Int,
    val start2: Int,
    val end2: Int
)

fun Cleanup.fullyOverlaps(): Boolean =
    start2 >= start1 && end2 <= end1 ||
        start1 >= start2 && end1 <= end2

fun Cleanup.overlaps(): Boolean = !(start1..end1).intersect(start2..end2).isEmpty()

fun String.asCleanup(): Cleanup =
    this.splitTwo(",").let { (left, right) ->
        val (start1, end1) = left.splitTwo("-") { it.toInt() }
        val (start2, end2) = right.splitTwo("-") { it.toInt() }
        Cleanup(start1, end1, start2, end2)
    }

fun main() {
    println(first())
    println(second())
}

fun first(): Int = withInput { input ->
    input
        .toList()
        .map { it.asCleanup() }
        .filter { it.fullyOverlaps() }
        .size
}

fun second(): Int = withInput { input ->
    input
        .toList()
        .map { it.asCleanup() }
        .filter { it.overlaps() }
        .size
}