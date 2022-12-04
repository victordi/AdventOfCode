package advent2022.day2

import Input.withInput

enum class Shape(val score: Int) {
    ROCK(1), PAPER(2), SCISSOR(3)
}

enum class Result(val score: Int) {
    LOSE(0), DRAW(3), WIN(6)
}

fun String.asShape(): Shape =
    if (this == "A" || this == "X") Shape.ROCK
    else if (this == "B" || this == "Y") Shape.PAPER
    else Shape.SCISSOR

fun String.asResult(): Result =
    if (this == "X") Result.LOSE
    else if (this == "Y") Result.DRAW
    else Result.WIN

fun Shape.vs(other: Shape): Int =
    this.score + when(this) {
        Shape.ROCK -> when(other) {
            Shape.ROCK -> Result.DRAW
            Shape.PAPER -> Result.LOSE
            Shape.SCISSOR -> Result.WIN
        }.score
        Shape.PAPER -> when(other) {
            Shape.ROCK -> Result.WIN
            Shape.PAPER -> Result.DRAW
            Shape.SCISSOR -> Result.LOSE
        }.score
        Shape.SCISSOR -> when(other) {
            Shape.ROCK -> Result.LOSE
            Shape.PAPER -> Result.WIN
            Shape.SCISSOR -> Result.DRAW
        }.score
    }

fun Shape.myPlay(result: Result): Shape =
    when(this) {
        Shape.ROCK -> when(result) {
            Result.DRAW -> Shape.ROCK
            Result.LOSE -> Shape.SCISSOR
            Result.WIN -> Shape.PAPER
        }
        Shape.PAPER -> when(result) {
            Result.DRAW -> Shape.PAPER
            Result.LOSE -> Shape.ROCK
            Result.WIN -> Shape.SCISSOR
        }
        Shape.SCISSOR -> when(result) {
            Result.DRAW -> Shape.SCISSOR
            Result.LOSE -> Shape.PAPER
            Result.WIN -> Shape.ROCK
        }
    }

fun main() {
    println(first())
    println(second())
}

fun first(): Int = withInput { input ->
    var score = 0
    input
        .map { it.split(" ").let { it[0].asShape() to it[1].asShape() } }
        .forEach { (opp, me) ->
            score += me.vs(opp)
        }
    score
}

fun second(): Int = withInput { input ->
    var score = 0
    input
        .map { it.split(" ").let { it[0].asShape() to it[1].asResult() } }
        .forEach { (opp, me) ->
            score += opp.myPlay(me).vs(opp)
        }
    score
}