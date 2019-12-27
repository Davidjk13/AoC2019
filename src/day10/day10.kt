package day10

import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val points = File(file.toURI()).useLines { it.toList() }.mapIndexed { index, s -> getPoints(index, s) }.flatten()

    //part 1
    val views = points.map { viewable(points, it) }
    val max = views.max()
    println(max)

    //part 2
    val station = points.maxBy { viewable(points, it) } ?: Pair(0, 0)
    val prize = points.filter { noneBetween(points, station, it) }[199]

    println(prize)
}

fun viewable(points: List<Pair<Int, Int>>, point: Pair<Int, Int>) = points.count { noneBetween(points, point, it) }

fun noneBetween(points: List<Pair<Int, Int>>, a: Pair<Int, Int>, b: Pair<Int, Int>): Boolean {
    if (a == b) return false
    if (b.first == a.first) {
        return points.filter { it != a && it != b }.none { it.first == a.first && ((it.second > a.second && it.second < b.second) ||  (it.second < a.second && it.second > b.second))}
    }
    if (b.second == a.second) {
        return points.filter { it != a && it != b }.none { it.second == a.second && ((it.first > a.first && it.first < b.first) ||  (it.first < a.first && it.first > b.first))}
    }

    val line = Line(a.first, a.second, b.first, b.second)
    return points.filter { it != a && it != b }.none { line.contains(it.first, it.second) }

}

fun getPoints(y: Int, line: String): List<Pair<Int, Int>> = line.mapIndexed { index, _ ->  Pair(index, y)}.filter { line[it.first] == '#' }

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val m = (y1 - y2).toDouble() / (x1 - x2).toDouble()
    val c = y1 - (m * x1)

    fun contains(x: Int, y: Int): Boolean {
        val a = (m * x) + c - y
        return a <= 0.000001 && a >= -0.000001 && between(x, y)
    }

   fun between(x: Int, y: Int): Boolean = (x in x1..x2 || x in x2..x1) && (y in y1..y2 || y in y2..y1)

}