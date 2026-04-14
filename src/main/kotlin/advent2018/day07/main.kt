package advent2018.day07

import utils.Input.readInput

fun main() {
  println(first())
  println(second())
}

val edges = readInput().map { line ->
  val parts = line.split(" ")
  parts[1] to parts[7]
}

fun first(): String = run {
  var response = ""
  val nodes = edges.flatMap { listOf(it.first, it.second) }.toMutableSet()
  val incoming = edges.groupBy({ it.second }, { it.first }).withDefault { emptyList() }
  while (nodes.isNotEmpty()) {
    val available = nodes.filter { incoming.getValue(it).all { pred -> pred !in nodes } }.sorted()
    val next = available.first()
    response += next
    nodes.remove(next)
  }
  response
}

fun second(): Int = run {
  val numWorkers = 5
  val baseDuration = 60
  val allNodes = edges.flatMap { listOf(it.first, it.second) }.toSet()
  val incoming = edges.groupBy({ it.second }, { it.first }).withDefault { emptyList() }
  val workers = MutableList(numWorkers) { Pair(0, "") } // (finish time, task)
  var time = 0
  val completed = mutableSetOf<String>()
  val assigned = mutableSetOf<String>()

  while (completed.size < allNodes.size) {
    for (i in workers.indices) {
      if (workers[i].second.isNotEmpty() && workers[i].first <= time) {
        completed.add(workers[i].second)
        workers[i] = Pair(0, "")
      }
    }

    for (i in workers.indices) {
      if (workers[i].second.isEmpty()) {
        val available = allNodes.filter {
          it !in assigned && incoming.getValue(it).all { pred -> pred in completed }
        }.sorted()
        if (available.isNotEmpty()) {
          val next = available.first()
          assigned.add(next)
          val duration = baseDuration + (next[0] - 'A' + 1)
          workers[i] = Pair(time + duration, next)
        }
      }
    }

    val nextEvent = workers.filter { it.second.isNotEmpty() }.minOfOrNull { it.first }
    if (nextEvent != null) {
      time = nextEvent
    }
  }

  time
}
