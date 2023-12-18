package advent2022.day20

import utils.Input

val input = Input.readInput().map { it.toInt() }
val SIZE = input.size

fun main() {
    println(solve())
    println(solve(811589153L, 10))
}

fun MutableList<Pair<Int, Long>>.move(i: Int) {
    val (idx, value) = withIndex().first { it.value.first == i }
    val newIdx = (idx.toLong() + value.second).mod(SIZE.toLong() - 1L).toInt()
    removeAt(idx)
    add(newIdx, value)
}

fun solve(key: Long = 1L, times: Int = 1): Long = run {
    val indexed = input.withIndex().map { it.index to it.value.toLong() * key }.toMutableList()
    repeat(times) { (0 until SIZE).forEach { indexed.move(it) } }
    val idxZero = indexed.indexOfFirst { it.second == 0L }
    indexed[(idxZero + 1000) % SIZE].second + indexed[(idxZero + 2000) % SIZE].second + indexed[(idxZero + 3000) % SIZE].second
}
