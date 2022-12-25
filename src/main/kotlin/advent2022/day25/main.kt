package advent2022.day25

import Input

fun main() {
    val sum = first()
    println(sum)
    println(sum.getSNAFU())
}

fun first(): Long = Input
    .readInput()
    .map { it.reversed() }
    .map {
        var pow5 = 1L
        var result = 0L
        it.forEach {
            if (it.isDigit()) result += it.digitToInt() * pow5
            else if (it == '-') result -= pow5
            else if (it == '=') result -= 2 * pow5
            pow5 *= 5
        }
        result
    }
    .sum()

fun Long.getSNAFU(): String {
    var digits = ""
    var number = this
    while (number > 0) {
        var carry = 0
        when (val rem = (number % 5).toInt()) {
            0, 1, 2 -> digits += rem
            3 -> digits += '='.also { carry = 1 }
            4 -> digits += '-'.also { carry = 1 }
        }
        number = number / 5 + carry
    }
    return digits.reversed()
}
