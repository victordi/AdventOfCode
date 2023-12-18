package advent2022.day16

import utils.Input

val neighbours: MutableMap<String, List<String>> = mutableMapOf()
val valves: MutableMap<String, Int> = mutableMapOf()

fun parseInput() = run {
    val input = Input.readInput()
    input.forEach { line ->
        val valve = line.drop(6).take(2)
        val flow = line.drop("Valve AA has flow rate=".length).takeWhile { it != ';' }.toInt()
        val n = line.split(" to ")[1].removePrefix("valves ").removePrefix("valve ")
            .split(", ")
        valves[valve] = flow
        neighbours[valve] = n
    }
}

data class State(
    val valves: Set<Pair<String, Int>>,
    val current: String,
    val turn: Int
)

val results = mutableListOf<State>()

fun main() {
    parseInput()
    println(first())
    println(second())
}


val visited = mutableSetOf<State>()
fun backtracking(state: State) {
    visited += state
    if (state.turn >= 30) {
        results += state
        return
    }
    if (state.valves.size == valves.size) {
        results += state
        return
    }
    for (neighbour in neighbours[state.current]!!) {
        if (neighbour !in state.valves.map { it.first }) {
            if (valves[neighbour]!! == 0) {
                val newState = state.copy(valves = state.valves + (neighbour to state.turn + 1), current = neighbour, turn = state.turn + 1)
                if (newState !in visited) backtracking(newState)
            } else {
                val newState = state.copy(valves = state.valves + (neighbour to state.turn + 2), current = neighbour, turn = state.turn + 2)
                val newStateNotOpen = state.copy(current = neighbour, turn = state.turn + 1)
                if (newState !in visited) backtracking(newState)
                if (newStateNotOpen !in visited) backtracking(newStateNotOpen)
            }
        } else {
            val newState = state.copy(current = neighbour, turn = state.turn + 1)
            if (newState !in visited) backtracking(newState)
        }
    }
}

val visited2 = mutableSetOf<Pair<State, State>>()
val results2 = mutableListOf<Pair<State, State>>()
fun backtrackingPart2(state: State, elephantState: State) {
    visited2 += state to elephantState
    if (state.turn >= 30 && elephantState.turn >= 30) {
        results2 += state to elephantState
        return
    }
    if (state.valves.size + elephantState.valves.size == valves.size + 1) {
        results2 += state to elephantState
        return
    }
    for (neighbour in neighbours[state.current]!!) {
        val (next, next2) = if (neighbour !in elephantState.valves.map { it.first } && neighbour !in state.valves.map { it.first }) {
            if (valves[neighbour]!! == 0) {
                val newState = state.copy(
                    valves = state.valves + (neighbour to state.turn + 1),
                    current = neighbour,
                    turn = state.turn + 1
                )
                newState to null
            } else {
                val newState = state.copy(
                    valves = state.valves + (neighbour to state.turn + 2),
                    current = neighbour,
                    turn = state.turn + 2
                )
                val newStateNotOpen = state.copy(current = neighbour, turn = state.turn + 1)
                newState to newStateNotOpen
            }
        } else {
            val newState = state.copy(current = neighbour, turn = state.turn + 1)
            newState to null
        }
        for (eleNeighbour in neighbours[elephantState.current]!!) {
            val (eleNext, eleNext2) = if (eleNeighbour !in elephantState.valves.map { it.first } && eleNeighbour !in state.valves.map { it.first }) {
                if (valves[neighbour]!! == 0) {
                    val newState = elephantState.copy(
                        valves = elephantState.valves + (eleNeighbour to elephantState.turn + 1),
                        current = eleNeighbour,
                        turn = elephantState.turn + 1
                    )
                    newState to null
                } else {
                    val newState = elephantState.copy(
                        valves = elephantState.valves + (eleNeighbour to elephantState.turn + 2),
                        current = neighbour,
                        turn = elephantState.turn + 2
                    )
                    val newStateNotOpen = elephantState.copy(current = eleNeighbour, turn = elephantState.turn + 1)
                    newState to newStateNotOpen
                }
            } else {
                val newState = elephantState.copy(current = eleNeighbour, turn = elephantState.turn + 1)
                newState to null
            }
            if (next to eleNext !in visited2) backtrackingPart2(next, eleNext)
            if (next2 != null && eleNext2 != null && next2 to eleNext2 !in visited2) backtrackingPart2(next2, eleNext2)
            if (next2 != null && next2 to eleNext !in visited2) backtrackingPart2(next2, eleNext)
            if (eleNext2 != null && next to eleNext2 !in visited2) backtrackingPart2(next, eleNext2)
        }
    }
}

fun score(state: State): Int {
    var result = 0
    state.valves.forEach { (valve, start) ->
        if (start < 30) result += (30 - start) * valves[valve]!!
    }
    return result
}

fun first(): Int = run {
    backtracking(State(mutableSetOf("AA" to 0), "AA", 0))
    results.sortByDescending { state -> score(state) }
    score(results.first())
}

fun second(): Int = run {
    backtrackingPart2(State(mutableSetOf("AA" to 0), "AA", 4), State(mutableSetOf("AA" to 0), "AA", 4))
    results2.sortByDescending { (state, eleState) -> score(state)  + score(eleState) }
    results2.first().let { (state, eleState) -> score(state)  + score(eleState) }
}