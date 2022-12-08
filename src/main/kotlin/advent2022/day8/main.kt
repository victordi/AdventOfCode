package advent2022.day8

import Input.readInput
import arrayForEach
import kotlin.math.max

var n: Int = 0

fun main() {
    println(first())
    println(second())
}

fun parseInput(): Array<Array<Int>> = with(readInput()) {
    n = this[0].length
    Array(n) { this[it].chunked(1).map { it.toInt() }.toTypedArray() }
}

fun first(): Int = run {
    val trees = parseInput()
    var visible = 0
    trees.arrayForEach { (i, j) -> if (trees.isVisible(i, j)) visible++ }
    return visible
}

fun second(): Int = run {
    val trees = parseInput()
    val scores = mutableListOf<Int>()
    trees.arrayForEach { (i, j) -> scores.add(trees.score(i, j)) }
    return scores.max()
}

fun Array<Array<Int>>.isVisible(i: Int, j: Int): Boolean {
    if (i == 0 || i == n - 1 || j == 0 || j == n - 1) return true
    val current = this[i][j]
    val visible = (0 until i).map { this[it][j] }.any { it >= current } &&
            (i + 1 until n).map { this[it][j] }.any { it >= current } &&
            (0 until j).map { this[i][it] }.any { it >= current } &&
            (j + 1 until n).map { this[i][it] }.any { it >= current }
    return !visible
}

fun Array<Array<Int>>.nrVisible(i: Int, j: Int, range: IntProgression, col: Int): Int {
    var score = 0
    for (idx in range) {
        if (col == 0 && this[idx][j] < this[i][j]) score++
        else if (col == 1 && this[i][idx] < this[i][j]) score++
        else {
            score++
            break
        }
    }
    return score
}

fun Array<Array<Int>>.score(i: Int, j: Int): Int =
    nrVisible(i, j, (i - 1 downTo 0), 0) * nrVisible(i, j, (i + 1 until n), 0) *
    nrVisible(i, j, (j - 1 downTo 0), 1) * nrVisible(i, j, (j + 1 until n), 1)
