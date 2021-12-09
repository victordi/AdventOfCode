package AoC2021.day4

import Input.withInput

typealias Board = MutableList<MutableList<Int>>

const val MARK = -19732

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput {
        var list = it.toList()
        val order = list.first().split(',').map { it.toInt() }
        list = list.drop(2)

        val matrix = mutableListOf<Board>()

        while (list.isNotEmpty()) {
            matrix.add(list.take(5).parse())
            list = list.drop(6)
        }

        for (nr in order) {
            if (result != 0) break
            for (board in matrix) {
                board.mark(nr)
                if (board.bingo()) {
                    result = nr * board.score()
                    break
                }
            }
        }
    }
    return result
}

fun second(): Int {
    var result = 0
    withInput {
        var list = it.toList()
        val order = list.first().split(',').map { it.toInt() }
        list = list.drop(2)

        val matrix = mutableListOf<Board>()
        val notBingoBoards = mutableListOf<Int>()

        var index = 0
        while (list.isNotEmpty()) {
            matrix.add(list.take(5).parse())
            list = list.drop(6)
            notBingoBoards.add(index++)
        }

        for (nr in order) {
            if (result != 0) break
            for ((idx, board) in matrix.withIndex()) {
                board.mark(nr)
                if (board.bingo()) {
                    notBingoBoards.remove(idx)
                    if (notBingoBoards.isEmpty()) {
                        result = nr * board.score()
                        break
                    }
                }
            }
        }
    }
    return result
}

fun List<String>.parse(): Board {
    return this.fold(mutableListOf()) { acc, str ->
        acc.add(str.split(' ').filter {!it.isEmpty()}.map { it.toInt() }.toMutableList())
        acc
    }
}

fun Board.mark(value: Int) {
    var x = 0
    var y = 0
    var contains = false
    this.forEachIndexed { index, line ->
        if (line.contains(value)) {
            contains = true
            x = index
            y = line.indexOf(value)
        }
    }
    if (contains) {
        this[x][y] = MARK
    }
}

fun Board.bingo(): Boolean {
    // check lines
    this.forEach { line ->
        if (line.fold(true) { acc, i -> acc && i == MARK}) {
            return true
        }
    }
    // check cols
    for (col in 0 until this[1].size) {
        if (this.fold(true) {acc, line -> acc && line[col] == MARK}) {
            return true
        }
    }
    // check main diag
    var diag = 0
    if (this.fold(true) {acc, line -> acc && line[diag++] == MARK}) {
        return true
    }
    // check sec diag
    var secDiag = 4
    return this.fold(true) {acc, line -> acc && line[secDiag--] == MARK}

}

fun Board.score(): Int = this.sumOf { line ->
    line.sumOf { if (it == MARK) 0 else it }
}
