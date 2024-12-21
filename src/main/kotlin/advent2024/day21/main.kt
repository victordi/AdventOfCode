package advent2024.day21

import utils.Grid
import utils.Input.withInput
import utils.Point

fun main() {
  println(first())
  println(second())
}

val nrGrid = Grid.from(listOf("789", "456", "123", "#0A"))
val keyGrid = Grid.from(listOf("#^A", "<v>"))
val input = withInput { it.toList() }

val memoNr: MutableMap<Pair<Char, Char>, List<String>> = mutableMapOf()
val memoKey: MutableMap<Pair<Char, Char>, List<String>> = mutableMapOf()
fun Grid<Char>.getAllPaths(start: Char, end: Char, memo: MutableMap<Pair<Char, Char>, List<String>>): List<String> {
  if (memo.containsKey(start to end)) return memo[start to end]!!
  val startPoint = indexOf(start)
  val endPoint = indexOf(end)
  val allPaths = mutableListOf<String>()
  val currentPath = mutableListOf<Char>()
  val visited = mutableSetOf(this.indexOf('#'))

  fun dfs(current: Point, path: String) {
    if (current == endPoint) {
      allPaths.add(path + "A")
      return
    }

    visited.add(current)
    currentPath.add(get(current))

    for ((next, _) in getAdjacentPoints(current)) {
      if (next !in visited) {
        val newPath = path + when {
          next.first == current.first && next.second == current.second + 1 -> ">"
          next.first == current.first && next.second == current.second - 1 -> "<"
          next.first == current.first + 1 && next.second == current.second -> "v"
          next.first == current.first - 1 && next.second == current.second -> "^"
          else -> ""
        }
        dfs(next, newPath)
      }
    }

    visited.remove(current)
    currentPath.removeAt(currentPath.size - 1)
  }

  dfs(startPoint, "")
  val result = allPaths.filter { it.length == allPaths.minOf { it.length } }
  memo[start to end] = result
  return result
}

val memo: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
fun rec(seq: String, nrRobot: Int): Long {
  if (seq to nrRobot in memo) return memo[seq to nrRobot]!!
  var len = 0L
  var current = 'A'

  seq.forEach { c ->
    val moves = keyGrid.getAllPaths(current, c, memoKey)
    if (nrRobot == 25) len += moves.minOf { it.length }
    else len += moves.minOf { rec(it, nrRobot + 1) }
    current = c
  }

  memo[seq to nrRobot] = len
  return len
}

fun solve(key: String): Int {
  var current = 'A'
  var nrMoves = 0
  key.forEach { c ->
    var robotPaths = nrGrid.getAllPaths(current, c, memoNr)
    repeat(2) {
      val r = robotPaths.flatMap { path ->
        current = 'A'
        var newPaths = listOf("")
        path.forEach { c ->
          newPaths = keyGrid.getAllPaths(current, c, memoKey).flatMap { p -> newPaths.map { it + p } }
          current = c
        }
        newPaths
      }
      robotPaths = r.filter { it.length == r.minOf { it.length } }
    }
    nrMoves += robotPaths.filter { it.length == robotPaths.minOf { it.length } }.first().length
    current = c
  }
  return nrMoves
}

fun solve2(key: String): Long {
  var current = 'A'
  var nrMoves = 0L
  key.forEach { c ->
    var robotPaths = nrGrid.getAllPaths(current, c, memoNr)
    nrMoves += robotPaths.minOf { rec(it, 1) }
    current = c
  }
  return nrMoves
}

fun first(): Long = input.sumOf { keycode ->
  solve(keycode) * keycode.dropLast(1).toLong()
}

fun second(): Long = input.sumOf { keycode ->
  solve2(keycode) * keycode.dropLast(1).toInt()
}
