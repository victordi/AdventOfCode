package advent2024.day09

import utils.Input.withInput

fun main() {
//  println(first())
  println(second())
}

fun first(): Long = withInput { input ->
  val digits = (input.first() + "0").chars().map { it - '0'.toInt() }.toArray().toList()
  var id = 0
  var isFree = false
  val files = mutableListOf<Long>()
  digits.forEach { digit ->
    if (isFree) {
      repeat(digit) {
        files.add(-1)
      }
    } else {
      repeat(digit) {
        files.add(id.toLong())
      }
      id++
    }
    isFree = !isFree
  }
  var reversed = files.reversed().filter { it != -1L }
  val totalFiles = files.count { it != -1L }
  val result = mutableListOf<Long>()
  files.forEach { file ->
    if (file == -1L) {
      result.add(reversed.first())
      reversed = reversed.drop(1)
    } else {
      result.add(file)
    }
  }
  var nr = 0L
  for (i in 0 until totalFiles) {
    nr += i * result[i].toString().toLong()
  }
  nr
}

data class File(val position: Int, val blocks: Int, val id: Long)

fun second(): Long = withInput { input ->
  val digits = (input.first() + "0").chars().map { it - '0'.toInt() }.toArray().toList()
  var idx = 0
  val files = mutableListOf<File>()
  val spaces = mutableListOf<File>()
  val memory = mutableListOf<Long>()
  digits.withIndex().forEach { (i, digit) ->
    if (i % 2 == 1) {
      spaces.add(File(idx, digit, -1))
      repeat(digit) {
        memory.add(-1)
        idx++
      }
    } else {
      val id = (i / 2).toLong()
      files.add(File(idx, digit, id))
      repeat(digit) {
        memory.add(id)
        idx++
      }
    }
  }
  for ((pos, count, fileId) in files.reversed()) {
    spaces
      .find { (spacePos, spaceCount, _) -> spacePos < pos && count <= spaceCount }
      ?.let { space ->
        for (j in 0 until count) {
          memory[pos + j] = -1
          memory[space.position + j] = fileId
        }
        spaces[spaces.indexOf(space)] = File(space.position + count, space.blocks - count, -1)
      }
  }
  memory
    .withIndex()
    .filter { (_, fileId) -> fileId != -1L }
    .sumOf { (i, fileId) -> i.toLong() * fileId }
}
