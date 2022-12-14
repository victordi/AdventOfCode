package AoC2021.Day17

import Input.withInput

var xLow = 0
var xHigh = 0
var yLow = 0
var yHigh = 0
lateinit var xArea: IntRange
lateinit var yArea: IntRange
val startingPositions = mutableSetOf<Pair<Int, Int>>()

fun main() {
    parseInput()
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun parseInput() {
    withInput { input ->
        val line = input.first()
        val xs = line.split("x=")[1].split(',')[0].split("..").map { it.toInt() }
        xLow = xs[0]
        xHigh = xs[1]
        val ys = line.split("y=")[1].split("..").map { it.toInt() }
        yLow = ys[0]
        yHigh = ys[1]
        xArea = xLow..xHigh
        yArea = yLow..yHigh
    }
}

fun pathOf(velX: Int, velY: Int): Int {
    var (x, y) = 0 to 0
    var maxY = y
    var velocityX = velX
    var velocityY = velY
    while(true) {
        x += velocityX
        y += velocityY
        if (y > maxY) maxY = y
        velocityX += if (velocityX > 0) -1 else if (velocityX < 0) 1 else 0
        velocityY -= 1

        if (x in xArea && y in yArea) return maxY
        if (y < yArea.first) break
    }
    return -1
}

fun first(): Int {
    var result = 0
    for (y in 500 downTo yLow) {
        for (x in 0..xHigh) {
            val maxY = pathOf(x, y)
            if (maxY != -1) {
                if (maxY > result)
                    result = maxY
                startingPositions.add(x to y)
            }
        }
    }
    return result
}

fun second(): Int = startingPositions.size