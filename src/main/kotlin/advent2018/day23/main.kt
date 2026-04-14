package advent2018.day23

import utils.Input.readInput
import utils.extractLongs

fun main() {
  println(first())
  println(second())
}

data class Nanobot(val x: Long, val y: Long, val z: Long, val r: Long)

fun String.toNano(): Nanobot = run {
  val longs = extractLongs(this)
  Nanobot(longs[0], longs[1], longs[2], longs[3])
}

val bots = readInput().map { it.toNano() }

fun Nanobot.contains(other: Nanobot): Boolean = run {
  val manhattan = kotlin.math.abs(x - other.x) + kotlin.math.abs(y - other.y) + kotlin.math.abs(z - other.z)
  manhattan <= r
}

fun first(): Int = run {
  val strongest = bots.maxByOrNull { it.r } ?: error("No bots")
  bots.count { strongest.contains(it) }
}

data class Box(val x: Long, val y: Long, val z: Long, val size: Long)

fun Box.distanceToOrigin(): Long {
  val dx = maxOf(0L, x, -x - size)
  val dy = maxOf(0L, y, -y - size)
  val dz = maxOf(0L, z, -z - size)
  return dx + dy + dz
}

fun Box.inRangeCount(bots: List<Nanobot>): Int = bots.count { bot ->
  val dx = maxOf(0L, x - bot.x, bot.x - x - size)
  val dy = maxOf(0L, y - bot.y, bot.y - y - size)
  val dz = maxOf(0L, z - bot.z, bot.z - z - size)
  val distance = dx + dy + dz
  distance <= bot.r
}

fun second(): Long {
  val minX = bots.minOf { it.x - it.r }
  val maxX = bots.maxOf { it.x + it.r }
  val minY = bots.minOf { it.y - it.r }
  val maxY = bots.maxOf { it.y + it.r }
  val minZ = bots.minOf { it.z - it.r }
  val maxZ = bots.maxOf { it.z + it.r }

  var size = 1L
  val maxRange = maxOf(maxX - minX, maxY - minY, maxZ - minZ)
  while (size < maxRange) {
    size *= 2
  }

  val queue = java.util.PriorityQueue<Pair<Box, Int>>(compareBy(
    { -it.second },
    { it.first.distanceToOrigin() },
    { it.first.size }
  ))

  val start = Box(minX, minY, minZ, size)
  queue.add(start to start.inRangeCount(bots))

  var bestCount = 0
  var bestDist = Long.MAX_VALUE

  while (queue.isNotEmpty()) {
    val (box, count) = queue.poll()

    if (count < bestCount) continue

    if (box.size == 0L) {
      val dist = kotlin.math.abs(box.x) + kotlin.math.abs(box.y) + kotlin.math.abs(box.z)
      if (count > bestCount || (dist < bestDist)) {
        bestCount = count
        bestDist = dist
      }
      continue
    }

    val newSize = box.size / 2
    for (dx in 0L..1L) {
      for (dy in 0L..1L) {
        for (dz in 0L..1L) {
          val newBox = if (newSize == 0L) {
            Box(box.x + dx, box.y + dy, box.z + dz, 0L)
          } else {
            Box(
              box.x + dx * newSize,
              box.y + dy * newSize,
              box.z + dz * newSize,
              newSize
            )
          }

          val newCount = if (newBox.size == 0L) {
            bots.count { bot ->
              val dist = kotlin.math.abs(newBox.x - bot.x) +
                         kotlin.math.abs(newBox.y - bot.y) +
                         kotlin.math.abs(newBox.z - bot.z)
              dist <= bot.r
            }
          } else {
            newBox.inRangeCount(bots)
          }

          queue.add(newBox to newCount)
        }
      }
    }
  }

  return bestDist
}
