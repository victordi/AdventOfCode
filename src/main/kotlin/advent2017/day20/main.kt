package advent2017.day20

import utils.Input.readInput
import utils.extractInts

fun main() {
  println(first())
  println(second())
}

data class Particle(
  val id: Int,
  val position: Triple<Int, Int, Int>,
  val velocity: Triple<Int, Int, Int>,
  val acceleration: Triple<Int, Int, Int>
) {
  fun move(): Particle {
    val newVelocity = Triple(
      velocity.first + acceleration.first,
      velocity.second + acceleration.second,
      velocity.third + acceleration.third
    )
    val newPosition = Triple(
      position.first + newVelocity.first,
      position.second + newVelocity.second,
      position.third + newVelocity.third
    )
    return this.copy(position = newPosition, velocity = newVelocity)
  }

  val distanceFromOrigin: Int
    get() = kotlin.math.abs(position.first) + kotlin.math.abs(position.second) + kotlin.math.abs(position.third)
}

val particles = readInput().mapIndexed { i, line ->
  val nrs = extractInts(line)
  Particle(
    i,
    Triple(nrs[0], nrs[1], nrs[2]),
    Triple(nrs[3], nrs[4], nrs[5]),
    Triple(nrs[6], nrs[7], nrs[8])
  )
}

fun first(): Int = generateSequence(particles) { currentParticles ->
  currentParticles.map { it.move() }
}
  .take(1000)
  .last()
  .minBy { it.distanceFromOrigin }.id

fun second(): Int = generateSequence(particles) { currentParticles ->
  currentParticles
    .map { it.move() }
    .groupBy { it.position }
    .filter { it.value.size == 1 }
    .flatMap { it.value }
}
  .take(1000)
  .last()
  .size
