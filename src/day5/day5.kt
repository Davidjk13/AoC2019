package day5

import intcode.IntcodeComputer
import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val nums = File(file.toURI()).useLines { it.toList() }.get(0).split(",").map { it.toInt() }

    val comp = IntcodeComputer(nums.toMutableList())
    comp.compute()

    val comp2 = IntcodeComputer(nums.toMutableList(), 5)
    comp2.compute()
}