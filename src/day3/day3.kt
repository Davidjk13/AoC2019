package day3

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val lines = File(file.toURI()).useLines { it.toList() }
    val first = lines[0]
    val second = lines[1]

    val firstPoints = getPoints(first.split(","))
    val secondPoints = getPoints(second.split(","))

    val crossover = firstPoints.intersect(secondPoints)

    //part 1
    val distance = crossover.map { manhat(it) }.filter { it > 0 }.min()
    println(distance)


    //part 2
    val time = crossover.map { minsteps(it, firstPoints, secondPoints) }.filter { it > 0 }.min()
    println(time)
}

fun minsteps(intersection: Pair<Int, Int>, firstPoints: List<Pair<Int, Int>>, secondPoints: List<Pair<Int, Int>>): Int {
    return firstPoints.indexOfFirst { it == intersection } + secondPoints.indexOfFirst { it == intersection }
}

fun manhat(intersection: Pair<Int, Int>): Int = intersection.first.absoluteValue + intersection.second.absoluteValue

fun getPoints(ins : List<String>) : List<Pair<Int, Int>> {
    val points = ArrayList<Pair<Int, Int>>()
    points.add(Pair(0, 0))

    for (item in ins) {

        when (item[0]) {
            'U' -> points.addAll(getY(points.last().first, points.last().second, item.substring(1).toInt()))
            'D' -> points.addAll(getYDown(points.last().first, points.last().second, -item.substring(1).toInt()))
            'L' -> points.addAll(getXDown(points.last().first, points.last().second, -item.substring(1).toInt()))
            'R' -> points.addAll(getX(points.last().first, points.last().second, item.substring(1).toInt()))
        }
    }

    return points
}


fun getY(x: Int, y: Int, move: Int): Collection<Pair<Int, Int>> = (y + 1).rangeTo(y + move).map { Pair(x, it) }
fun getYDown(x: Int, y: Int, move: Int): Collection<Pair<Int, Int>> = (y - 1).downTo(y + move).map { Pair(x, it) }

fun getX(x: Int, y: Int, move: Int): Collection<Pair<Int, Int>> = (x + 1).rangeTo(x + move).map { Pair(it, y) }
fun getXDown(x: Int, y: Int, move: Int): Collection<Pair<Int, Int>> = (x - 1).downTo(x + move).map { Pair(it, y) }



