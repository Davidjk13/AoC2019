package day7

import intcode.IntcodeComputer
import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val nums = File(file.toURI()).useLines { it.toList() }.get(0).split(",").map { it.toInt() }

    //part 1
    val max = getPerms(emptyList(), listOf(0, 1, 2, 3, 4)).map { findAmp(it, nums) }.max()
    println(max)

//    part 2
    val max2 = getPerms(emptyList(), listOf(5, 6, 7, 8, 9)).map { findAmpLoop(it, nums) }.max()
    println(max2)

}

fun findAmp(perm: List<Int>, nums: List<Int>, input: Int = 0): Int {
    return perm.fold(input){a, b -> IntcodeComputer(nums.toMutableList(), b).computeOut(a).lastOutput}
}

fun findAmpLoop(perm: List<Int>, nums: List<Int>, input: Int = 0): Int {
    var inp = input
    val phaseComputers = HashMap<Int, IntcodeComputer>()
    for (phase in perm) {
        val computer = IntcodeComputer(nums.toMutableList(), phase)
        phaseComputers.put(phase, computer)
        val result = computer.computeOut(inp)
        inp = result.lastOutput
        if (result.halt != null) return inp
    }

    while (true) {
        for (phase in perm) {
            val computer = phaseComputers[phase]
            val result = computer?.computeOut(inp)
            if (result?.halt != null) {
                return inp
            }
            inp = result?.lastOutput ?: 0
        }
    }

}

fun getPerms(left: List<Int>, nums: List<Int>): Set<List<Int>> {
    if (nums.isEmpty()) return setOf(left)
    return nums.flatMap { num -> getPerms(appendLeft(left, num), nums.filter { it != num }) }.toSet()
}

fun appendLeft(left: List<Int>, it: Int): List<Int> {
    val newleft = left.toMutableList()
    newleft.add(it)
    return newleft
}

