import java.io.File
import java.nio.charset.Charset

object Input {
    private val file = File({}::class.java.getResource("/input.txt")?.file ?: "")

    fun withInput(handler: (Sequence<String>) -> Unit) {
        file.useLines(Charset.defaultCharset(), handler)
    }
}

fun <T> Array<Array<T>>.getAdjacent(x: Int, y: Int): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    val n = this.size - 1
    if (x < n) result.add(x + 1 to y)
    if (x > 0) result.add(x - 1 to y)
    if (y < n) result.add(x to y + 1)
    if (y > 0) result.add(x to y - 1)
    return result
}

fun <T> Array<Array<T>>.getAdjacentWithCorners(x: Int, y: Int): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    for (x_fact in -1..1) {
        for (y_fact in -1..1) {
            if (x + x_fact in this.indices && y + y_fact in this[0].indices) {
                result.add(x + x_fact to y + y_fact)
            }
        }
    }
    result.remove(x to y)
    return result
}