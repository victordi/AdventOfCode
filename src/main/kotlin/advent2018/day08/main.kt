package advent2018.day08

import utils.Input.withInput
import utils.println

fun main() {
  println(first())
  println(second())
}

data class Node(
  val children: List<Node>,
  val metadata: List<Int>
)

fun Node.metadataSum(): Int = metadata.sum() + children.sumOf { it.metadataSum() }
fun Node.value(): Int = if (children.isEmpty()) {
  metadata.sum()
} else {
  metadata.sumOf { idx -> children.getOrNull(idx - 1)?.value() ?: 0 }
}

fun parse(): Node = withInput { input ->
  val numbers = input.flatMap { line -> line.split(" ").map { it.toInt() } }.iterator()

  fun parseNode(): Node {
    val numChildren = numbers.next()
    val numMetadata = numbers.next()
    val children = List(numChildren) { parseNode() }
    val metadata = List(numMetadata) { numbers.next() }
    return Node(children, metadata)
  }

  return@withInput parseNode()
}

val tree = parse()

fun first(): Int = tree.metadataSum()

fun second(): Int = tree.value()
