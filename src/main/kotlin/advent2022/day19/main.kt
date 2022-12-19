package advent2022.day19

import arrow.core.Tuple4
import java.util.*
import kotlin.system.measureTimeMillis

data class Blueprint(
    val id: Int,
    val oreRobot: Resource,
    val clayRobot: Resource,
    val obsidianRobot: Resource,
    val geodeRobot: Resource
) {
    val maxOre: Int
        get() = maxOf(clayRobot.first, obsidianRobot.first, geodeRobot.first)

    val maxClay: Int
        get() = obsidianRobot.second

    val maxObsidian: Int
        get() = geodeRobot.third
}

typealias Resource = Tuple4<Int, Int, Int, Int>

fun Resource.hasEnough(other: Resource) = first >= other.first && second >= other.second && third >= other.third
infix operator fun Resource.minus(other: Resource) = Tuple4(first - other.first, second - other.second, third - other.third, fourth - other.fourth)
infix operator fun Resource.plus(other: Resource) = Tuple4(first + other.first, second + other.second, third + other.third, fourth + other.fourth)

val blueprints = Input.readInput().mapIndexed { index, s ->
    val split = s.split(" costs ")
    val ore = split[1].split(" ")[0].toInt()
    val clay = split[2].split(" ")[0].toInt()
    val obsidian = split[3].split(" ").let { it[0].toInt() to it[3].toInt() }
    val geode = split[4].split(" ").let { it[0].toInt() to it[3].toInt() }
    Blueprint(index + 1, Tuple4(ore, 0, 0, 0), Tuple4(clay, 0, 0, 0), Tuple4(obsidian.first, obsidian.second, 0, 0), Tuple4(geode.first, 0, geode.second, 0))
}

fun main() {
    println("Part 1 took ${measureTimeMillis { println(first()) }} ms")  // 1306
    println("Part 2 took ${measureTimeMillis { println(second()) }} ms") // 37604
}

typealias State = Triple<Resource, Resource, Int>

fun Blueprint.bfs(initialState: State, turns: Int): MutableList<Int> {
    val maxGeode = mutableMapOf<Int, Int>()
    val results: MutableList<Int> = mutableListOf()
    val visited: MutableSet<State> = mutableSetOf()
    val queue = LinkedList<State>()
    var maxProducedGeode = 0
    queue.add(initialState)

    while(queue.isNotEmpty()) {
        val (robots, resources, minute) = queue.pop()

        if (minute >= turns) {
            results.add(resources.fourth)
            continue
        }

        if (robots.fourth < maxGeode.getOrDefault(minute, 0) - 1) continue
        maxGeode[minute] = maxOf(robots.fourth, maxGeode.getOrDefault(minute, 0))

        val state = Triple(robots, resources, 0)
        if (state in visited) continue
        visited.add(state)

        val newResources = resources + robots
        if (resources.hasEnough(oreRobot) && robots.first < maxOre) queue.add(Triple(robots.copy(first = robots.first + 1), newResources - oreRobot, minute + 1))
        if (resources.hasEnough(clayRobot) && robots.second < maxClay) queue.add(Triple(robots.copy(second = robots.second + 1), newResources - clayRobot, minute + 1))
        if (resources.hasEnough(obsidianRobot) && robots.third < maxObsidian) queue.add(Triple(robots.copy(third = robots.third + 1), newResources - obsidianRobot, minute + 1))
        if (resources.hasEnough(geodeRobot)) queue.add(Triple(robots.copy(fourth = robots.fourth + 1), newResources - geodeRobot, minute + 1))
        if (!resources.hasEnough(geodeRobot) && resources.first < maxOre + 1) queue.add(Triple(robots, newResources, minute + 1))
    }
    return results
}

fun first(): Int =
    blueprints.sumOf {
        it.bfs(Triple(Tuple4(1, 0, 0, 0), Tuple4(0, 0, 0, 0), 0), 24).max() * it.id
    }

fun second(): Long =
    blueprints.take(3).fold(1) { acc, blueprint ->
        acc * blueprint.bfs(Triple(Tuple4(1, 0, 0, 0), Tuple4(0, 0, 0, 0), 0), 32).max()
    }