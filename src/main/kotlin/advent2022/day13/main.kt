package advent2022.day13

import utils.Input.readInput
import utils.splitList

fun main() {
    println(first())
    println(second())
}

fun String.splitLists(): List<String> {
    val arr = """\[(.*?)]|[0-9]*""".toRegex().findAll(this.drop(1).dropLast(1)).map { it.value }.toList()
    return arr.filter { !it.isBlank() }
}

fun checkOrder(first: String, second: String): Int {
    val firstAsInt = first.toIntOrNull()
    val secondAsInt = second.toIntOrNull()
    if (firstAsInt != null && secondAsInt != null) {
        if (firstAsInt < secondAsInt) return 1
        if (firstAsInt > secondAsInt) return -1
        return 0
    }

    if (firstAsInt != null) return checkOrder("[$firstAsInt]", second)
    if (secondAsInt != null) return checkOrder(first , "[$secondAsInt]")

    val splitFirst = first.splitLists()
    val splitSecond = second.splitLists()
    val minIdx = minOf(splitFirst.size, splitSecond.size)
    for (i in 0 until minIdx) {
        val result = checkOrder(splitFirst[i], splitSecond[i])
        if (result != 0) return result
    }

    if (splitFirst.size == splitSecond.size) return 0
    return if (minIdx == splitFirst.size) 1 else -1
}

fun first(): Int = run {
    val input = readInput().splitList { it.isBlank() }
    val indexes = mutableListOf<Int>()
    input.forEachIndexed { idx, it ->
        if (checkOrder(it[0], it[1]) == 1) {
            indexes.add(idx + 1)
        }
    }
    indexes.sum()
}

fun second(): Int = run {
    val input = readInput().filter { it.isNotBlank() }.toMutableList()
    input.add("[[2]]")
    input.add("[[6]]")
    input.sortWith { o1, o2 -> -1 * checkOrder(o1, o2) }
    (input.indexOf("[[2]]") + 1) * (input.indexOf("[[6]]") + 1)
}

