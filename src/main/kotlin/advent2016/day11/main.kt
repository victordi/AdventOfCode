package advent2016.day11

import arrow.core.Tuple4
import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

data class Item(
  val name: String,
  val isGenerator: Boolean
)

data class State(
  val floors: Tuple4<Set<Item>, Set<Item>, Set<Item>, Set<Item>>,
  val elevator: Int = 0,
  val steps: Int = 0
) {
  fun isFinal(): Boolean = floors.let { (f1, f2, f3, _) ->
    f1.isEmpty() && f2.isEmpty() && f3.isEmpty() && elevator == 3
  }
}

fun Set<Item>.goesBoom(): Boolean = this.any { item ->
  this.any { other ->
    item.isGenerator != other.isGenerator && item.name == other.name
  }
}

fun first(): Int = withInput { input ->
  input.toList().size
}

fun second(): Int = withInput { input ->
  input.toList().size
}
