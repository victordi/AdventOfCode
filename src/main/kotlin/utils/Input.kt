package utils

import java.io.File
import java.nio.charset.Charset

object Input {
  private val file = File({}::class.java.getResource("/input.txt")?.file ?: "")

  fun <T> withInput(handler: (Sequence<String>) -> T): T =
    file.useLines(Charset.defaultCharset(), handler)

  fun readInput(): List<String> = file.readLines(Charset.defaultCharset())
}