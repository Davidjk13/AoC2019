package day9

import intcode.IntcodeComputer
import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val nums = File(file.toURI()).useLines { it.toList() }.get(0).split(",").map { it.toInt() }

    val comp = IntcodeComputer(nums.toMutableList())
    comp.compute()

}