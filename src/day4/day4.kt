package day4

fun main() {

    val passwords = 206938.rangeTo(679128).filter { passCheck(it, ::passDouble) }.count()
    val betterPasswords = 206938.rangeTo(679128).filter { passCheck(it, ::passBetterDouble) }.count()

    println(passwords)
    println(betterPasswords)
}

fun passCheck(password: Int, doubleCheck: (CharArray) -> Boolean): Boolean {
    val digits = password.toString().toCharArray()
    return doubleCheck(digits) && passLeftToRight(digits)
}

fun passLeftToRight(digits: CharArray): Boolean = 0.rangeTo(4).all { digits[it] <= digits[it + 1]}

fun passDouble(digits: CharArray) : Boolean = 0.rangeTo(4).any { digits[it] == digits[it + 1] }

fun passBetterDouble(digits: CharArray) : Boolean = 0.rangeTo(4).any { digits[it] == digits[it + 1]
                                                                          && digits.getOrNull(it - 1) != digits[it]
                                                                          && digits[it + 1] != digits.getOrNull(it + 2) }
