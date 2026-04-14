package advent2018.day15

import utils.Input.readInput
import utils.Point
import utils.getAdjacent
import utils.readArrayFromInput
import java.util.*

fun main() {
  println(first())
  println(second())
}

data class Unit(val type: Char, val position: Point, val hp: Int = 200, val attack: Int = 3) {
  val isAlive get() = hp > 0
}

class Combat(
  val grid: Array<Array<Char>>,
  val units: MutableList<Unit>,
  val initialElfCount: Int
) {
  companion object {
    fun create(elfAttack: Int = 3): Combat {
      val grid = readArrayFromInput()
      val units = mutableListOf<Unit>()

      grid.forEachIndexed { row, line ->
        line.forEachIndexed { col, char ->
          if (char in "EG") {
            val attack = if (char == 'E') elfAttack else 3
            units.add(Unit(char, row to col, attack = attack))
            grid[row][col] = '.'
          }
        }
      }

      val elfCount = units.count { it.type == 'E' }
      return Combat(grid, units, elfCount)
    }
  }

  fun Point.adjacent() = listOf(first - 1 to second, first to second - 1, first to second + 1, first + 1 to second)
    .filter { it in grid.indices.flatMap { r -> grid[0].indices.map { c -> r to c } } }

  fun findPath(from: Point, to: Point): List<Point>? {
    if (from == to) return emptyList()

    val queue = LinkedList<Pair<Point, List<Point>>>()
    val visited = mutableSetOf<Point>()
    val occupied = units.filter { it.isAlive }.map { it.position }.toSet()

    queue.add(from to emptyList())
    visited.add(from)

    while (queue.isNotEmpty()) {
      val (pos, path) = queue.poll()

      // Explore in reading order: up, left, right, down
      for (next in pos.adjacent()) {
        if (next == to) return path + next

        if (next !in visited && next !in occupied && grid[next.first][next.second] == '.') {
          visited.add(next)
          queue.add(next to path + next)
        }
      }
    }

    return null
  }

  fun takeTurn(unitIndex: Int, stopOnElfDeath: Boolean = false): Boolean {
    val unit = units[unitIndex]
    val enemies = units.filter { it.isAlive && it.type != unit.type }

    // Check if already adjacent to an enemy
    val adjacentEnemies = enemies.filter { it.position in unit.position.adjacent() }

    // Only move if not already in attack range
    if (adjacentEnemies.isEmpty()) {
      val inRange = enemies.flatMap { enemy -> enemy.position.adjacent().filter { grid[it.first][it.second] == '.' && units.none { u -> u.position == it && u.isAlive } } }.toSet()

      // Find all reachable targets with their paths
      val paths = inRange.mapNotNull { target ->
        findPath(unit.position, target)?.let { path ->
          Triple(target, path.size, path.firstOrNull())
        }
      }.filter { it.third != null }

      if (paths.isNotEmpty()) {
        // Find minimum distance
        val minDist = paths.minOf { it.second }

        // Get all paths with minimum distance
        val shortestPaths = paths.filter { it.second == minDist }

        // Choose first step in reading order among all shortest paths
        val firstStep = shortestPaths
          .map { it.third!! }
          .minWith(compareBy({ it.first }, { it.second }))

        units[unitIndex] = unit.copy(position = firstStep)
      }
    }

    // Attack after potentially moving
    val currentUnit = units[unitIndex]
    val currentAdjacentEnemies = enemies.filter { it.position in currentUnit.position.adjacent() }

    if (currentAdjacentEnemies.isNotEmpty()) {
      val target = currentAdjacentEnemies.minWith(compareBy({ it.hp }, { it.position.first }, { it.position.second }))
      val targetIndex = units.indexOf(target)
      units[targetIndex] = target.copy(hp = target.hp - currentUnit.attack)

      // Check if an elf just died
      if (stopOnElfDeath && target.type == 'E' && !units[targetIndex].isAlive) {
        return false
      }
    }

    return true
  }

  fun simulate(stopOnElfDeath: Boolean = false): Int? {
    var rounds = 0

    while (true) {
      units.sortWith(compareBy({ it.position.first }, { it.position.second }))

      for (i in units.indices) {
        val unit = units[i]
        // Skip dead units
        if (!unit.isAlive) continue

        // Check if combat should end (no enemies at start of turn)
        val enemies = units.filter { it.isAlive && it.type != unit.type }
        if (enemies.isEmpty()) {
          val remainingHp = units.filter { it.isAlive }.sumOf { it.hp }
          return rounds * remainingHp
        }

        // Take the turn - returns false if elf died and we should stop
        if (!takeTurn(i, stopOnElfDeath)) {
          return null
        }
      }

      units.removeAll { !it.isAlive }
      rounds++
    }
  }
}

fun first(): Int = Combat.create().simulate()!!

fun second(): Int {
  // Read input once and cache initial state
  val inputLines = readInput()

  // Binary search for minimum elf attack power where all elves survive
  var low = 4
  var high = 200
  var result = 0

  while (low <= high) {
    val mid = (low + high) / 2
    println("Testing elf attack power: $mid")

    // Create combat with cached input
    val grid = readArrayFromInput()
    val units = mutableListOf<Unit>()
    var elfCount = 0

    grid.forEachIndexed { row, line ->
      line.forEachIndexed { col, char ->
        if (char in "EG") {
          val attack = if (char == 'E') mid else 3
          units.add(Unit(char, row to col, attack = attack))
          grid[row][col] = '.'
          if (char == 'E') elfCount++
        }
      }
    }

    val outcome = Combat(grid, units, elfCount).simulate(stopOnElfDeath = true)

    if (outcome != null) {
      // All elves survived
      println("  -> Success with outcome $outcome")
      result = outcome
      high = mid - 1
    } else {
      // At least one elf died
      println("  -> Failed (elf died)")
      low = mid + 1
    }
  }

  return result
}
