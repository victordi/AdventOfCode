package AoC2021.day3

import Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput {
        val list = it.toList()
        val size = list.size
        var nrZeroes = Array(list[0].length) { 0 }
        for (nr in list) {
            nr.reversed().forEachIndexed {index, bit ->
                if (bit == '0') nrZeroes[index]++
            }
        }
        var (gamma, epsilon) = 0 to 0
        var pow2 = 1
        nrZeroes.forEach {
            if (it > size / 2) {
                epsilon += pow2
            } else {
                gamma += pow2
            }
            pow2 *= 2
        }
        result = gamma * epsilon
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput {
        val list = it.toList()

        var oxygenList = list.toList()
        var idx = 0
        while(oxygenList.size != 1) {
            val nrZeroes = oxygenList.getNrZeroes()
            oxygenList =
                if (nrZeroes[idx] > oxygenList.size / 2) {
                    oxygenList.filter { it[idx] == '0' }
                } else {
                    oxygenList.filter { it[idx] == '1' }
                }
            idx++
        }
        result = oxygenList[0].convertBinary()

        var scrubberList = list.toList()
        idx = 0
        while(scrubberList.size != 1) {
            val nrZeroes = scrubberList.getNrZeroes()
            scrubberList =
                if (nrZeroes[idx] > scrubberList.size / 2) {
                    scrubberList.filter { it[idx] == '1' }
                } else {
                    scrubberList.filter { it[idx] == '0' }
                }
            idx++
        }
        result *= scrubberList[0].convertBinary()
    }
    return result
}

fun String.convertBinary(): Int {
    var result = 0
    var pow2 = 1
    this.reversed().forEach {
        if (it == '1') result += pow2
        pow2 *= 2
    }
    return result
}

fun List<String>.getNrZeroes(): Array<Int> {
    var nrZeroes = Array(this[0].length) { 0 }
    for (nr in this) {
        nr.forEachIndexed {index, bit ->
            if (bit == '0') nrZeroes[index]++
        }
    }
    return nrZeroes
}