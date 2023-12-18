package AoC2021.Day25

import utils.Input.withInput
import utils.arrayForEach

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second())
}

fun Array<Array<Char>>.step(): Boolean {
    val n = this.size
    val m = this[0].size

    val rightMoves = mutableListOf<Pair<Int, Int>>()
    this.arrayForEach { (i, j) ->
        if (this[i][j] == '>' && this[i][(j + 1) % m] == '.') {
            rightMoves.add(i to j)
        }
    }
    rightMoves.forEach { (i, j) ->
        this[i][(j + 1) % m] = '>'
        this[i][j] = '.'
    }

    val downMoves = mutableListOf<Pair<Int, Int>>()
    this.arrayForEach { (i, j) ->
        if (this[i][j] == 'v' && this[(i + 1) % n][j] == '.') {
            downMoves.add(i to j)
        }
    }
    downMoves.forEach { (i, j) ->
        this[(i + 1) % n][j] = 'v'
        this[i][j] = '.'
    }

    return rightMoves.isNotEmpty() || downMoves.isNotEmpty()
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        val matrix = Array(list.size)  {list[it].toCharArray().toTypedArray() }
        while(matrix.step()) {
            result++
        }
    }
    return result + 1
}

fun second(): String = "Merry Christmas"