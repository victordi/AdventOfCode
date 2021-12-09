package AoC2021.day8

import Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput {
        val list = it.toList()
        list.forEach { input ->
            val split = input.split('|')
            val right = split[1].split(' ').drop(1)
            result += right.count { it.length in listOf(2, 3, 4, 7) }
        }
    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput {
        val list = it.toList()
        list.forEach { input ->
            val split = input.split('|')
            val left = split[0].split(' ').dropLast(1).map { it.toCharArray().toSet() }
            val right = split[1].split(' ').drop(1).map { it.toCharArray().toSet() }

            val x1 = left.first { it.size == 2 }
            val x4 = left.first { it.size == 4 }
            val x7 = left.first { it.size == 3 }
            val x8 = left.first { it.size == 7 }

            lateinit var x0: Set<Char>
            lateinit var x2: Set<Char>
            lateinit var x3: Set<Char>
            lateinit var x6: Set<Char>
            lateinit var x9: Set<Char>

            var len6 = left.filter { it.size == 6 }
            var len5 = left.filter { it.size == 5 }

            len6.forEach {
                if (it.intersect(x1).size == 1)
                    x6 = it
            }
            len6 = len6.filter { it != x6 }
            if (len6.first().intersect(x4).size == 4) {
                x9 = len6.first()
                x0 = len6.drop(1).first()
            } else {
                x0 = len6.first()
                x9 = len6.drop(1).first()
            }

            val x5 = len5.filter { (x8 - x6).first() !in it }.first()
            len5 = len5.filter { it != x5 }
            if (len5.first().intersect(x1).size == 2) {
                x3 = len5.first()
                x2 = len5.drop(1).first()
            } else {
                x2 = len5.first()
                x3 = len5.drop(1).first()
            }

            val map = listOf(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9)
            result += right.map { map.indexOf(it) }.toNumber()
        }
    }
    return result
}

fun List<Int>.toNumber(): Int =
    this[0] * 1000 + this[1] * 100 + this[2] * 10 + this[3]