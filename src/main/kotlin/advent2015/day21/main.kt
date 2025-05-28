package advent2015.day21

import utils.Input

val lines = Input.readInput()

data class Stats(val hp: Int, val dmg: Int, val armor: Int)

val boss = Stats(109, 8, 2)

fun Stats.simulate(): Boolean {
  var bosshp = boss.hp
  var playerhp = hp
  while (bosshp > 0 && playerhp > 0) {
    bosshp -= maxOf(1, dmg - boss.armor)
    if (bosshp <= 0) return true
    playerhp -= maxOf(1, boss.dmg - armor)
    if (playerhp <= 0) return false
  }
  return false
}

fun main() {
  println(first())
  println(second())
}

fun first(): Int = run {
  // dmg + armor = 11 -> win + min 4 dmg

  // 6dmg 5armor = 25 + 93 = 118
  // 7dmg 4armor = 40 + 71 = 111 -> cheapest
  // 8dmg 3armor = 65 + 51 = 116

  111
}

fun second(): Int = run {
  // dmg + armor < 11 -> lose | mandatory wep -> min 4 dmg
  // rings most cost inefficient -> 3 dmg + 3 armor + 4 mdg = 7 3 < 11 -> lose
  188
}