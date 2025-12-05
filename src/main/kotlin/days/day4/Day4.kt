package days.day4

import days.Day
import dev.stenz.algorithms.coordinate.get8Neighbours
import dev.stenz.algorithms.coordinate.mapFromStringList

class Day4 : Day(false) {
    override fun partOne(): Any {
        val map = mapFromStringList(readInput())

        return map.count { entry ->
            entry.value == "@" && entry.key.get8Neighbours().map {
                if (map.containsKey(it)) map[it] else "."
            }.count { it == "@" } < 4
        }
    }

    override fun partTwo(): Any {
        val map = mapFromStringList(readInput())

        var noMore = false
        var res = 0
        while (!noMore) {
            noMore = true
            map.forEach { entry ->
                if (entry.value == "@" && entry.key.get8Neighbours().map {
                        if (map.containsKey(it)) map[it] else "."
                    }.count { it == "@" } < 4) {
                    res++
                    noMore = false
                    map[entry.key] = "."
                }
            }
        }
        return res
    }
}

