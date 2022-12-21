package advent2022.day21

import java.util.*

data class Monkey(val name: String, val x: String, val y: String, val op: Char)
val numbers = mutableMapOf<String, Long>()
fun parseInput(): List<Monkey> = Input.readInput().mapNotNull {
    val name = it.take(4)
    val rest = it.drop(6)
    if (rest.toIntOrNull() != null) {
        numbers[name] = rest.toLong()
        null
    } else {
        val x = rest.take(4)
        val y = rest.drop(7).take(4)
        val op = rest.drop(5)[0]
        Monkey(name, x, y, op)
    }
}

val monkeys = parseInput().toMutableList()

fun main() {
    println(first())
    println(second())
}

fun first(): Long = run {
    repeat(38) { cnt ->
        for (monkey in monkeys) {
            if (monkey.x in numbers && monkey.y in numbers) {
                val number = when (monkey.op) {
                    '+' -> numbers[monkey.x]!! + numbers[monkey.y]!!
                    '-' -> numbers[monkey.x]!! - numbers[monkey.y]!!
                    '*' -> numbers[monkey.x]!! * numbers[monkey.y]!!
                    '/' -> numbers[monkey.x]!! / numbers[monkey.y]!!
                    else -> throw IllegalArgumentException("Unsupported op: ${monkey.op}")
                }
                numbers[monkey.name] = number
                if (monkey.name == "root") return number.also { println(cnt) }
            }
        }
    }
    -1
}

fun second(): Long = run { // root : 21973580688943 == lccz
    numbers.clear()
    val monkeys = parseInput()
    val current = LinkedList<String>()
    current.add("lccz")
    numbers.remove("humn")
    repeat(38) { cnt ->
        for (monkey in monkeys) {
            if (monkey.x in numbers && monkey.y in numbers) {
                val number = when (monkey.op) {
                    '+' -> numbers[monkey.x]!! + numbers[monkey.y]!!
                    '-' -> numbers[monkey.x]!! - numbers[monkey.y]!!
                    '*' -> numbers[monkey.x]!! * numbers[monkey.y]!!
                    '/' -> numbers[monkey.x]!! / numbers[monkey.y]!!
                    else -> throw IllegalArgumentException("Unsupported op: ${monkey.op}")
                }
                numbers[monkey.name] = number
            }
        }
    }
    var op = ""
    while(current.isNotEmpty()) {
        val name = current.pop()
        if (name == "humn") continue
        val monkey = monkeys.find { it.name == name }!!
        val x = numbers[monkey.x]?.toString() ?: monkey.x.also { current.add(monkey.x) }
        val y = numbers[monkey.y]?.toString() ?: monkey.y.also { current.add(monkey.y) }
        op += "${monkey.name} = $x ${monkey.op} $y | "
    }
    println(op)

    val bnct = 21973580688943L * 4
    val cvhv = bnct - 358
    val gwst = cvhv / 2
    val dstd = 101381294475907L - gwst
    val dqcj = dstd / 4
    val mnmb = dqcj + 483
    val zcnc = mnmb * 3
    val njdc = zcnc - 553
    val zgnz = njdc / 2
    val srrz = zgnz + 136
    val fbrw = srrz * 3
    val zffj = fbrw - 187
    val wtcg = zffj / 2
    val gtls = wtcg - 781
    val bdns = gtls * 3
    val rvtr = bdns - 74
    val qfzq = rvtr + 155
    val vzbp = qfzq / 18
    val lrmw = vzbp - 204
    val zftv = lrmw * 5
    val dhdf = zftv + 244
    val jqvq = dhdf * 2
    val cwrj = jqvq - 222
    val humn = ((((((((((((((((((((cwrj / 2 + 825 ) * 3 + 993 ) / 2 - 628 ) * 3 + 227 ) / 4 - 453 ) * 2 - 830 ) / 2 + 965 ) / 3 - 567 ) * 4 + 181 ) * 2 + 30 ) / 2 - 8) / 16 - 291) * 3 + 78) * 9 - 114) / 3 + 978) * 5 + 501) / 2 - 52) / 3 + 304) * 3 - 670) * 2 - 548) / 29 + 375

    humn
}