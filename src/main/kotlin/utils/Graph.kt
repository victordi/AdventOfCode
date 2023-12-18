package utils

import java.util.*

/* Graph creation example:
 *  val graph: Graph<Int> = newGraph(9)
 *  graph.addEdgeBi(0, 1, 4)
 *  graph.addEdgeBi(0, 7, 8)                          8       7
 *  graph.addEdgeBi(1, 2, 8)                      1 - - - 2 - - - 3
 *  graph.addEdgeBi(1, 7, 11)                    /|       |\      |\
 *  graph.addEdgeBi(2, 3, 7)                   4/ |       |2\     | \9
 *  graph.addEdgeBi(2, 8, 2)                   /  |       |  \    |  \
 *  graph.addEdgeBi(2, 5, 4)                 0    |11     8   \4  |   4
 *  graph.addEdgeBi(3, 4, 9)                   \  |     / |    \  |  /
 *  graph.addEdgeBi(3, 5, 14)                  8\ |   /   |6    \ | /10
 *  graph.addEdgeBi(4, 5, 10)                    \| /     |      \|/
 *  graph.addEdgeBi(5, 6, 2)                      7 - - - 6 - - - 5
 *  graph.addEdgeBi(6, 7, 1)                          1       2
 *  graph.addEdgeBi(6, 8, 6)
 *  graph.addEdgeBi(7, 8, 7*
 */

typealias Graph<Int> = Array<Array<Int>>

fun newGraph(size: Int): Graph<Int> = Array(size) { Array(size) { -1 } }

fun Graph<Int>.addEdge(from: Int, to: Int, weight: Int) {
  this[from][to] = weight
}

fun Graph<Int>.addEdgeBi(from: Int, to: Int, weight: Int) {
  this[from][to] = weight
  this[to][from] = weight
}

fun Graph<Int>.neighbours(current: Int): Set<Int> =
  this[current].foldIndexed(emptySet()) { idx, acc, e -> if (e != -1 && idx != current) acc + idx else acc }

fun Graph<Int>.dfs(current: Int) {
  val stack = Stack<Int>()
  val visited = mutableSetOf(current)
  stack.add(current)
  while (stack.isNotEmpty()) {
    val node = stack.pop()
    println("Visiting $node")
    this.neighbours(node).reversed().forEach { next ->
      if (next !in visited) {
        stack.add(next)
        visited.add(next)
      }
    }
  }
}

fun Graph<Int>.bfs(current: Int) {
  val queue: Queue<Int> = LinkedList()
  val visited = mutableSetOf(current)
  queue.add(current)
  while (queue.isNotEmpty()) {
    val node = queue.poll()
    println("Visiting $node")
    this.neighbours(node).forEach { next ->
      if (next !in visited) {
        queue.add(next)
        visited.add(next)
      }
    }
  }
}

fun Graph<Int>.dijkstra(src: Int): List<Int> {
  val dist = Array(this.size) { Int.MAX_VALUE }
  dist[src] = 0
  val priorityQueue: MutableList<Pair<Int, Int>> = mutableListOf()
  priorityQueue.add(src to 0)
  while (priorityQueue.isNotEmpty()) {
    priorityQueue.sortBy { it.second }
    val (current, weight) = priorityQueue.first()
    priorityQueue.removeAt(0)
    this.neighbours(current).forEach { next ->
      if (dist[next] > this[current][next] + weight) {
        dist[next] = this[current][next] + weight
        priorityQueue.add(next to dist[next])
      }
    }
  }
  return dist.toList()
}

//TODO() : A* implementation