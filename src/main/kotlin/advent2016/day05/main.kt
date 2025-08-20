package advent2016.day05

import utils.println

fun main() {
  println(first())
  println(second())
}

fun String.md5(): String = java.security.MessageDigest
  .getInstance("MD5")
  .digest(this.toByteArray())
  .joinToString("") { "%02x".format(it) }

fun first(): String = run {
  val id = "cxdnnyjw"
  var pwd = ""
  var idx = 0
  while (pwd.length < 8) {
    val hash = "${id}${idx}".md5()
    if (hash.startsWith("00000")) {
      pwd += hash[5]
    }
    idx++
  }
  pwd
}

fun second(): String = run {
  val id = "cxdnnyjw"
  var pwd = "!!!!!!!!"
  var found = 0
  var idx = 0
  while (found < 8) {
    val hash = "${id}${idx}".md5()
    if (hash.startsWith("00000")) {
      val i  = hash[5].digitToIntOrNull() ?: -1
      if (i in 0..7 && pwd[i] == '!') {
        pwd = pwd.replaceRange(i..i, hash[6].toString())
        found++
        pwd.println
      }
    }
    idx++
  }
  pwd
}
