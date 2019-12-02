package day1

import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val lines = File(file.toURI()).useLines { it.toList() }.map { it.toInt() }

    //part 1
    println(lines.sumBy { it.getFuel() })

    //part 2
    println(lines.sumBy { it.getMoreFuel() })
}

fun Int.getFuel(): Int = (this / 3) - 2

fun Int.getMoreFuel() : Int {
    val fuel = this.getFuel()
    if (fuel <= 0) return 0
    return fuel + fuel.getMoreFuel()
}