package advent2016.day21

import utils.Input.withInput

fun main() {
  println(first())
  println(second())
}

sealed class Operation {
  abstract fun apply(password: String): String
  abstract fun unapply(password: String): String
}
data class SwapPosition(val x: Int, val y: Int) : Operation() {
  override fun apply(password: String): String {
    val chars = password.toMutableList()
    val temp = chars[x]
    chars[x] = chars[y]
    chars[y] = temp
    return chars.joinToString("")
  }
  
  override fun unapply(password: String): String = apply(password)
}

data class SwapLetter(val x: Char, val y: Char) : Operation() {
  override fun apply(password: String): String {
    val chars = password.toMutableList()
    val xIndex = chars.indexOf(x)
    val yIndex = chars.indexOf(y)
    chars[xIndex] = y
    chars[yIndex] = x
    return chars.joinToString("")
  }
  
  override fun unapply(password: String): String = apply(password)
}

data class RotateLeft(val steps: Int) : Operation() {
  override fun apply(password: String): String {
    val effectiveSteps = steps % password.length
    return password.drop(effectiveSteps) + password.take(effectiveSteps)
  }
  
  override fun unapply(password: String): String {
    val effectiveSteps = steps % password.length
    return password.takeLast(effectiveSteps) + password.dropLast(effectiveSteps)
  }
}

data class RotateRight(val steps: Int) : Operation() {
  override fun apply(password: String): String {
    val effectiveSteps = steps % password.length
    return password.takeLast(effectiveSteps) + password.dropLast(effectiveSteps)
  }
  
  override fun unapply(password: String): String {
    val effectiveSteps = steps % password.length
    return password.drop(effectiveSteps) + password.take(effectiveSteps)
  }
}

data class RotateBasedOnPosition(val letter: Char) : Operation() {
  override fun apply(password: String): String {
    val index = password.indexOf(letter)
    val steps = 1 + index + if (index >= 4) 1 else 0
    return password.takeLast(steps) + password.dropLast(steps)
  }
  
  override fun unapply(password: String): String {
    for (originalIndex in 0 until password.length) {
      val steps = 1 + originalIndex + if (originalIndex >= 4) 1 else 0
      val rotated = password.drop(steps % password.length) + password.take(steps % password.length)
      if (rotated.indexOf(letter) == originalIndex) {
        return rotated
      }
    }
    return password
  }
}

data class ReversePositions(val start: Int, val end: Int) : Operation() {
  override fun apply(password: String): String {
    val chars = password.toMutableList()
    val subList = chars.subList(start, end + 1).reversed()
    for (i in start..end) {
      chars[i] = subList[i - start]
    }
    return chars.joinToString("")
  }
  
  override fun unapply(password: String): String = apply(password) // Same operation
}

data class MovePosition(val from: Int, val to: Int) : Operation() {
  override fun apply(password: String): String {
    val chars = password.toMutableList()
    val char = chars.removeAt(from)
    chars.add(to, char)
    return chars.joinToString("")
  }
  
  override fun unapply(password: String): String {
    val chars = password.toMutableList()
    val char = chars.removeAt(to)
    chars.add(from, char)
    return chars.joinToString("")
  }
}


fun String.parseOperation(): Operation {
  val parts = split(" ")
  return when (parts[0]) {
    "swap" -> if (parts[1] == "position") {
      SwapPosition(parts[2].toInt(), parts[5].toInt())
    } else {
      SwapLetter(parts[2][0], parts[5][0])
    }
    "rotate" -> if (parts[1] == "left") {
      RotateLeft(parts[2].toInt())
    } else if (parts[1] == "right") {
      RotateRight(parts[2].toInt())
    } else {
      RotateBasedOnPosition(parts[6][0])
    }
    "reverse" -> ReversePositions(parts[2].toInt(), parts[4].toInt())
    "move" -> MovePosition(parts[2].toInt(), parts[5].toInt())
    else -> throw IllegalArgumentException("Unknown operation: $this")
  }
}

val operations = withInput { input -> input.map { it.parseOperation() }.toList() }

fun first(): String = run {
  val password = "abcdefgh"
  operations.fold(password) { acc, operation ->
    operation.apply(acc)
  }
}

fun second(): String = run {
  val password = "fbgdceah"
  operations.reversed().fold(password) { acc, operation ->
    operation.unapply(acc)
  }
}
