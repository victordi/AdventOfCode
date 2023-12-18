package AoC2021.Day22.kt

import AoC2021.Day19.Point
import utils.Input.withInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        val cubes = mutableSetOf<Point>()

        for (it in list) {
            val ranges = it.split(",")
            val x = ranges[0].split('=')[1].split("..").map {
                val z = it.toInt()
                if (z < -50) -50 else if (z >= 50) 50 else z
            }
            if (x[0] == 50 || x[1] == -50) continue
            val y = ranges[1].split('=')[1].split("..").map {
                val z = it.toInt()
                if (z < -50) -50 else if (z > 50) 50 else z
            }
            if (y[0] == 50 || y[1] == -50) continue
            val z = ranges[2].split('=')[1].split("..").map {
                val z = it.toInt()
                if (z < -50) -50 else if (z > 50) 50 else z
            }
            if (z[0] == 50 || z[1] == -50) continue
            val option = it.split(" ")[0]
            for (i in x[0]..x[1])
                for (j in y[0]..y[1])
                    for (k in z[0]..z[1]) {
                        val point = Point(i, j, k)
                        if (option == "off") cubes.remove(point)
                        else cubes.add(point)
                    }
        }
        result = cubes.size

    }
    return result
}


data class Cube(val xrange: IntRange, val yrange: IntRange, val zrange: IntRange, val option: String) {
    fun nrPoints(): Long = xrange.length() * yrange.length() * zrange.length()

    fun intersect(other: Cube): Cube = Cube(
        xrange.intersect(other.xrange),
        yrange.intersect(other.yrange),
        zrange.intersect(other.zrange),
        option
    )

    fun isEmpty(): Boolean = xrange.isEmpty() || yrange.isEmpty() || zrange.isEmpty()
}

fun String.parse(): Cube {
    val ranges = this.split(",")
    val x = ranges[0].split('=')[1].split("..").map { it.toInt() }
    val y = ranges[1].split('=')[1].split("..").map { it.toInt() }
    val z = ranges[2].split('=')[1].split("..").map { it.toInt() }
    val option = this.split(" ")[0]
    return Cube(
        x[0]..x[1],
        y[0]..y[1],
        z[0]..z[1],
        option
    )
}

fun IntRange.length(): Long = (this.last - this.first + 1).toLong()

fun IntRange.intersect(other: IntRange): IntRange =
    if (this.last < other.first || this.first > other.last) IntRange.EMPTY
    else min(max(this.first, other.first), other.last)..min(max(this.last, other.first), other.last)

fun countOn(cube: Cube, rest: List<Cube>): Long =
    rest
        .map { it.intersect(cube) }
        .filter { !it.isEmpty() }.let { filtered ->
            filtered.foldIndexed(cube.nrPoints()) { index, acc, cube2 ->
                acc - countOn(cube2, filtered.subList(index + 1, filtered.size))
            }
        }

fun second(): Long {
    var result = 0L
    withInput { input ->
        val list = input.map { it.parse() }.toList()

        list.forEachIndexed { index, cube ->
            if (cube.option == "on") {
                result += countOn(cube, list.subList(index + 1, list.size))
            }
        }
    }
    return result
}