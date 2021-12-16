package AoC2021.day16

import Input.withInput

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
        val hexaString = input.first()
        val bitString = hexaString.fold("") {acc, c -> acc + map[c]}

        var index = 0
        while (index < bitString.length) {
            if (index + 6 >= bitString.length) break
            val version = bitString.substring(index, index + 3).toDecimal()
            result += version
            val type =  bitString.substring(index + 3, index + 6).toDecimal()
            index += 6
            if (type == 4L) {
                while(true) {
                    val chunk = bitString.substring(index, index + 5)
                    index += 5
                    if (chunk[0] == '0') break
                }
            } else {
                val lengthType = bitString[index]
                if (lengthType == '0') {
                    index += 16
                } else {
                    index += 12
                }
            }
        }

    }
    return result
}

private fun String.toDecimal(): Long {
    var pow2 = 1L
    var result = 0L
    for (c in this.reversed()) {
        if (c == '1') result += pow2
        pow2 *= 2
    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val hexaString = input.first()
        val bitString = hexaString.fold("") {acc, c -> acc + map[c]}
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
        val subpackets = mutableListOf<Long>()
        val lengthType = this[index]
        if (lengthType == '0') {
            val len = this.substring(index + 1, index + 16).toDecimal()
            index += 16
            while ( index < len + 22) {
                val subpacket = this.substring(index).evaluate()
                subpackets += subpacket.first
                index += subpacket.second
            }
        } else {
            var nrPackets = this.substring(index + 1, index + 12).toDecimal()
            index += 12
            while (nrPackets > 0) {
                val subpacket = this.substring(index).evaluate()
                subpackets += subpacket.first
                index += subpacket.second
                nrPackets--
            }
        }
        val result = when (type) {
            0L -> subpackets.sum()
            1L -> subpackets.fold(1L) {acc , e -> acc * e }
            2L -> subpackets.minOf { it }
            3L -> subpackets.maxOf { it }
            5L -> if (subpackets[0] > subpackets[1]) 1L else 0L
            6L -> if (subpackets[0] < subpackets[1]) 1L else 0L
            7L -> if (subpackets[0] == subpackets[1]) 1L else 0L
            else -> 0
        }
        return result to index
    }
}