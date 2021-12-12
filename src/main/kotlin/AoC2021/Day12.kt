package AoC2021.day12

import Graph
import Input.withInput
import addEdgeBi
import neighbours
import newGraph
import java.util.*

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Long {
    var result = 0L
    withInput { input ->
        val end = 12

        val list = input.map {
            val split = it.split('-')
            split[0] to split[1]
        }.toList()
        val map = mutableMapOf("start" to 0, "end" to end)
        var cnt = 1
        val big = mutableSetOf<Int>()
        val graph: Graph<Int> = newGraph(end + 1)
        for ((a, b) in list) {
            if (a !in map) {
                map[a] = cnt++
            }
            if (b !in map) {
                map[b] = cnt++
            }
            if (a == a.uppercase()) big.add(map[a]!!)
            if (b == b.uppercase()) big.add(map[b]!!)

            graph.addEdgeBi(map[a]!!, map[b]!!, 0)
        }
        big.add(end)

        val done = mutableSetOf<List<Int>>()
        val queue: Queue<Pair<Pair<Int, List<Int>>, Set<Int>>> = LinkedList()
        queue.add(0 to listOf(0) to setOf(0))
        while (queue.isNotEmpty()) {
            val (z, visited) = queue.poll()
            val (node, path) = z
            if (node == end) {
                done.add(path)
                continue
            }
            graph.neighbours(node).forEach { next ->
                if (next !in visited) {
                    if (next !in big){
                        queue.add(next to path + next to visited + next)
                    } else {
                        queue.add(next to path + next to visited)
                    }
                }
            }
        }

        result = done.size.toLong()
    }
    return result
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val end = 12

        val list = input.map {
            val split = it.split('-')
            split[0] to split[1]
        }.toList()

        val map = mutableMapOf("start" to 0, "end" to end)
        var cnt = 1
        val big = mutableSetOf<Int>()
        val graph: Graph<Int> = newGraph(end + 1)
        for ((a, b) in list) {
            if (a !in map) {
                map[a] = cnt++
            }
            if (b !in map) {
                map[b] = cnt++
            }
            if (a == a.uppercase()) big.add(map[a]!!)
            if (b == b.uppercase()) big.add(map[b]!!)

            graph.addEdgeBi(map[a]!!, map[b]!!, 0)
        }
        big.add(end)

        val done = mutableSetOf<List<Int>>()

        data class QueueElement(val node:Int, val path: List<Int>, val visited: Set<Int>, val trick: Boolean)
        val queue: Queue<QueueElement> = LinkedList()
        queue.add(QueueElement(0, listOf(0), setOf(0), false))
        while (queue.isNotEmpty()) {
            val element = queue.poll()
            val (node, path, visited, trick) = element
            if (node == end) {
                done.add(path)
                continue
            }
            graph.neighbours(node).forEach { next ->
                if (next !in visited) {
                    if (next !in big){
                        queue.add(element.copy(node = next, path = path + next, visited = visited + next))
                    } else {
                        queue.add(element.copy(node = next, path = path + next))
                    }
                } else if (!trick && next != 0) {
                    queue.add(element.copy(node = next, path = path + next, visited = visited, trick = true))
                }
            }
        }

        result = done.size.toLong()
    }
    return result
}