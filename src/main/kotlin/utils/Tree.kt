package utils

sealed class Node<out T, V>
data class Leaf<V>(val value: V) : Node<Nothing, V>()
data class Tree<T, V>(val nodes: MutableMap<T, Node<T, V>>) : Node<T, V>() {
  fun add(key: T, value: Node<T, V>) {
    nodes[key] = value
  }
}