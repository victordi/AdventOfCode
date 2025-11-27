package advent2017.day07

import utils.Input.withInput
import utils.Leaf
import utils.Node
import utils.Tree
import java.util.Stack

fun main() {
  println(first())
  println(second())
}

data class Tower(val name: String, val weight: Int, val children: List<String> = emptyList())

val towers = withInput { input ->
  input.toList().map { line ->
    val regex = Regex("(\\w+) \\((\\d+)\\)( -> (.*))?")
    val matchResult = regex.matchEntire(line) ?: throw IllegalArgumentException("Invalid line format: $line")
    val (name, weight, children) = matchResult.destructured
    val childrenList = if (children.isEmpty()) emptyList() else children.drop(4).filter { it != ' ' }.split(",")
    Tower(name, weight.toInt(), childrenList)
  }
}


fun first(): String = towers.first { (name, _, _) ->
  towers.none { (_, _, children) -> children.contains(name) }
}.name

fun Node<String, Int>.weight(root: String): Int = when(this) {
  is Tree -> {
    val ownWeight = towers.find { it.name == root }!!.weight
    val childrenWeight = nodes.entries.sumOf { (name, node) ->
      when (node) {
        is Tree -> node.weight(name)
        is Leaf<Int> -> node.value
      }
    }
    ownWeight + childrenWeight
  }
  is Leaf<Int> -> value
}

fun second(): Int = run {
  val rootName = first()
  val root: Tree<String, Int> = Tree(mutableMapOf())
  val stack = Stack<Pair<String, Tree<String, Int>>>()
  stack.add(rootName to root)
  while(stack.isNotEmpty()) {
    val (current, tree) = stack.pop()
    val tower = towers.find { it.name == current }!!
    tower.children.forEach {
      val child = Tree<String, Int>(mutableMapOf())
      tree.add(it, child)
      stack.add(it to child)
    }
  }

  var current = root

  current.nodes.entries.forEach { (name, node) ->
    println("$name: ${node.weight(name)}") // rbzmniw
  }

  current = current.nodes["rbzmniw"]!! as Tree<String, Int>
  current.nodes.entries.forEach { (name, node) ->
    println("$name: ${node.weight(name)}") // exrud
  }

  current = current.nodes["exrud"]!! as Tree<String, Int>
  current.nodes.entries.forEach { (name, node) ->
    println("$name: ${node.weight(name)}") // gozhrsf
  }

  current = current.nodes["gozhrsf"]!! as Tree<String, Int>
  current.nodes.entries.forEach { (name, node) ->
    println("$name: ${node.weight(name)}") // equal => gozhrsf - 5
  }

  757
}