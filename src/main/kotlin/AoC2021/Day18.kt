package AoC2021.Day18

import utils.Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun String.myReduce(): String {
    var result = ""

    var depth = 0
    var leftMost = -1
    var rightAdd = -1
    var i = 0
    while (i < this.length) {
        val c = this[i]
        when(c) {
            '[' -> {
                result += c
                depth++
            }
            ']' -> {
                result += c
                depth--
            }
            ',' -> result += c
            ' ' -> result += c
            else -> {
                val numbers = this.substring(i).split(']')[0]
                val split = numbers.split(',')
                val a = split[0].toInt()
                if (split.size == 1 || split[1].contains('[')) {
                    if (a > 9){
                        val newPair = if (a % 2 == 0) "[${a / 2},${a / 2}]" else "[${a / 2},${a / 2 + 1}]"
                        return "$result$newPair${this.substring(i + 2)}"
                    }
                    result += a
                    leftMost = i
                    i++
                    depth--
                    continue
                }
                if (split[1][0] == '[') {
                    result += a
                    leftMost = i
                    i++
                    continue
                }
                val b = split[1].toInt()
                i += numbers.length
                depth--
                val x = this.substring(0, i).count { it == '[' } - this.substring(0, i).count { it == ']' } - 1
                if (x >= 4) {
                    if (leftMost != -1) {
                        val rightPart = this.substring(i).takeWhile { it in listOf(',',' ', '[', ']') }
                        if (i + rightPart.length >= this.length) {
                            var prev = this[leftMost].digitToInt()
                            var toDrop = 1
                            if (this[leftMost + 1].isDigit()) {
                                prev = prev * 10 + this[leftMost + 1].digitToInt()
                                toDrop = 2
                            }
                            return "${this.substring(0, leftMost)}${prev + a}" +
                                    this.substring(leftMost + toDrop, i - numbers.length).dropLast(1) +
                                    "0${this.substring(i + 1)}"
                        }
                        var prev = this[leftMost].digitToInt()
                        var toDrop = 1
                        if (this[leftMost + 1].isDigit()) {
                            prev = prev * 10 + this[leftMost + 1].digitToInt()
                            toDrop = 2
                        }
                        val between = this.substring(leftMost + toDrop, i - numbers.length).dropLast(1)

                        var rightNumber = this[i + rightPart.length].digitToInt()
                        if (this[i + rightPart.length + 1].isDigit()) {
                            i++
                            rightNumber = 10 * rightNumber + this[i + rightPart.length].digitToInt()
                        }
                        return "${this.substring(0, leftMost)}${prev + a}" +
                                between +
                                "0${rightPart.drop(1)}${b + rightNumber}${this.substring(i + 1 + rightPart.length)}"
                    } else {
                        val rightPart = this.substring(i).takeWhile { it in listOf(',',' ', '[', ']') }
                        var rightNumber = this[i + rightPart.length].digitToInt()
                        if (this[i + rightPart.length + 1].isDigit()) {
                            i++
                            rightNumber = 10 * rightNumber + this[i + rightPart.length].digitToInt()
                        }
                        return "${this.substring(0, i - numbers.length).dropLast(1)}0${rightPart.drop(1)}${b + rightNumber}${this.substring(i + 1 + rightPart.length)}"
                    }
                }
                if (a > 9){
                    val newPair = if (a % 2 == 0) "[${a / 2},${a / 2}]" else "[${a / 2},${a / 2 + 1}]"
                    return "$result$newPair,$b${this.substring(i)}"
                }
                if (b > 9) {
                    val newPair = if (b % 2 == 0) "[${b / 2},${b / 2}]" else "[${b / 2},${b / 2 + 1}]"
                    return "$result$a,$newPair${this.substring(i)}"
                }
                result += "$numbers]"
                leftMost = i - numbers.length + 2
            }
        }

        i++
    }

    return result
}

fun String.magnitude(): Int {
    if (this.first() != '[') {
        return this.toInt()
    }
    var idx = 0
    var depth = 0
    while(idx < this.length) {
        val c = this[idx]
        if (c == '[') depth++
        if (c == ']') depth--
        if (c == ',' && depth == 1) {
            val left = this.substring(1, idx)
            val right = this.substring(idx + 1, this.length - 1)
            return 3 * left.magnitude() + 2 * right.magnitude()
        }
        idx++
    }
    return 0
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList().map { it.filter { it != ' ' } }
        var current = list.first()
        while (true) {
            val new = current.myReduce()
            if (new == current) break
            current = new
        }
        for (nr in list.drop(1)) {
            current = "[$current,$nr]"
            while (true) {
                val new = current.myReduce()
                if (new == current) break
                current = new
            }
        }
        result = current.magnitude()
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList().map { it.filter { it != ' ' } }
        for (i in list.indices) {
            val snail1 = list[i]
            for (j in list.indices) {
                if (i == j) continue
                val snail2 = list[j]
                var sum = "[$snail1,$snail2]"
                while (true) {
                    val new = sum.myReduce()
                    if (new == sum) break
                    sum = new
                }
                val magnitude = sum.magnitude()
                if (magnitude > result) result = magnitude
            }
        }
    }
    return result
}