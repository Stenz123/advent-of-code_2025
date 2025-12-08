package days.day7

import days.Day
import dev.stenz.algorithms.coordinate.Coord
import dev.stenz.algorithms.coordinate.arrayMapFromStringList
import dev.stenz.algorithms.coordinate.draw
import dev.stenz.algorithms.coordinate.x
import dev.stenz.algorithms.coordinate.y
import dev.stenz.algorithms.uti.ConsoleColors

class Day7: Day(false) {
    override fun partOne(): Any {
        val data = readInput()
        var start: Coord? = null
        val splitterHitMap = mutableMapOf<Coord, Boolean>()
        for (y in data.indices) {
            for (x in data[y].indices) {
                if (data[y][x] == '^') {
                    splitterHitMap[x to y] = false
                }
                if (data[y][x] == 'S') {
                    start = x to y
                }
            }
        }

        val printMap = arrayMapFromStringList(data)
        val res = getFirstSplitterUnder(splitterHitMap, start!!, data.size-1, printMap)
        println(splitterHitMap.entries.count { it.value })
        printMap.draw()
        return res
    }

    fun getFirstSplitterUnder(splitterHitMap: MutableMap<Coord, Boolean>, start: Coord, bottomY: Int, printMap: Array<Array<String>>): Int{
        var y = start.y()
        while (y <= bottomY) {

            if (splitterHitMap[start.x() to y] == false) {
                splitterHitMap[start.x() to y] = true
                printMap[y][start.x()] = ConsoleColors.CYAN+"^"+ConsoleColors.RESET
                return 1 + getFirstSplitterUnder(splitterHitMap, (start.x() - 1) to y, bottomY, printMap) + getFirstSplitterUnder(splitterHitMap, (start.x() + 1) to y, bottomY, printMap)
            }
            if (splitterHitMap[start.x() to y] == true) return 0
            printMap[y][start.x()] = "|"
            y++
        }
        return 0
    }

    override fun partTwo(): Any {
        val data = readInput()
        var start: Coord? = null
        val splitterHitMap = mutableMapOf<Coord, Boolean>()
        for (y in data.indices) {
            for (x in data[y].indices) {
                if (data[y][x] == '^') {
                    splitterHitMap[x to y] = false
                }
                if (data[y][x] == 'S') {
                    start = x to y
                }
            }
        }

        val cache = mutableMapOf<Coord, Long>()
        val res = getFirstSplitterUnderPart2(splitterHitMap, start!!, data.size-1, cache)


        return res+1
    }

    fun getFirstSplitterUnderPart2(splitterHitMap: MutableMap<Coord, Boolean>, start: Coord, bottomY: Int, cache: MutableMap<Coord, Long>): Long{
        if(cache.containsKey(start))return cache[start]!!
        var y = start.y()
        while (y <= bottomY) {
            if (splitterHitMap.containsKey(start.x() to y)) {
                val res = 1 + getFirstSplitterUnderPart2(splitterHitMap, (start.x() - 1) to y, bottomY, cache) + getFirstSplitterUnderPart2(splitterHitMap, (start.x() + 1) to y, bottomY, cache)
                cache[start] = res
                println("${cache.size} / 1749")
                return res
            }
            y++
        }
        return 0
    }
}

