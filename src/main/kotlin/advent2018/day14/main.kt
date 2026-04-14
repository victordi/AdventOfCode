package advent2018.day14

fun main() {
  println(first())
  println(second())
}

const val nrRecipes = 554401

data class State(
  val recipes: MutableList<Int>,
  var elf1: Int,
  var elf2: Int
)

val initialState = State(mutableListOf(3, 7), 0, 1)

fun State.step() {
  val sum = recipes[elf1] + recipes[elf2]
  if (sum >= 10) {
    recipes.add(sum / 10)
  }
  recipes.add(sum % 10)
  elf1 = (elf1 + 1 + recipes[elf1]) % recipes.size
  elf2 = (elf2 + 1 + recipes[elf2]) % recipes.size
}

fun first(): String = run {
  val state = initialState
  val numSteps = nrRecipes
  while (state.recipes.size < numSteps + 10) {
    state.step()
  }
  state.recipes.drop(numSteps).take(10).joinToString("")
}

fun second(): Int = run {
  val state = initialState
  val pattern = nrRecipes.toString()
  while (pattern !in state.recipes.takeLast(pattern.length + 1).joinToString("")) {
    state.step()
  }
  state.recipes.joinToString("").indexOf(pattern)
}
