package AoC2021.day14

import utils.Input.withInput

fun main() {
    println("Result of the first part: " + first().toString())
    println("Result of the second part: " + second().toString())
}

fun first(): Int {
    var result = 0
    withInput { input ->
        val list = input.toList()
        val template = list.first()
        val rules = list.drop(2).associate { rule ->
            rule.split(" -> ").let {
                it[0] to it[1]
            }
        }

        var current = template
        for (step in 0 until 10) {
            current =
                current.zipWithNext().fold("") { acc, (a, b) ->
                    acc + a + rules.getOrDefault("$a$b", "")
                } + current.last()
        }
        val counts = mutableMapOf<Char, Int>()
        current.forEach { char ->
            counts[char] = counts.getOrDefault(char, 0) + 1
        }
        val sorted = counts.values.sorted()
        result = sorted.last() - sorted.first()
    }
    return result
}

fun Pair<Char, Char>.generateNext(rules: Map<Pair<Char, Char>, Char>): List<Pair<Char, Char>> {
    val generated = rules[this]!!
    return listOf(this.first to generated, generated to this.second)
}

fun second(): Long {
    var result = 0L
    withInput { input ->
        val list = input.toList()
        val template = list.first()
        val rules = list.drop(2).associate { rule ->
            val split = rule.split(" -> ")
            split[0][0] to split[0][1] to split[1][0]
        }

        val occurrences = mutableMapOf<Pair<Char, Char>, Long>()
        template.zipWithNext { a, b ->
            occurrences[a to b] = occurrences.getOrDefault(a to b, 0) + 1
        }

        for (step in 0 until 40) {
            val copy = mutableMapOf<Pair<Char, Char>, Long>()
            occurrences.keys.forEach {
                copy[it] = occurrences[it]!!
            }
            for (it in copy.keys) {
                it.generateNext(rules).forEach { pair ->
                    occurrences[pair] = occurrences.getOrDefault(pair, 0L) + copy[it]!!
                }
                occurrences[it] = occurrences[it]!! - copy[it]!!
            }
        }
        val counts = mutableMapOf<Char, Long>()
        occurrences.keys.forEach { (a, b) ->
            counts[a] = counts.getOrDefault(a, 0L) + occurrences[a to b]!!
            counts[b] = counts.getOrDefault(b, 0L) + occurrences[a to b]!!
        }
        counts[template.first()] = counts[template.first()]!! + 1
        counts[template.last()] = counts[template.last()]!! + 1

        counts.values.map { it / 2 }.sorted().let {
            result = it.last() - it.first()
        }
    }
    return result
}