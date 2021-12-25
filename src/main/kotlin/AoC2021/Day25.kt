package AoC2021.Day25

import Input.withInput
import myEquals
import myForEach

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second())
}

fun Array<Array<Char>>.step(): Array<Array<Char>> {
    val result = Array(this.size) { i -> Array(this[0].size) { j -> this[i][j]} }

    val n = this.size
    val m = this[0].size

    this.myForEach { (i, j) ->
        if (this[i][j] == '>' && this[i][(j + 1) % m] == '.') {
            result[i][(j + 1) % m] = '>'
            result[i][j] = '.'
        }
    }

    val copy = Array(this.size) { i -> Array(this[0].size) { j -> result[i][j]} }

    copy.myForEach { (i, j) ->
        if (copy[i][j] == 'v' && copy[(i + 1) % n][j] == '.') {
            result[(i + 1) % n][j] = 'v'
            result[i][j] = '.'
        }
    }

    return result
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        var matrix = Array(list.size)  {list[it].toCharArray().toTypedArray() }
        while(true) {
            result++
            val new = matrix.step()
            if (matrix.myEquals(new)) break
            matrix = new
        }
    }
    return result
}

fun second(): String = "Merry Christmas"