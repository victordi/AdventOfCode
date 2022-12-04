import arrow.core.identity
import java.io.File
import java.nio.charset.Charset
import java.util.*

fun Int.modSkipZero(n: Int) : Int = if (this % n == 0) n else this % n


object Input {
    private val file = File({}::class.java.getResource("/input.txt")?.file ?: "")

    fun <T> withInput(handler: (Sequence<String>) -> T): T =
        file.useLines(Charset.defaultCharset(), handler)
}

fun String.splitTwo(delimiter: String): Pair<String, String> =
    splitTwo(delimiter) { it }

fun <T> String.splitTwo(delimiter: String, f: (String) -> T): Pair<T, T> =
    split(delimiter).let { f(it[0]) to f(it[1]) }

fun String.toDecimal(): Long = this.reversed().fold(1L to 0L) { (pow2, result), c ->
    pow2 * 2 to  result + if (c == '1') pow2 else 0
}.second

fun <T> Iterable<T>.zip3(): List<Triple<T, T, T>> {
    val iterator = iterator()
    if (!iterator.hasNext()) return emptyList()
    var current = iterator.next()
    if (!iterator.hasNext()) return emptyList()
    val result = mutableListOf<Triple<T, T, T>>()
    var next = iterator.next()
    while (iterator.hasNext()) {
        val aux = iterator.next()
        result.add(Triple(current, next, aux))
        current = next
        next = aux
    }
    return result
}

fun <T> List<T>.each3(): List<Triple<T, T, T>> {
    val result = mutableListOf<Triple<T, T, T>>()
    for (i in indices step 3) {
        result.add(Triple(this[i], this[i + 1], this[i + 2]))
    }
    return result
}

fun <T> List<T>.split(delimiter: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    val current = mutableListOf<T>()

    this.forEach {
        if (delimiter(it)) {
            result.add(current.toList())
            current.clear()
        }
        else current.add(it)
    }
    result.add(current.toList())

    return result
}

/* Matrix stuff */

fun <T> Array<Array<T>>.getAdjacent(x: Int, y: Int): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    val n = this.size - 1
    if (x < n) result.add(x + 1 to y)
    if (x > 0) result.add(x - 1 to y)
    if (y < n) result.add(x to y + 1)
    if (y > 0) result.add(x to y - 1)
    return result
}

fun <T> Array<Array<T>>.getAdjacentWithCorners(x: Int, y: Int): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    for (x_fact in -1..1) {
        for (y_fact in -1..1) {
            if (x + x_fact in this.indices && y + y_fact in this[0].indices) {
                result.add(x + x_fact to y + y_fact)
            }
        }
    }
    result.remove(x to y)
    return result
}

fun <T> Array<Array<T>>.prettyPrint() {
    for (i in this.indices) {
        var line = ""
        for (j in this[0].indices) {
            line += this[i][j]
            line += " "
        }
        println(line.dropLast(1))
    }
}

fun <T> Array<Array<T>>.arrayForEach(f: (Pair<Int, Int>) -> Unit) {
    for (i in this.indices)
        for (j in this[0].indices)
            f(i to j)
}

infix fun <T> Array<Array<T>>.arrayEquals(other: Array<Array<T>>): Boolean {
    if (this.size != other.size) return false
    if (this[0].size != other[0].size) return false
    return this.foldIndexed(true) {i, acc, line ->
        acc && line.foldIndexed(true) { j ,acc, e -> acc && e == other[i][j] }
    }
}


/* Graph Stuff */

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