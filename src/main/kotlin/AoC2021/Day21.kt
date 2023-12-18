package AoC2021.Day21

import utils.Input.withInput
import utils.modSkipZero

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun Int.simulateRolls(): Pair<Int, Int> {
    var die = this
    var sump1 = die
    die = die.nextDie()
    sump1 += die
    die = die.nextDie()
    sump1 += die
    die = die.nextDie()
    return die to sump1
}

fun Int.nextDie(): Int = (this + 1).modSkipZero(100)
fun Int.movePlayer(value: Int): Int = (this + value).modSkipZero(10)

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        var p1 = list[0].split(": ")[1].toInt()
        var p2 = list[1].split(": ")[1].toInt()
        var die = 1
        var nrRolls = 0
        var points1 = 0
        var points2 = 0
        while (true) {
            val (newDie, sump1) = die.simulateRolls()
            p1 = p1.movePlayer(sump1)
            points1 += p1
            nrRolls += 3
            if (points1 >= 1000) break

            val (newDie2, sump2) = newDie.simulateRolls()
            die = newDie2
            p2 = p2.movePlayer(sump2)
            points2 += p2
            nrRolls += 3
            if (points2 >= 1000) break
        }

        result = nrRolls * if (points1 >= 1000) points2 else points1
    }
    return result
}

data class State(val p1: Int, val p2: Int, val points1: Int, val points2: Int)

val map = mutableMapOf<State, Pair<Long, Long>>()
fun getWinCount(
    p1: Int,
    p2: Int,
    points1: Int,
    points2: Int
): Pair<Long, Long> {
    if (State(p1, p2, points1, points2) in map) return map[State(p1, p2, points1, points2)]!!
    var (wins1, wins2) = 0L to 0L
    allDieRolls().forEach {
        val newP1 = p1.movePlayer(it)
        val newPoints1 = points1 + newP1
        if (newPoints1 >= 21) wins1++
        else {
            val (newWins1, newWins2) = getWinCount(
                p2, newP1, points2, newPoints1
            )
            wins1 += newWins2
            wins2 += newWins1
        }
    }
    map[State(p1, p2, points1, points2)] = wins1 to wins2
    return wins1 to wins2
}

fun allDieRolls(): List<Int> {
    val result = mutableListOf<Int>()
    for (die1 in 1..3)
        for (die2 in 1..3)
            for (die3 in 1..3)
                result.add(die1 + die2 + die3)
    return result
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val list = input.toList()
        val p1 = list[0].split(": ")[1].toInt()
        val p2 = list[1].split(": ")[1].toInt()

        val (x, y) = getWinCount(p1, p2, 0, 0)
        result = if (x > y) x else y
    }
    return result
}
