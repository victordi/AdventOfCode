package AoC2021.Day19

import Input.withInput
import kotlin.math.absoluteValue

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

data class Point(val x: Int, val y: Int, val z: Int) {
    fun diff(other: Point): Point =
        Point(
            other.x - x,
            other.y - y,
            other.z - z
        )

    fun manhattan(other: Point): Int {
        if (other == emptyPoint) return 0
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue
    }

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

            Point(y, x, z),
            Point(-y, x, z),
            Point(-y, -x, z),
            Point(-y, x, -z),
            Point(-y, -x, -z),
            Point(y, -x, z),
            Point(y, -x, -z),
            Point(y, x, -z),

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

    fun difference(other: Point): List<Point> =
        this.pointSet().map {it.diff(other)}

    fun applySign(signs: List<Int>, other: Point): Point {
        if (signs.size != 6) return emptyPoint
        return Point(
            signs[0] * this.x - signs[1] * other.x,
            signs[2] * this.y - signs[3] * other.y,
            signs[4] * this.z - signs[5] * other.z,
        )
    }
}

val emptyPoint = Point(-555555, -55555555, -555555)

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        var scanner = 0
        val points = mutableListOf<Point>()
        val scanners = mutableListOf<List<Point>>()
        for (line in list) {
            if ("scanner" in line) {
                points.clear()
                continue
            }
            if (line.isBlank()) {
                scanners.add(points.toList())
            }
            else {
                val split = line.split(',').map { it.toInt() }
                points.add(Point(split[0], split[1], split[2]))
            }
        }
        scanners.add(points.toList())

        val leftScanners = scanners.indices.toMutableList()
        leftScanners.remove(0)
        var step = 0
        while(leftScanners.size != 0 && step < 1000) {
            step++
//            println(leftScanners)
            for (idx in leftScanners) {
                val map = mutableMapOf<Point, Int>()
                var diff: Point = emptyPoint
                var pointX: Point = emptyPoint
                var pointY: Point = emptyPoint
                var rotation = 0
                for (point1 in scanners[idx]) {
                    for (point2 in scanners[0]) {
                        point1.difference(point2).forEachIndexed { index, point ->
                            map[point] = map.getOrDefault(point, 0) + 1
                            if (map[point]!! == 12) {
                                rotation = index
                                pointX = point1.pointSet()[index]
                                pointY = point2
                                diff = point
                            }
                        }
                    }
                }
                if (diff != emptyPoint) {
                    val signs = getSigns(pointX, pointY, diff)
                    val pointsMappedto0 = scanners[idx].map {
                        it.pointSet()[rotation].applySign(signs, diff)
                    }
                    scanners[0] = (scanners[0] + pointsMappedto0).toSet().toList()
                    leftScanners.remove(idx)
                    break
                }
            }
        }
        result = scanners[0].size
    }
    return result
}

fun getSigns(pointX: Point, pointY: Point, diff: Point) : List<Int> {
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

fun second(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        var scanner = 0
        val points = mutableListOf<Point>()
        val scanners = mutableListOf<List<Point>>()
        for (line in list) {
            if ("scanner" in line) {
                points.clear()
                continue
            }
            if (line.isBlank()) {
                scanners.add(points.toList())
            }
            else {
                val split = line.split(',').map { it.toInt() }
                points.add(Point(split[0], split[1], split[2]))
            }
        }
        scanners.add(points.toList())

        val leftScanners = scanners.indices.toMutableList()
        leftScanners.remove(0)
        var step = 0
        val distancesFrom0 = mutableListOf<Point>()
        while(leftScanners.size != 0 && step < 1000) {
            step++
            println(leftScanners)
            var diff: Point = emptyPoint
            var pointX: Point = emptyPoint
            var pointY: Point = emptyPoint
            for (idx in leftScanners) {
                val map = mutableMapOf<Point, Int>()
                var rotation = 0
                for (point1 in scanners[idx]) {
                    for (point2 in scanners[0]) {
                        point1.difference(point2).forEachIndexed { index, point ->
                            map[point] = map.getOrDefault(point, 0) + 1
                            if (map[point]!! == 12) {
                                rotation = index
                                pointX = point1.pointSet()[index]
                                pointY = point2
                                diff = point
                            }
                        }
                    }
                }
                if (diff != emptyPoint) {
                    distancesFrom0.add(diff)
                    val signs = getSigns(pointX, pointY, diff)
                    val pointsMappedto0 = scanners[idx].map {
                        it.pointSet()[rotation].applySign(signs, diff)
                    }
                    scanners[0] = (scanners[0] + pointsMappedto0).toSet().toList()
                    leftScanners.remove(idx)
                    break
                }
            }
        }
        for (i in distancesFrom0.indices) {
            for (j in distancesFrom0.indices) {
                if (i == j) continue
                val manhattan = distancesFrom0[i].manhattan(distancesFrom0[j])
                if (manhattan > result) result = manhattan
            }
        }
    }
    return result
}