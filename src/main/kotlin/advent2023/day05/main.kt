package advent2023.day05

import utils.Input.withInput
import utils.splitList

fun main() {
  println(first())
  println(second())
}

fun first(): Long = withInput { input ->
  val list = input.toList()
  val seeds = list[0].split(": ")[1].split(" ").map { it.toLong() }.toMutableList()
  list.drop(2).splitList { it.isEmpty() }.forEach {
    val newMappings = mutableListOf<Pair<Long, Long>>()
    it.drop(1).forEach { map ->
      val (dest, source, range) = map.split(" ").map { it.toLong() }
      seeds.forEach { seed ->
        if (seed in (source..source + range)) {
          newMappings.add(seed to dest + (seed - source))
        }
      }
    }
    newMappings.forEach { (old, new) ->
      seeds.remove(old)
      seeds.add(new)
    }
  }
  seeds.min()
}

fun second(): Long = withInput { input ->
  val list = input.toList()
  val firstLine = list[0].split(": ")[1].split(" ").map { it.toLong() }.toMutableList()
  val seedRanges = firstLine.chunked(2).map { (x, y) -> x to x + y }
  val locations = mutableListOf<Pair<Long, Long>>()
  for (range in seedRanges) {
    var queue = mutableSetOf(range)
    val newRanges = mutableListOf<Pair<Long, Long>>()
    list.drop(2).splitList { it.isEmpty() }.forEach {
      while (queue.isNotEmpty()) {
        val (first, last) = queue.first() // [first, last)
        queue.remove(first to last)
        var cnt = 0
        for (map in it.drop(1)) {
          cnt++
          val (dest, source, size) = map.split(" ").map { it.toLong() } // [dest, dest + size) <= [source, end)
          val end = source + size
          if (last <= source || first > end) {
            continue
          } else if (first < source && last >= end) { // first - source - end - last
            queue.add(first to source)
            newRanges.add(dest to dest + size)
            queue.add(end + 1 to last)
            break
          } else if (first < source) { // first - source - last - end
            queue.add(first to source)
            newRanges.add(dest to dest + (last - source))
            break
          } else if (last >= end) { // source - first - end - last
            newRanges.add(dest + (first - source) to dest + size)
            queue.add(end + 1 to last)
            break
          } else { // source - first - last - end
            newRanges.add(dest + (first - source) to dest + (last - source))
            break
          }
        }
        if (cnt == it.drop(1).size) newRanges.add(first to last)
      }
      queue = newRanges.toMutableSet()
      newRanges.clear()
    }
    locations.addAll(queue)
  }
  locations.minOf { it.first } - 1
}
