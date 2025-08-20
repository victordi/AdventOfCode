package advent2016.day07

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

fun String.splitParts(): Pair<List<String>, List<String>> = run {
  val insideParts = this.split("[", "]")
    .filterIndexed { index, _ -> index % 2 == 1 }
  val outsideParts = this.split("[", "]")
    .filterIndexed { index, _ -> index % 2 == 0 }
  outsideParts to insideParts
}

fun String.isTLS(): Boolean = run {
  val (outsideParts, insideParts) = splitParts()
  val insideABBA = insideParts.any { part ->
    part.windowed(4).any { "${it[0]}${it[1]}" == "${it[3]}${it[2]}" && it[0] != it[1] }
  }
  val outsideABBA = outsideParts.any { part ->
    part.windowed(4).any { "${it[0]}${it[1]}" == "${it[3]}${it[2]}" && it[0] != it[1] }
  }
  outsideABBA && !insideABBA
}

fun String.isSSL(): Boolean = run {
  val (outsideParts, insideParts) = splitParts()
  outsideParts.any { part ->
    part.windowed(3).any {
      val rev = "${it[1]}${it[0]}${it[1]}"
      it[0] == it[2] && it[0] != it[1] && insideParts.any { insidePart ->
        insidePart.contains(rev)
      }
    }
  }
}

fun first(): Int = withInput { input ->
  input.toList().count { it.isTLS() }
}

fun second(): Int = withInput { input ->
  input.toList().count { it.isSSL() }
}
