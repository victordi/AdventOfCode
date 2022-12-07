package advent2022.day7

import Input.readInput
import Leaf
import Tree
import arrow.core.foldLeft
import splitTwo

sealed class Instruction
data class CD(val path: String): Instruction()
object LS: Instruction()
data class Dir(val name: String): Instruction()
data class File(val name: String, val size: Long): Instruction()

fun String.parseInstruction(): Instruction =
    if (startsWith("$ cd")) CD(removePrefix("$ cd "))
    else if (this == "$ ls") LS
    else if (startsWith("dir")) Dir(removePrefix("dir "))
    else splitTwo(" ").let { (size, name) -> File(name, size.toLong()) }

val dirSizes = mutableListOf<Long>()
fun Tree<String, Long>.getSize(): Long =
    nodes.foldLeft(0L) { acc, (_, node) ->
        acc + when (node) {
            is Leaf -> node.value
            is Tree -> node.getSize()
        }
    }.also { dirSizes.add(it) }

fun Tree<String, Long>.currentDirectory(path: List<String>): Tree<String, Long> =
    path.fold(this) { acc, dirName ->
        acc.nodes[dirName]!! as Tree
    }

fun newDir(): Tree<String, Long> = Tree(mutableMapOf())

fun main() {
    println(first())
    println(second())
}

fun parseInput(): Tree<String, Long> = run {
    val path = mutableListOf<String>()
    var currentNode = newDir()
    val tree = newDir()
    tree.nodes["/"] = currentNode

    readInput()
        .map { it.parseInstruction() }
        .forEach { instruction ->
            when(instruction) {
                is CD -> if (instruction.path == "..") path.removeLast() else path.add(instruction.path)
                is LS -> currentNode = tree.currentDirectory(path)
                is Dir -> currentNode.add(instruction.name, newDir())
                is File -> currentNode.add(instruction.name, Leaf(instruction.size))
            }
        }
    return tree
}

fun first(): Long = with(readInput()) {
    val tree = parseInput()
    tree.getSize()
    dirSizes.filter { it <= 100000L }.sum()
}

fun second(): Long {
    dirSizes.sort()
    val toFree = 30000000L - (70000000L - dirSizes.last())
    return dirSizes.first { it >= toFree }
}
