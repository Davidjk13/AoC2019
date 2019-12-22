package day8

import java.io.File

typealias layer = List<List<Int>>

const val x = 25
const val y = 6
const val chunksize = x * y

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val nums = File(file.toURI()).useLines { it.toList() }[0].map { it.toString().toInt() }

    val layers = getLayers(nums)

    //part 1
    val layer = layers.minBy { it.flatten().count { it == 0 } } ?: emptyList()
    println(layer.flatten().count { it == 1 } * layer.flatten().count { it == 2 })

    //part 2
    println(layers.reduce{first, second -> combineLayers(first, second)})
}

fun combineLayers(first: layer, second: layer): layer {
    return first.flatten()
        .zip(second.flatten())
        .map { if (it.first == 2) it.second else it.first }
        .chunked(x)
}

fun getLayers(nums: List<Int>): List<layer> = nums.chunked(chunksize).map { it.chunked(x) }


