package advent2015.day14

import utils.Input

val lines = Input.readInput()
val reindeers = lines.map {
  val regex = Regex("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.")
  val (_, speed, flyTime, restTime) = regex.find(it)!!.destructured
  Reindeer(speed.toInt(), flyTime.toInt(), restTime.toInt())
}

fun Reindeer.tick(): Reindeer = run {
  val newDistance = if (isFlying) distance + speed else distance
  val (newIsFlying, newTime) =
    if (isFlying && time + 1 == flyTime) false to 0
    else if (!isFlying && time + 1 == restTime) true to 0
    else isFlying to time + 1
  Reindeer(speed, flyTime, restTime, newDistance, newTime, newIsFlying)
}

data class Reindeer(
  val speed: Int,
  val flyTime: Int,
  val restTime: Int,
  val distance: Int = 0,
  val time: Int = 0,
  val isFlying: Boolean = true
)

fun main() {
  println(first())
  println(second())
}

fun first(): Int = (1..2503)
  .fold(reindeers) { acc, _ -> acc.map { it.tick() } }
  .maxOf { it.distance }

fun second(): Int = run {
  val scores = reindeers.map { 0 }.toMutableList()
  (1..2503)
    .fold(reindeers) { acc, _ ->
      acc.map { it.tick() }.also { newOrder ->
        val lead = newOrder.indexOf(newOrder.maxByOrNull { it.distance })
        scores[lead] += 1
      }
    }
  scores.max()
}