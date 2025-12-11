package days.day11

import days.Day

class Day11 : Day(false) {
    override fun partOne() = goDown(
        "you",
        "out",
        mapOf(*readInput().map { it.substringBefore(":") to it.substringAfter(": ").split(" ") }.toTypedArray())
    )

    override fun partTwo(): Any {
        val nodes = mapOf(*readInput().map { it.substringBefore(":") to it.substringAfter(": ").split(" ") }.toTypedArray())
        val dacToOut = goDown("dac", "out", nodes)
        val svrToFft = goDown("svr", "fft", nodes)
        val fftToDac = goDown("fft", "dac", nodes)
        return svrToFft * fftToDac * dacToOut
    }

    fun goDown(
        curr: String,
        dest: String,
        nodes: Map<String, List<String>>,
        cache: MutableMap<String, Long> = mutableMapOf(),
    ): Long {
        if (curr == dest) return 1
        if (cache.containsKey(curr)) return cache[curr]!!
        val res = nodes[curr]?.sumOf { label ->
            goDown(label, dest, nodes, cache)
        }?:0
        cache[curr] = res
        return res
    }
}

