package AoC2021.Day20

import Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun Set<Pair<Int, Int>>.applyEnchance(initial: String, start:Int, stop: Int): Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()

    for (i in start - 2..stop + 2) {
        for (j in start - 2..stop + 2) {
            var pow2 = 1
            var algo = 0
            (i to j).adjacent().forEach {
                if (it in this) algo += pow2
                pow2 *= 2
            }
            if (initial[algo] == '.') result.add(i to j)
        }
    }

    return result
}

fun Set<Pair<Int, Int>>.applyEnchance2(initial: String, start: Int, stop: Int): Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()

    for (i in start - 2..stop + 2) {
        for (j in start - 2..stop + 2) {
            var pow2 = 1
            var algo = 0
            (i to j).adjacent().forEach {
                if (it !in this) algo += pow2
                pow2 *= 2
            }
            if (initial[algo] == '#') result.add(i to j)
        }
    }

    return result
}

fun Pair<Int, Int>.adjacent(): List<Pair<Int, Int>> {
    val (x, y) = this
    val result = mutableListOf<Pair<Int, Int>>()
    for (x_fact in -1..1) {
        for (y_fact in -1..1) {
            result.add(x + x_fact to y + y_fact)
        }
    }
    return result.reversed()
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        val initial = list.first()
        val darkPixels = mutableSetOf<Pair<Int, Int>>()
        val matrix = list.drop(2)
        for (i in 0 until matrix[0].length) {
            for (j in matrix.indices) {
                if (matrix[i][j] == '#') darkPixels.add(i to j)
            }
        }
        val start = 0
        val stop = matrix[0].length - 1
        val new = darkPixels.applyEnchance(initial, start, stop).applyEnchance2(initial, start - 2, stop + 2)
        result = new.size
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        val initial = list.first()
        var darkPixels = mutableSetOf<Pair<Int, Int>>()
        val matrix = list.drop(2)
        for (i in 0 until matrix[0].length) {
            for (j in matrix.indices) {
                if (matrix[i][j] == '#') darkPixels.add(i to j)
            }
        }
        var start = 0
        var stop = 99
        for (step in 0 until 25) {
            val new = darkPixels.applyEnchance(initial, start, stop).applyEnchance2(initial, start - 2, stop + 2)
            start -= 4
            stop += 4
            darkPixels = new.toMutableSet()
        }
        result = darkPixels.size
    }
    return result
}