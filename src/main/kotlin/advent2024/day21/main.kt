package advent2024.day21

import utils.*
import utils.Input.withInput

fun main() {
  println(solve(2))
  println(solve(25))
}

val nrGrid = Grid.from(listOf("789", "456", "123", "#0A"))
val keyGrid = Grid.from(listOf("#^A", "<v>"))
val input = withInput { it.toList() }

fun Grid<Char>.shortestPaths(start: Char, end: Char): List<String> {
  val allPaths = mutableListOf<String>()
  val currentPath = mutableListOf<Char>()
  val visited = mutableSetOf(indexOf('#'))

  fun dfs(current: Point, path: String) {
    if (current == indexOf(end)) {
      allPaths.add(path + "A")
      return
    }

    visited.add(current)
    currentPath.add(get(current))
    listOf(Direction.East, Direction.West, Direction.North, Direction.South).forEach { dir ->
      val next = current + dir.diff
      if (next !in visited && (getOrNull(next) ?: '#') != '#') {
        val newPath = path + dir.toChar()
        dfs(next, newPath)
      }
    }

    visited.remove(current)
    currentPath.removeAt(currentPath.size - 1)
  }

  dfs(indexOf(start), "")
  return allPaths.filter { it.length == allPaths.minOf { it.length } }
}

data class State(val seq: String, val nrRobot: Int, val totalRobots: Int)
val memo: MutableMap<State, Long> = mutableMapOf()
fun shortestSequence(seq: String, nrRobot: Int, totalRobots: Int): Long {
  val state = State(seq, nrRobot, totalRobots)
  if (state in memo) return memo[state]!!
  var len = 0L
  var current = 'A'

  seq.forEach { c ->
    val moves = keyGrid.shortestPaths(current, c)
    if (nrRobot == totalRobots) len += moves.minOf { it.length }
    else len += moves.minOf { shortestSequence(it, nrRobot + 1, totalRobots) }
    current = c
  }

  memo[state] = len
  return len
}

fun solve(directionalRobots: Int): Long = input.sumOf { keycode ->
  val minLen = keycode
    .fold('A' to 0L) { (current, len), c ->
      c to len + nrGrid.shortestPaths(current, c).minOf { shortestSequence(it, 1, directionalRobots) }
    }
    .second
  minLen * keycode.dropLast(1).toInt()
}
