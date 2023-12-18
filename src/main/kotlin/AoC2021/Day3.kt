package AoC2021.day3

import utils.Input.withInput
import utils.binaryToDecimal

fun main() = withInput {
    val binaryNumbers = it.toList()
    println("Result of the first part: " + first(binaryNumbers).toString())
    println("Result of the second part: " + second(binaryNumbers).toString())
}

fun first(list: List<String>): Long {
    val gamma = list.getNrZeroes().map { nrZero ->
        if (nrZero > list.size / 2) '0' else '1'
    }.joinToString(separator = "")
    val epsilon = gamma.map { if (it == '0') '1' else '0' }.joinToString(separator = "")
    return gamma.binaryToDecimal() * epsilon.binaryToDecimal()
}

fun second(list: List<String>): Long =
    list.findRating('0', '1') * list.findRating('1', '0')

fun List<String>.findRating(common: Char, uncommon: Char): Long = this[0].indices.fold(this) { acc, idx ->
    if (acc.size == 1) acc
    else {
        val nrZeroes = acc.getNrZeroes()
        acc.filter {
            it[idx] == if (nrZeroes[idx] > acc.size / 2) common else uncommon
        }
    }
}.first().binaryToDecimal()

fun List<String>.getNrZeroes(): List<Int> =
    this.first().indices.map { idx -> this.count { it[idx] == '0' } }