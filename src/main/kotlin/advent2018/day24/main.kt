package advent2018.day24

import utils.Input.readInput
import utils.extractInts
import utils.splitList

fun main() {
  println(first())
  println(second())
}

data class Army(
  val count: Int,
  val hp: Int,
  val immunities: Set<String>,
  val weaknesses: Set<String>,
  val dmg: Int,
  val dmgType: String,
  val initiative: Int,
  val isImmune: Boolean
) {
  val effectivePower: Int
    get() = count * dmg

  fun damageDealtTo(target: Army): Int {
    if (target.immunities.contains(dmgType)) return 0
    val multiplier = if (target.weaknesses.contains(dmgType)) 2 else 1
    return effectivePower * multiplier
  }

  fun takeDamage(damage: Int): Army {
    val unitsKilled = damage / hp
    val newCount = (count - unitsKilled).coerceAtLeast(0)
    return copy(count = newCount)
  }
}

fun String.toUnit(isImmune: Boolean): Army = run {
  val nrs = extractInts(this)
  val brackets = "\\((.+)\\)".toRegex().find(this)?.groupValues?.get(1)
  val parts = brackets?.split(";") ?: emptyList()
  val immunities =
    parts.find { it.contains("immune to") }?.let { it.drop(10).split(",").map { it.trim() } }?.toSet() ?: emptySet()
  val weaknesses =
    parts.find { it.contains("weak to") }?.let { it.drop(8).split(",").map { it.trim() } }?.toSet() ?: emptySet()
  Army(
    count = nrs[0],
    hp = nrs[1],
    immunities = immunities,
    weaknesses = weaknesses,
    dmg = nrs[2],
    dmgType = this.split(" ").reversed()[4],
    initiative = nrs[3],
    isImmune = isImmune
  )
}

val input = readInput().splitList { it.isEmpty() }
val immuneSystem = input[0].drop(1).map { it.toUnit(true) }
val infection = input[1].drop(1).map { it.toUnit(false) }

data class BattleResult(val immuneWon: Boolean, val unitsRemaining: Int)

fun simulate(immune: List<Army>, infect: List<Army>, boost: Int = 0): BattleResult {
  var immuneGroups = immune.withIndex().associate {
    it.index to it.value.copy(dmg = it.value.dmg + boost)
  }.toMutableMap()
  var infectionGroups = infect.withIndex().associate { it.index to it.value }.toMutableMap()

  while (immuneGroups.isNotEmpty() && infectionGroups.isNotEmpty()) {
    val totalUnitsBefore = immuneGroups.values.sumOf { it.count } + infectionGroups.values.sumOf { it.count }
    val allGroups = (immuneGroups.values + infectionGroups.values)
      .sortedWith(compareBy({ -it.effectivePower }, { -it.initiative }))

    val targets = mutableMapOf<Int, Int>()
    val selected = mutableSetOf<Int>()

    for (attacker in allGroups) {
      val enemies = if (attacker.isImmune) infectionGroups.values else immuneGroups.values
      val target = enemies
        .filter { it.initiative !in selected }
        .filter { attacker.damageDealtTo(it) > 0 }
        .maxWithOrNull(
          compareBy(
            { attacker.damageDealtTo(it) },
            { it.effectivePower },
            { it.initiative }
          )
        )

      if (target != null) {
        targets[attacker.initiative] = target.initiative
        selected.add(target.initiative)
      }
    }

    val attackOrder = allGroups.sortedBy { -it.initiative }

    for (attacker in attackOrder) {
      val currentAttacker = if (attacker.isImmune) {
        immuneGroups.values.find { it.initiative == attacker.initiative }
      } else {
        infectionGroups.values.find { it.initiative == attacker.initiative }
      } ?: continue

      if (currentAttacker.count == 0) continue

      val targetInitiative = targets[attacker.initiative] ?: continue

      val currentTarget = if (attacker.isImmune) {
        infectionGroups.values.find { it.initiative == targetInitiative }
      } else {
        immuneGroups.values.find { it.initiative == targetInitiative }
      } ?: continue

      val damage = currentAttacker.damageDealtTo(currentTarget)
      val updated = currentTarget.takeDamage(damage)

      if (currentTarget.isImmune) {
        val key = immuneGroups.entries.find { it.value.initiative == targetInitiative }?.key
        if (key != null) immuneGroups[key] = updated
      } else {
        val key = infectionGroups.entries.find { it.value.initiative == targetInitiative }?.key
        if (key != null) infectionGroups[key] = updated
      }
    }

    immuneGroups = immuneGroups.filterValues { it.count > 0 }.toMutableMap()
    infectionGroups = infectionGroups.filterValues { it.count > 0 }.toMutableMap()

    val totalUnitsAfter = immuneGroups.values.sumOf { it.count } + infectionGroups.values.sumOf { it.count }
    if (totalUnitsAfter == totalUnitsBefore) {
      return BattleResult(immuneWon = false, unitsRemaining = 0)
    }
  }

  val immuneWon = immuneGroups.isNotEmpty()
  val remaining = immuneGroups.values.sumOf { it.count } + infectionGroups.values.sumOf { it.count }
  return BattleResult(immuneWon = immuneWon, unitsRemaining = remaining)
}

fun first(): Int = simulate(immuneSystem, infection).unitsRemaining

fun second(): Int {
  var boost = 25
  while (true) {
    val result = simulate(immuneSystem, infection, boost)
    if (result.immuneWon) {
      return result.unitsRemaining
    }
    boost++
  }
}
