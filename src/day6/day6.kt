package day6

import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val lines = File(file.toURI()).useLines { it.toList() }

    val orbits = lines.map { getOrbit(it) }

    //part 1
    val sum = orbits.map { findOrbits(it, orbits) }.sum() + orbits.size
    println(sum)

    //part 2
    val you = orbits.filter { it.orbit == "YOU" }[0]
    println(findShortestPath(you, orbits, 0, Orbit("NOPE", "NOPE")))
}

fun findShortestPath(you: Orbit, orbits: List<Orbit>, i: Int, previous: Orbit): Int? {
    if (you.orbit == "SAN") return i - 2

    val linked = orbits.filter { it != previous && (it.orbit == you.point || it.point == you.orbit) }

    if (linked.isEmpty()) return orbits.size
    return linked.map { findShortestPath(it, orbits, i + 1, you) }.minBy { it ?: 0 }
}

fun findOrbits(orbit: Orbit, orbits: List<Orbit>): Int {
    val previous = orbits.filter { it.orbit == orbit.point }
    return previous.count() + previous.map { findOrbits(it, orbits) }.sum()
}

fun getOrbit(line: String): Orbit {
    val split = line.split(")")
    return Orbit(split[0], split[1])
}

data class Orbit (val point : String, val orbit : String)