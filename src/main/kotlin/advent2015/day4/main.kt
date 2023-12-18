package advent2015.day4

import utils.Input
import java.math.BigInteger
import java.security.MessageDigest

val lines = Input.readInput()

fun main() {
    println(first())
    println(second())
}

fun md5Hash(str: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}

fun first(): Int = run {
    var i = 0
    val key = "yzbqklnj"
    while(true) {
        i++
        if (md5Hash(key + i.toString()).take(5) == "00000") return i
    }
    TODO()
}

fun second(): Long = run {
    var i = 282749L
    val key = "yzbqklnj"
    while(true) {
        i++
        if (md5Hash(key + i.toString()).take(6) == "000000") return i
    }
    TODO()
}