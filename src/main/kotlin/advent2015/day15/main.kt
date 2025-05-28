package advent2015.day15

import utils.Input
import utils.println

val lines = Input.readInput()

data class Ingredient(
  val name: String,
  val capacity: Int,
  val durability: Int,
  val flavor: Int,
  val texture: Int,
  val calories: Int
)

val ingredients = lines.map {
  val regex =
    Regex("(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)")
  val (name, capacity, durability, flavor, texture, calories) = regex.find(it)!!.destructured
  Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
}

fun possiblePermutations(nr: Int): List<List<Int>> {
  val permutations = mutableListOf<List<Int>>()

  fun generate(current: List<Int>, remaining: Int) {
    if (current.size == nr) {
      if (remaining == 0) {
        permutations.add(current)
      }
      return
    }

    for (i in 1..remaining) {
      generate(current + i, remaining - i)
    }
  }

  generate(emptyList(), 100)
  return permutations
}

fun Int.customLong(): Long = if (this < 0) 0 else this.toLong()

fun List<Int>.score(ingredients: List<Ingredient>, useCalories: Boolean = false): Long {
  val combinations = zip(ingredients)
  val totalCapacity = combinations.sumOf { (amount, ingredient) -> ingredient.capacity * amount }.customLong()
  val totalDurability = combinations.sumOf { (amount, ingredient) -> ingredient.durability * amount }.customLong()
  val totalFlavor = combinations.sumOf { (amount, ingredient) -> ingredient.flavor * amount }.customLong()
  val totalTexture = combinations.sumOf { (amount, ingredient) -> ingredient.texture * amount }.customLong()
  val totalCalories = combinations.sumOf { (amount, ingredient) -> ingredient.calories * amount }.customLong()
  if (useCalories && totalCalories != 500L) return 0
  return totalCapacity * totalDurability * totalFlavor * totalTexture
}

fun main() {
  println(first())
  println(second())
}

fun first(): Long = possiblePermutations(ingredients.size).maxOf { it.score(ingredients) }
fun second(): Long = possiblePermutations(ingredients.size).maxOf { it.score(ingredients, true) }