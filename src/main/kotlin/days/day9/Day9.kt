package days.day9

import days.Day
import dev.stenz.algorithms.allPairs
import dev.stenz.algorithms.containedByAny
import dev.stenz.algorithms.coordinate.Coord
import dev.stenz.algorithms.coordinate.x
import dev.stenz.algorithms.coordinate.y
import dev.stenz.algorithms.mergeAll
import dev.stenz.algorithms.rangeToEither
import kotlin.math.abs

class Day9 : Day(false) {
    override fun partOne(): Any {
        val coords = readInput().map { Coord(it, ",") }
        return coords.maxOf { p1 ->
            coords.maxOf { p2 ->
                area(p1, p2)
            }
        }
    }

    override fun partTwo(): Any {
        val redTiles = readInput().map { Coord(it, ",") }

        val tiles = mutableMapOf<Coord, String>()
        fun addBorder(i1: Int, i2: Int) {
            val x1 = redTiles[i1].x()
            val y1 = redTiles[i1].y()
            val x2 = redTiles[i2].x()
            val y2 = redTiles[i2].y()
            if (x1 == x2) {
                for (y in y1.rangeToEither(y2)) tiles[x1 to y] = "X"
            } else {
                for (x in x1.rangeToEither(x2)) tiles[x to y1] = "X"
            }
        }
        for (i in 0 until redTiles.size - 1) {
            addBorder(i, i + 1)
        }
        addBorder(0, redTiles.size-1)

        redTiles.forEach {
            tiles[it] = "#"
        }

        fun buildRanges(
            keySelector: (Coord) -> Int,
            valueSelector: (Coord) -> Int,
            coordBuilder: (Int, Int) -> Pair<Int, Int>
        ): MutableMap<Int, MutableList<IntRange>> {
            val ranges = mutableMapOf<Int, MutableList<IntRange>>()

            tiles.keys.groupBy(keySelector).forEach { (key, group) ->
                val rangeList = mutableListOf<IntRange>()
                var index = 0
                var isPrevRed = false

                group.map(valueSelector).sorted().forEach { value ->
                    if (isPrevRed) index = 1
                    if (index % 2 == 0) {
                        rangeList.add(value..value)
                    } else {
                        rangeList.add(rangeList.removeLast().expandBy(value))
                    }
                    isPrevRed = redTiles.contains(coordBuilder(key, value))
                    index++
                }

                ranges[key] = mergeAll(rangeList).toMutableList()
            }

            return ranges
        }

        val yRanges = buildRanges(
            keySelector = { it.x() },
            valueSelector = { it.y() },
            coordBuilder = { x, y -> x to y }
        )

        return allPairs(redTiles)
            .sortedBy { -area(it.first, it.second) }
            .find { (p1, p2) ->
                p1.y().rangeToEither(p2.y()).containedByAny(yRanges[p1.x()]!!) &&
                        p1.y().rangeToEither(p2.y()).containedByAny(yRanges[p2.x()]!!)
            }.let { p -> area(p!!.first, p.second) }
    }


    private fun area(p1: Coord, p2: Coord): Long = (abs(p1.x() - p2.x()) + 1L) * (abs(p1.y() - p2.y()) + 1L)

    private fun IntRange.expandBy(num: Int) = if (num !in this) {
        if (num > this.last) this.first..num
        else num..this.last
    } else this.first..this.last

    private fun getBorderOfRectangle(p1: Coord, p2: Coord): List<Coord> {
        val res = mutableListOf<Coord>()

        res.addAll(p1.y().rangeToEither(p2.y()).flatMap { y ->
            listOf(
                p1.x() to y,
                p2.x() to y,
            )
        })
        res.addAll(p1.x().rangeToEither(p2.x()).flatMap { x ->
            listOf(
                x to p1.y(),
                x to p2.y(),
            )
        })
        return res
    }

}

