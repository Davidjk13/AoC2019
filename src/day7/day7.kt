package day7

import intcode.IntcodeComputer
import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val nums = File(file.toURI()).useLines { it.toList() }.get(0).split(",").map { it.toInt() }

    //part 1
    val max = getPerms(emptyList(), listOf(0, 1, 2, 3, 4)).map { findAmp(it, nums) }.max()
    println(max)

    //part 2
//     val max2 = getPerms(emptyList(), listOf(5, 6, 7, 8, 9)).map { findAmp(it, nums) }.max()
//    println(max2)

}

fun findAmp(perm: List<Int>, nums: List<Int>, input: Int = 0): Int {
    var inp = input
    for (phase in perm) {
        inp =  IntcodeComputer(nums.toMutableList(), inp, phase).computeOut()
    }
    return inp
}

//fun findAmpLoop(perm: List<Int>, nums: List<Int>, input: Int = 0): Int {
//    var inp = input
//    var halt: Int? = null
//    for (phase in perm) {
//        val result =  IntcodeComputer(nums.toMutableList(), inp, phase).computeOut()
//        inp = result.first
//        halt = result.second
//    }
//    if (halt == null) return findAmp(perm, nums, inp)
//    return input
//}

fun getPerms(left: List<Int>, nums: List<Int>): Set<List<Int>> {
    if (nums.isEmpty()) return setOf(left)
    return nums.flatMap { num -> getPerms(appendLeft(left, num), nums.filter { it != num }) }.toSet()
}

fun appendLeft(left: List<Int>, it: Int): List<Int> {
    val newleft = left.toMutableList()
    newleft.add(it)
    return newleft
}

