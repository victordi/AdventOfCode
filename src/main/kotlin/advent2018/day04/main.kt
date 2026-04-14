package advent2018.day04

import utils.Input.readInput
import utils.extractInts

enum class Action {
  GUARD, WAKES, SLEEPS
}

data class Record(val month: Int, val day: Int, val hour: Int, val minute: Int, val action: Action, val guardId: Int?)

val records = readInput().map { line ->
  val time = line.substring(1, 17)
  val (_, month, day, hour, minute) = extractInts(time)
  val actionPart = line.substring(19)
  when {
    actionPart.startsWith("Guard") -> Record(month, day, hour, minute, Action.GUARD, extractInts(actionPart).first())
    actionPart.startsWith("wakes") -> Record(month, day, hour, minute, Action.WAKES, null)
    else -> Record(month, day, hour, minute, Action.SLEEPS, null)
  }
}

val sortedRecords = records.sortedWith(compareBy({ it.month }, { it.day }, { it.hour }, { it.minute }))
val guards = mutableMapOf<Int, MutableMap<Int, Int>>()

fun main() {
  records.forEach { it.guardId?.let { id -> guards[id] = mutableMapOf<Int, Int>() } }
  var currentGuard = -1
  var minuteStart = -1
  sortedRecords.forEach { record ->
    when (record.action) {
      Action.GUARD -> currentGuard = record.guardId!!
      Action.SLEEPS -> minuteStart = record.minute
      Action.WAKES -> {
        val minuteEnd = record.minute
        if (minuteStart < minuteEnd) (minuteStart until minuteEnd).forEach { minute ->
          val guardMinutes = guards[currentGuard]!!
          guardMinutes[minute] = guardMinutes.getOrDefault(minute, 0) + 1
        } else {
          (minuteStart until 60).forEach { minute ->
            val guardMinutes = guards[currentGuard]!!
            guardMinutes[minute] = guardMinutes.getOrDefault(minute, 0) + 1
          }
          (0 until minuteEnd).forEach { minute ->
            val guardMinutes = guards[currentGuard]!!
            guardMinutes[minute] = guardMinutes.getOrDefault(minute, 0) + 1
          }
        }
      }
    }
  }
  println(first())
  println(second())
}

fun first(): Int = run {
  val mostAsleep = guards.entries.maxBy { (_, minutes) ->
    minutes.values.sum()
  }
  val commonMinute = mostAsleep.value.maxBy { it.value }.key

  commonMinute * mostAsleep.key
}

fun second(): Int = run {
  val (guardId, minute, _) = guards.filter { it.value.isNotEmpty() }.map { (guard, minutes) ->
    val (minute, frequency) = minutes.maxBy { it.value }
    Triple(guard, minute, frequency)
  }.maxBy { it.third }
  guardId * minute
}
