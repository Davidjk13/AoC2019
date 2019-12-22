package day2
import intcode.IntcodeComputer
import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val nums = File(file.toURI()).useLines { it.toList() }.get(0).split(",").map { it.toInt() }

    //day 1
    println(getValue(nums))

    //day 2
    for (i in 0..99) {
        findNum(nums, i)
    }

}

fun findNum(nums : List<Int>, i: Int = 12) {
    for (num in 0..99) {
        if (getValue(nums, i, num ) == 19690720) println((100 * i) + num)
    }
}

fun getValue(values : List<Int>, noun: Int = 12, verb: Int = 2) : Int {
    val nums = values.toMutableList()
    nums[1] = noun
    nums[2] = verb

    for (i in nums.indices step 4) {
        when(nums[i]) {
            99 -> return nums[0]
            1 -> nums.runOp(i, Int::plus)
            2 -> nums.runOp(i, Int::times)
        }
    }

    return nums[0]
}

fun MutableList<Int>.runOp(start : Int, op: (Int, Int) -> Int) {
    val result = op(this.getVal(start + 1), this.getVal(start + 2))
    this[this[start + 3]] = result
}

fun List<Int>.getVal(index: Int) = this[this[index]]