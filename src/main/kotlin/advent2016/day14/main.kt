package advent2016.day14

import advent2016.day05.md5
import utils.println

fun main() {
  println(solve(0))
  println(solve(2016))
}

fun solve(repeats: Int): Int = run {
  val salt = "qzyelonm"
  var index = -1
  val cache = mutableMapOf<Int, String>()
  var count = 0
  var response = 0
  while (count < 64) {
    index++
    cache.remove(index - 1)
    val key = salt + index
    val hash = cache.getOrPut(index) { key.stretchedMd5(repeats) }
    val tripleChar = hash.windowed(3).find { it[0] == it[1] && it[1] == it[2] } ?: continue
    val char = tripleChar[0]
    for (i in 1..1000) {
      val nextKey = salt + (index + i)
      val nextHash = cache.getOrPut(index + i) { nextKey.stretchedMd5(repeats) }
      if (nextHash.contains(char.toString().repeat(5))) {
        count++
        count.println
        response = index
        break
      }
    }
  }
  response
}

fun String.stretchedMd5(repeats: Int): String {
  var hash = md5()
  repeat(repeats) {
    hash = hash.md5()
  }
  return hash
}
