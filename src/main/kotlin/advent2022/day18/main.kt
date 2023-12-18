package advent2022.day18

import utils.Input

typealias Cube = Triple<Int, Int, Int>

val cubes = Input.readInput().map { line -> line.split(',').map { it.toInt() }.let { Triple(it[0], it[1], it[2]) } }.toSet()

fun main() {
    println(solve())
    println(solve { !it.isSurrounded(cubes) })
}

fun Cube.neighbours(step: Int = 1): List<Cube> = this.let { (x, y, z) ->
    listOf(
        Triple(x, y, z + step),
        Triple(x, y, z - step),
        Triple(x + step, y, z),
        Triple(x - step, y, z),
        Triple(x, y + step, z),
        Triple(x, y - step, z)
    )
}

fun Cube.isSurrounded(cubes: Set<Cube>): Boolean {
    val directions = mutableListOf(false, false, false, false, false, false)
    repeat(20) { neighbours(it).forEachIndexed { idx, cube -> if (cube in cubes) directions[idx] = true } }
    return directions.fold(true) { acc, e -> acc && e }
}

fun solve(part2Condition: (Cube) -> Boolean = { true }): Int =
    cubes.sumOf { it.neighbours().filter { it !in cubes && part2Condition(it) }.size }
 // 2525 < part2 < 2535