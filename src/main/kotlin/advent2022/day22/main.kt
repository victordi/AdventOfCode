package advent2022.day22

import utils.AoCPart
import utils.Input
import utils.Part1
import utils.Part2
import utils.Point
import java.util.regex.Pattern

val lines = Input.readInput()
val m = lines.maxOf { it.length }
val n = lines.size - 2
val map = Array(n) {
    val line = lines[it].toCharArray().toMutableList()
    for (i in line.size until m) line.add(' ')
    line.toTypedArray()
}
val startY = map[0].indexOfFirst { it == '.' }

fun Point.getFace(): Char =
    if (this.first < 50) {
        if (this.second in (50 until 100)) 'A'
        else  'B'
    }
    else if (this.first in (50 until 100)) 'C'
    else if (this.first in (100 until 150)) {
        if (this.second in (50 until 100)) 'E'
        else 'D'
    }
    else 'F'

fun main() {
    println(solve(Part1))
    println(solve(Part2))
}

sealed class Direction {
    abstract fun rotate(dir: Char): Direction
    abstract fun wrapAround(point: Point): Point
    abstract fun wrapAroundP2(point: Point): Pair<Point, Direction>
    abstract fun score(): Int
}

object Up : Direction() {
    override fun rotate(dir: Char): Direction = when (dir) {
        'R' -> Right
        'L' -> Left
        else -> throw IllegalArgumentException("$dir not supported")
    }

    override fun wrapAround(point: Point): Point {
        var x = point.first
        x = Math.floorMod(x - 1, n)
        while(map[x][point.second] == ' ') {
            x = Math.floorMod(x - 1, n)
        }
        return if (map[x][point.second] == '#') point
        else x to point.second
    }

    override fun wrapAroundP2(point: Point): Pair<Point, Direction> = run {
        val (newPoint, direction) = when (point.getFace()) {
            'C', 'E', 'F' -> wrapAround(point) to Up
            'A' -> {
                if (point.first != 0) wrapAround(point) to Up
                else Point(100 + point.second, 0) to Right // wrap to F
            }
            'B' -> {
                if (point.first != 0) wrapAround(point) to Up
                else Point(199, point.second - 100) to Up // wrap to F
            }
            'D' -> {
                if (point.first != 100) wrapAround(point) to Up
                else Point(50 + point.second, 50) to Right // wrap to C
            }
            else -> throw IllegalArgumentException("Wrong face")
        }
        if (map[newPoint.first][newPoint.second] == '#') point to this
        else newPoint to direction
    }

    override fun score(): Int  = 3
}

object Right : Direction() {
    override fun rotate(dir: Char): Direction = when (dir) {
        'R' -> Down
        'L' -> Up
        else -> throw IllegalArgumentException("$dir not supported")
    }

    override fun wrapAround(point: Point): Point {
        var y = point.second
        y = Math.floorMod(y + 1, m)
        while(map[point.first][y] == ' ') {
            y = Math.floorMod(y + 1, m)
        }
        return if (map[point.first][y] == '#') point
        else point.first to y
    }

    override fun score(): Int  = 0

    override fun wrapAroundP2(point: Point): Pair<Point, Direction> = run {
        val (newPoint, direction) =  when (point.getFace()) {
            'A', 'D' -> wrapAround(point) to Right
            'B' -> {
                if (point.second != 149) wrapAround(point) to Right
                else Point(100 + point.first, 99) to Left // wrap to E
            }
            'C' -> {
                if (point.second != 99) wrapAround(point) to Right
                else Point(49, point.first + 50) to Up // wrap to B
            }
            'E' -> {
                if (point.second != 99) wrapAround(point) to Right
                else Point(149 - point.first, 149) to Left // wrap to B
            }
            'F' -> {
                if (point.second != 49) wrapAround(point) to Right
                else Point(149, point.first - 100) to Up // wrap to C
            }
            else -> throw IllegalArgumentException("Wrong face")
        }
        if (map[newPoint.first][newPoint.second] == '#') point to this
        else newPoint to direction
    }
}

object Down : Direction() {
    override fun rotate(dir: Char): Direction = when (dir) {
        'R' -> Left
        'L' -> Right
        else -> throw IllegalArgumentException("$dir not supported")
    }

    override fun wrapAround(point: Point): Point {
        var x = point.first
        x = Math.floorMod(x + 1, n)
        while(map[x][point.second] == ' ') {
            x = Math.floorMod(x + 1, n)
        }
        return if (map[x][point.second] == '#') point
        else x to point.second
    }

    override fun score(): Int  = 1

    override fun wrapAroundP2(point: Point): Pair<Point, Direction> = run {
        val (newPoint, direction) = when (point.getFace()) {
            'A', 'C', 'D' -> wrapAround(point) to Down
            'B' -> {
                if (point.first != 49) wrapAround(point) to Down
                else Point(point.second - 50, 99) to Left // wrap to C
            }
            'E' -> {
                if (point.first != 149) wrapAround(point) to Down
                else Point(point.second + 100, 49) to Left // wrap to F
            }
            'F' -> {
                if (point.first != 199) wrapAround(point) to Down
                else Point(0, point.second + 100) to Down // wrap to C
            }
            else -> throw IllegalArgumentException("Wrong face")
        }
        if (map[newPoint.first][newPoint.second] == '#') point to this
        else newPoint to direction
    }
}

object Left : Direction() {
    override fun rotate(dir: Char): Direction = when (dir) {
        'R' -> Up
        'L' -> Down
        else -> throw IllegalArgumentException("$dir not supported")
    }

    override fun wrapAround(point: Point): Point {
        var y = point.second
        y = Math.floorMod(y - 1, m)
        while(map[point.first][y] == ' ') {
            y = Math.floorMod(y - 1, m)
        }
        return if (map[point.first][y] == '#') point
        else point.first to y
    }

    override fun score(): Int  = 2

    override fun wrapAroundP2(point: Point): Pair<Point, Direction> = run {
        val (newPoint, direction) = when (point.getFace()) {
            'B', 'E' -> wrapAround(point) to Left
            'A' -> {
                if (point.second != 50) wrapAround(point) to Left
                else Point(149 - point.first, 0) to Right // wrap to D
            }
            'C' -> {
                if (point.second != 50) wrapAround(point) to Left
                else Point(100, point.first - 50) to Down // wrap to D
            }
            'D' -> {
                if (point.second != 0) wrapAround(point) to Left
                else Point(149 - point.first, 50) to Right // wrap to A
            }
            'F' -> {
                if (point.second != 0) wrapAround(point) to Left
                else Point(0, point.first - 100) to Down // wrap to A
            }
            else -> throw IllegalArgumentException("Wrong face")
        }
        if (map[newPoint.first][newPoint.second] == '#') point to this
        else newPoint to direction
    }
}

fun solve(part: AoCPart): Int = run {
    var current = 0 to startY
    var direction: Direction = Right
    val steps = lines.last().split(Pattern.compile("R|L")).map { it.toInt() }
    val rotations = lines.last().filter { !it.isDigit() }.toCharArray().toTypedArray().plus('R')
    for (i in steps.indices) {
        repeat(steps[i]) {
            if (part == Part1) current = direction.wrapAround(current)
            if (part == Part2) {
                val result = direction.wrapAroundP2(current)
                current = result.first
                direction = result.second
            }
        }
        direction = direction.rotate(rotations[i])
    }

    (current.first + 1) * 1000 + (current.second + 1) * 4 + direction.rotate('L').score()
}

/* Cube for second part
      AABB
      AABB
      CC
      CC
    DDEE
    DDEE
    FF
    FF
 */
