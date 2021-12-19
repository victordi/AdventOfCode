package AoC2021.Day19

import Input.withInput
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {

    val time = measureTimeMillis {
        val (part1, part2) = solve()
        println("Result of the first part: $part1")
        println("Result of the second part: $part2")
    }
    println("Ran in $time ms")
}

data class Point(val x: Int, val y: Int, val z: Int) {
    private fun diff(other: Point): Point = Point(
        other.x - x,
        other.y - y,
        other.z - z
    )

    fun manhattan(other: Point) =
        if (other == emptyPoint) 0
        else (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

    fun pointSet(): List<Point> =
        listOf(
            Point(x, y, z),
            Point(-x, y, z),
            Point(-x, -y, z),
            Point(-x, y, -z),
            Point(-x, -y, -z),
            Point(x, -y, z),
            Point(x, -y, -z),
            Point(x, y, -z),

            Point(x, z, y),
            Point(-x, z, y),
            Point(-x, -z, y),
            Point(-x, z, -y),
            Point(-x, -z, -y),
            Point(x, -z, y),
            Point(x, -z, -y),
            Point(x, z, -y),

            Point(y, x, z),
            Point(-y, x, z),
            Point(-y, -x, z),
            Point(-y, x, -z),
            Point(-y, -x, -z),
            Point(y, -x, z),
            Point(y, -x, -z),
            Point(y, x, -z),

            Point(y, z, x),
            Point(-y, z, x),
            Point(-y, -z, x),
            Point(-y, z, -x),
            Point(-y, -z, -x),
            Point(y, -z, x),
            Point(y, -z, -x),
            Point(y, z, -x),

            Point(z, x, y),
            Point(-z, x, y),
            Point(-z, -x, y),
            Point(-z, x, -y),
            Point(-z, -x, -y),
            Point(z, -x, y),
            Point(z, -x, -y),
            Point(z, x, -y),

            Point(z, y, x),
            Point(-z, y, x),
            Point(-z, -y, x),
            Point(-z, y, -x),
            Point(-z, -y, -x),
            Point(z, -y, x),
            Point(z, -y, -x),
            Point(z, y, -x),
        )

    fun difference(other: Point) =
        this.pointSet().map { it.diff(other) }

    fun applySign(signs: List<Int>, other: Point) =
        if (signs.size != 6) emptyPoint
        else Point(
            signs[0] * this.x - signs[1] * other.x,
            signs[2] * this.y - signs[3] * other.y,
            signs[4] * this.z - signs[5] * other.z,
        )
}

val emptyPoint = Point(-555555, -55555555, -555555)

fun getSigns(pointX: Point, pointY: Point, diff: Point): List<Int> {
    val result = mutableListOf<Int>()
    if (pointX.x - diff.x == pointY.x) {
        result.add(1)
        result.add(1)
    } else if (-pointX.x - diff.x == pointY.x) {
        result.add(-1)
        result.add(1)
    } else if (pointX.x + diff.x == pointY.x) {
        result.add(1)
        result.add(-1)
    } else if (-pointX.x + diff.x == pointY.x) {
        result.add(-1)
        result.add(-1)
    }

    if (pointX.y - diff.y == pointY.y) {
        result.add(1)
        result.add(1)
    } else if (-pointX.y - diff.y == pointY.y) {
        result.add(-1)
        result.add(1)
    } else if (pointX.y + diff.y == pointY.y) {
        result.add(1)
        result.add(-1)
    } else if (-pointX.y + diff.y == pointY.y) {
        result.add(-1)
        result.add(-1)
    }

    if (pointX.z - diff.z == pointY.z) {
        result.add(1)
        result.add(1)
    } else if (-pointX.z - diff.z == pointY.z) {
        result.add(-1)
        result.add(1)
    } else if (pointX.z + diff.z == pointY.z) {
        result.add(1)
        result.add(-1)
    } else if (-pointX.z + diff.z == pointY.z) {
        result.add(-1)
        result.add(-1)
    }
    return result
}

fun solve(): Pair<Int, Int> {
    var part2 = 0
    var part1 = 0
    withInput { input ->
        val list = input.toList()
        val points = mutableListOf<Point>()
        val scanners = mutableListOf<List<Point>>()
        for (line in list) {
            if ("scanner" in line) {
                points.clear()
                continue
            }
            if (line.isBlank()) {
                scanners.add(points.toList())
            } else {
                val split = line.split(',').map { it.toInt() }
                points.add(Point(split[0], split[1], split[2]))
            }
        }
        scanners.add(points.toList())

        val leftScanners = scanners.indices.toMutableList()
        leftScanners.remove(0)
        val distances = mutableListOf<Point>()
        while (leftScanners.isNotEmpty()) {
            var diff: Point = emptyPoint
            var destination: Point = emptyPoint
            var source: Point = emptyPoint
            for (idx in leftScanners) {
                val map = mutableMapOf<Point, Int>()
                var rotation = 0
                for (point1 in scanners[idx]) {
                    if (diff != emptyPoint) continue
                    for (point2 in scanners[0]) {
                        if (diff != emptyPoint) continue
                        point1.difference(point2).forEachIndexed { index, point ->
                            map[point] = map.getOrDefault(point, 0) + 1
                            if (map[point]!! == 12) {
                                rotation = index
                                destination = point1.pointSet()[index]
                                source = point2
                                diff = point
                            }
                        }
                    }
                }
                if (diff != emptyPoint) {
                    distances.add(diff)
                    val signs = getSigns(destination, source, diff)
                    val mappedPoints = scanners[idx].map {
                        it.pointSet()[rotation].applySign(signs, diff)
                    }
                    scanners[0] = (scanners[0] + mappedPoints).toSet().toList()
                    leftScanners.remove(idx)
                    break
                }
            }
        }
        part1 = scanners[0].size
        for (i in distances.indices) {
            for (j in distances.indices) {
                if (i == j) continue
                val manhattan = distances[i].manhattan(distances[j])
                if (manhattan > part2) part2 = manhattan
            }
        }
    }
    return part1 to part2
}