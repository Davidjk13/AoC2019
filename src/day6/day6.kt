package day6

import java.io.File

fun main() {
    val file = object {}.javaClass.getResource("input.txt")
    val lines = File(file.toURI()).useLines { it.toList() }

    val orbits = lines.map { getOrbit(it) }.toMap()

    //part 1
    val sum = orbits.map { findOrbits(it.key, orbits) }.sum()
    println(sum)

//    part 2
    println(findShortestPath(orbits))
}

fun findShortestPath(orbits: Map<String, String>): Int {
    val you = getChain("YOU", orbits)
    val santa = getChain("SAN", orbits)

    val crossover = you.first { santa.contains(it) }
    return you.indexOf(crossover) + santa.indexOf(crossover)
}

tailrec fun getChain(start: String, orbits: Map<String, String>, chains: List<String> = ArrayList()): List<String> {
    val next = orbits[start] ?: return chains
    return getChain(next, orbits, chains + next)
}

tailrec fun findOrbits(orbit: String, orbits: Map<String, String>, count: Int = 0): Int {
    val previous = orbits[orbit] ?: return count
    return findOrbits(previous, orbits, count + 1)
}

fun getOrbit(line: String): Pair<String, String> {
    val split = line.split(")")
    return split[1] to split[0]
}
