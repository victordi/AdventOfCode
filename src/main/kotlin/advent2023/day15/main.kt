package advent2023.day15

import utils.Input.readInput
import advent2023.day15.Sign.Equals
import advent2023.day15.Sign.Minus

fun main() {
  println(first())
  println(second())
}

val words = readInput().first().split(",")

fun first(): Int = words.sumOf {it.getHash() }

fun String.getHash(): Int = fold(0) { acc, c -> ((acc + c.code) * 17 ) % 256 }

enum class Sign {
  Equals, Minus
}

data class Entry(val label: String, val sign: Sign, val lens: Int) {
  companion object {
    fun from(value: String): Entry =
      if (value.contains("=")) {
        val (s, i) = value.split("=")
        Entry(s, Equals, i.toInt())
      } else if (value.contains("-")) {
        val (s, _) = value.split("-")
        Entry(s, Minus, 0)
      } else throw IllegalArgumentException("Invalid entry no = or -")
  }
}

fun second(): Int = run {
  val boxes = (0..255).map { mutableListOf<Entry>() }
  words.map { Entry.from(it) }.forEach { (label, sign, lens) ->
    val hash = label.getHash()
    when(sign) {
      Equals -> {
        val inBox = boxes[hash].any { it.label == label }
        if (inBox) {
          boxes[hash].replaceAll {
            if (it.label == label) it.copy(lens = lens)
            else it
          }
        } else boxes[hash].add(Entry(label, sign, lens))
      }
      Minus -> {
        boxes[hash].removeIf { it.label == label }
      }
    }
  }

  boxes.foldIndexed(0) { boxNr, acc, entries ->
    acc + entries.foldIndexed(0) { entryNr, acc2, entry ->
      acc2 + (1 + boxNr) * (1 + entryNr) * entry.lens
    }
  }
}
