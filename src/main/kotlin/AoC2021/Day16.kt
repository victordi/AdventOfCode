package AoC2021.day16

import Input.withInput
import toDecimal

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

val map = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)

fun first(): Long {
    var result = 0L
    withInput { input ->
        val bitString = input.first().fold("") {acc, c -> acc + map[c]}

        var index = 0
        while (index < bitString.length) {
            if (index + 6 >= bitString.length) break
            result += bitString.substring(index, index + 3).toDecimal()
            val type =  bitString.substring(index + 3, index + 6).toDecimal()
            index += 6
            if (type == 4L) {
                while(bitString.substring(index, index + 5)[0] == '1') {
                    index += 5
                }
                index += 5
            } else {
                index += if (bitString[index] == '0') 16 else 12
            }
        }

    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val bitString = input.first().fold("") {acc, c -> acc + map[c]}
        result = bitString.evaluate().first
    }
    return result
}

fun String.evaluate(): Pair<Long, Int> {
    var index = 0
    val type =  this.substring(index + 3, index + 6).toDecimal()
    index += 6
    if (type == 4L) {
        var bitValue = ""
        while(true) {
            val chunk = this.substring(index, index + 5)
            bitValue += chunk.drop(1)
            index += 5
            if (chunk[0] == '0') break
        }
        return bitValue.toDecimal() to index
    } else {
        val packetValues = mutableListOf<Long>()
        if (this[index] == '0') {
            val len = this.substring(index + 1, index + 16).toDecimal() + index
            index += 16
            while (index < len + 6) {
                val (packetValue, packetSize) = this.substring(index).evaluate()
                packetValues += packetValue
                index += packetSize
            }
        } else {
            var nrPackets = this.substring(index + 1, index + 12).toDecimal()
            index += 12
            while(--nrPackets >= 0) {
                val (packetValue, packetSize) = this.substring(index).evaluate()
                packetValues += packetValue
                index += packetSize
            }
        }
        val result = when (type) {
            0L -> packetValues.sum()
            1L -> packetValues.fold(1L) { acc, e -> acc * e }
            2L -> packetValues.minOf { it }
            3L -> packetValues.maxOf { it }
            5L -> if (packetValues[0] > packetValues[1]) 1L else 0L
            6L -> if (packetValues[0] < packetValues[1]) 1L else 0L
            7L -> if (packetValues[0] == packetValues[1]) 1L else 0L
            else -> TODO("Should never get to this, type is always in 0..7 since it only contains 3 bits")
        }
        return result to index
    }
}