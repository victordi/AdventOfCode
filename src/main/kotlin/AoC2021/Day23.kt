package AoC2021.Day23

fun main() {
    gameState.prettyPrint()

    gameState2.prettyPrint2()
}

data class GameState(
    val colA: List<Char>,
    val colB: List<Char>,
    val colC: List<Char>,
    val colD: List<Char>,
    val hallway: List<Char>
) {
    fun prettyPrint() {
        hallway.forEach { print(it) }
        println()
        println("##${colA[1]}#${colB[1]}#${colC[1]}#${colD[1]}##")
        println(" #${colA[0]}#${colB[0]}#${colC[0]}#${colD[0]}# ")
        println(" ######### ")
    }

    fun prettyPrint2() {
        hallway.forEach { print(it) }
        println()
        println("##${colA[3]}#${colB[3]}#${colC[3]}#${colD[3]}##")
        println("##${colA[2]}#${colB[2]}#${colC[2]}#${colD[2]}##")
        println("##${colA[1]}#${colB[1]}#${colC[1]}#${colD[1]}##")
        println(" #${colA[0]}#${colB[0]}#${colC[0]}#${colD[0]}# ")
        println(" ######### ")
    }

    fun remove(col: Char, idx: Int): GameState =
        when (col) {
            'A' -> this.copy(colA = colA.subList(0, idx) + '.' + colA.subList(idx + 1, colA.size))
            'B' -> this.copy(colB = colB.subList(0, idx) + '.' + colB.subList(idx + 1, colB.size))
            'C' -> this.copy(colC = colC.subList(0, idx) + '.' + colC.subList(idx + 1, colC.size))
            'D' -> this.copy(colD = colD.subList(0, idx) + '.' + colD.subList(idx + 1, colD.size))
            else -> TODO()
        }
    fun add(col: Char) =
        when (col) {
            'A' -> this.copy(colA = colA + 'A')
            'B' -> this.copy(colA = colB + 'B')
            'C' -> this.copy(colC = colC + 'C')
            'D' -> this.copy(colA = colD + 'D')
            else -> TODO()
        }
}

val gameState = GameState(
    listOf('A','B'),
    listOf('D','C'),
    listOf('C','B'),
    listOf('A','D'),
    (0..11).map { '.' }
)


val gameState2 = GameState(
    listOf('A','D','D','B'),
    listOf('D','B','C','C'),
    listOf('C','A','B','B'),
    listOf('A','C','A','D'),
    (0..11).map { '.' }
)