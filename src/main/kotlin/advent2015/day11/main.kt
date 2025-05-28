package advent2015.day11

import utils.Input

val lines = Input.readInput()

fun String.nextPassword(): String {
    var password = this
    do {
        password = password.increment()
    } while (!password.isValid())
    return password
}

fun String.increment(): String {
    val chars = this.toCharArray()
    for (i in chars.size - 1 downTo 0) {
        if (chars[i] == 'z') {
            chars[i] = 'a'
        } else {
            chars[i] = chars[i] + 1
            break
        }
    }
    return String(chars)
}

fun String.isValid(): Boolean {
    if (this.contains("i") || this.contains("o") || this.contains("l")) return false
    if (!this.contains(Regex("(.)\\1.*(.)\\2"))) return false
    if (!this.contains(Regex("abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz"))) return false
    return true
}

fun main() {
    println(first())
    println(second())
}

fun first(): String = lines.first().nextPassword()

fun second(): String = lines.first().nextPassword().nextPassword()