package advent2018.day09

fun main() {
  println(first())
  println(second())
}

const val players = 468
const val lastMarble = 71010

class Node(val value: Int) {
  var next: Node = this
  var prev: Node = this
}

fun solve(players: Int, lastMarble: Int): Long {
  val scores = LongArray(players) { 0L }
  var current = Node(0)

  for (marble in 1..lastMarble) {
    if (marble % 23 == 0) {
      val player = (marble - 1) % players
      repeat(7) { current = current.prev }
      scores[player] += marble.toLong() + current.value

      current.prev.next = current.next
      current.next.prev = current.prev
      current = current.next
    } else {
      current = current.next
      val newNode = Node(marble)
      newNode.next = current.next
      newNode.prev = current
      current.next.prev = newNode
      current.next = newNode
      current = newNode
    }
  }

  return scores.max()
}

fun first(): Long = solve(players, lastMarble)

fun second(): Long = solve(players, lastMarble * 100)
