package advent2023.day22

import utils.Input.readInput

fun main() {
  val initial = bricks.solve()
  val numberOfFallenBricks = bricks.keys.map { brickId ->
    val withoutBrick = bricks.toMutableMap().apply { remove(brickId) }.toMap().solve()
    withoutBrick.entries.filter { (id, positions) ->
      initial[id]!! != positions
    }.size
  }
  println("Part 1: ${numberOfFallenBricks.filter { it == 0 }.size}")
  println("Part 2: ${numberOfFallenBricks.sum()}")
}

typealias Brick = Triple<Int, Int, Int>

val bricks: Map<Int, List<Brick>> = readInput()
  .mapIndexed { i, line ->
    val (l, r) = line.split('~')
    val (x1, y1, z1) = l.split(',').map { it.toInt() }
    val (x2, y2, z2) = r.split(',').map { it.toInt() }
    val points =
      if (x1 != x2) (x1..x2).map { Triple(it, y1, z1) }
      else if (y1 != y2) (y1..y2).map { Triple(x1, it, z1) }
      else (z1..z2).map { Triple(x1, y1, it) }
    i to points
  }
  .toMap()

fun Map<Int, List<Brick>>.solve(): Map<Int, List<Brick>> {
  val orderedBricks = mutableMapOf<Int, List<Brick>>()
  val bricksByHeight = mutableMapOf<Int, List<Pair<Int, Int>>>()

  entries.sortedBy { it.value.minOf { brick -> brick.third } }.forEach { (brickId, bricks) ->
    val withoutZ = bricks.map { it.first to it.second }
    val newZ = bricksByHeight.entries.sortedByDescending { it.key }.firstOrNull { (_, placed) ->
      placed.any { withoutZ.contains(it) }
    }?.key?.let { it + 1 } ?: 1

    if (withoutZ.toSet().size != 1) {
      bricksByHeight[newZ] = bricksByHeight.getOrDefault(newZ, emptyList()) + withoutZ
      orderedBricks[brickId] = bricks.map { it.copy(third = newZ) }
    }
    else {
      List(bricks.size) { index ->
        bricksByHeight[newZ + index] = bricksByHeight.getOrDefault(newZ + index, emptyList()) + withoutZ
      }
      orderedBricks[brickId] = bricks.mapIndexed { index, triple -> triple.copy(third = newZ + index) }
    }
  }

  return orderedBricks
}
