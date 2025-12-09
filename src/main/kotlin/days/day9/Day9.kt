package days.day9

import days.Day
import dev.stenz.algorithms.coordinate.Coord
import dev.stenz.algorithms.coordinate.draw
import dev.stenz.algorithms.coordinate.x
import dev.stenz.algorithms.coordinate.y
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day9 : Day(false) {
    override fun partOne(): Any {
        val coords = readInput().map { line ->
            Coord(line, ",")
        }
        return coords.maxOf { p1 ->
            coords.maxOf { p2 ->
                area(p1, p2)
            }
        }
    }

    override fun partTwo(): Any {
        val redTiles = readInput().map { line ->
            Coord(line, ",")
        }
        val tiles = mutableMapOf<Coord, String>()
        for (i in 0 until redTiles.size - 1) {
            val x1 = redTiles[i].x()
            val y1 = redTiles[i].y()
            val x2 = redTiles[i + 1].x()
            val y2 = redTiles[i + 1].y()
            if (x1 == x2) {
                for (y in y1.rangeToEither(y2)) tiles[x1 to y] = "X"
            } else {
                for (x in x1.rangeToEither(x2)) tiles[x to y1] = "X"
            }
        }
        val x1 = redTiles[0].x()
        val y1 = redTiles[0].y()
        val x2 = redTiles.last().x()
        val y2 = redTiles.last().y()
        if (x1 == x2) {
            for (y in y1.rangeToEither(y2)) tiles[x1 to y] = "X"
        } else {
            for (x in x1.rangeToEither(x2)) tiles[x to y1] = "X"
        }


        redTiles.forEach {
            tiles[it] = "#"
        }
        val xRanges = mutableMapOf<Int, MutableList<IntRange>>()
        val yRanges = mutableMapOf<Int, MutableList<IntRange>>()

        /*tiles.keys.forEach { coord ->
            val x = coord.x()
            val y = coord.y()

            xRanges[y] = xRanges[y]?.expandBy(x) ?: (x..x)
            yRanges[x] = yRanges[x]?.expandBy(y) ?: (y..y)
        }*/


        //Y Ranges
        tiles.keys.groupBy { it.x() }.forEach { (x, coords) ->
            yRanges[x] = mutableListOf()
            var index = 0
            var isPrevRed = false
            coords.map { it.y() }.sorted().forEach { y ->
                if (isPrevRed) index = 1
                if (index % 2 == 0) {
                    yRanges[x]!!.add(y..y)
                } else {
                    yRanges[x]!!.add(yRanges[x]!!.removeLast().expandBy(y))
                }
                isPrevRed = redTiles.contains(x to y)
                index++
            }
            yRanges[x] = mergeAll(yRanges[x]!!).toMutableList()
        }

        //X Ranges
        tiles.keys.groupBy { it.y() }.forEach { (y, coords) ->
            xRanges[y] = mutableListOf()
            var index = 0
            var isPrevRed = false
            coords.map { it.x() }.sorted().forEach { x ->
                if (isPrevRed) index = 1
                if (index % 2 == 0) {
                    xRanges[y]!!.add(x..x)
                } else {
                    xRanges[y]!!.add(xRanges[y]!!.removeLast().expandBy(x))
                }
                isPrevRed = redTiles.contains(x to y)
                index++
            }
            xRanges[y] = mergeAll(xRanges[y]!!).toMutableList()
        }

        //printAllXRanges with the numberd
        //printAllYRanges with the numberd
        if (this.useExampleInput) {

            tiles.draw(' ')
            println("------------")
            printXRanges(xRanges)
            println("------------")

            printXRanges(yRanges)
        }

        return redTiles.flatMap { p1 ->
            redTiles.map { p2 ->
                p1 to p2
            }

        }.sortedBy { -area(it.first, it.second) }.find { (p1, p2) ->
            p1.y().rangeToEither(p2.y()).containedByAny(yRanges[p1.x()]!!) &&
                    p1.y().rangeToEither(p2.y()).containedByAny(yRanges[p2.x()]!!)
            //  p1.x().rangeToEither(p2.x()).containedByAny(xRanges[p1.y()]!!) &&
            //  p1.x().rangeToEither(p2.x()).containedByAny(xRanges[p2.y()]!!)
        }.let { p -> area(p!!.first, p.second) }
    }

    fun Int.rangeToEither(a: Int): IntRange =
        min(this, a)..max(this, a)

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

    fun IntRange.containedBy(range: IntRange) = (this.first >= range.first && this.last <= range.last)
    fun IntRange.containedByAny(ranges: List<IntRange>) =
        ranges.any { this.containedBy(it) }

    fun mergeAll(ranges: List<IntRange>): List<IntRange> {
        if (ranges.isEmpty()) return emptyList()

        val sorted = ranges.sortedBy { it.first }
        val result = mutableListOf<IntRange>()

        var current = sorted.first()

        for (r in sorted.drop(1)) {
            val merged = current.merge(r)
            if (merged != null) {
                current = merged
            } else {
                result.add(current)
                current = r
            }
        }

        result.add(current)
        return result
    }

    fun IntRange.merge(that: IntRange): IntRange? {
        return if (this.last + 1 < that.first || that.last + 1 < this.first) {
            null
        } else {
            minOf(this.first, that.first)..maxOf(this.last, that.last)
        }
    }

    fun printXRanges(xRanges: Map<Int, List<IntRange>>) {
        if (xRanges.isEmpty()) return

        // Compute bounds
        val allXs = xRanges.values.flatten().flatMap { listOf(it.first, it.last) }
        val minX = allXs.min()
        val maxX = allXs.max()

        val allYs = xRanges.keys
        val minY = allYs.min()
        val maxY = allYs.max()

        for (y in minY..maxY) {
            val line = CharArray(maxX - minX + 1) { '.' }
            val ranges = xRanges[y]
            if (ranges != null) {
                for (r in ranges) {
                    for (x in r.first..r.last) {
                        line[x - minX] = '#'
                    }
                }
            }
            println("y=$y: ${String(line)}")
        }
    }

    fun printYRanges(yRanges: Map<Int, List<IntRange>>) {
        if (yRanges.isEmpty()) return

        val allYs = yRanges.values.flatten().flatMap { listOf(it.first, it.last) }
        val minY = allYs.min()
        val maxY = allYs.max()

        val allXs = yRanges.keys.sorted()

        for (x in allXs) {
            println("x=$x:")
            val column = CharArray(maxY - minY + 1) { '.' }
            val ranges = yRanges[x]
            if (ranges != null) {
                for (r in ranges) {
                    for (y in r.first..r.last) {
                        column[y - minY] = '#'
                    }
                }
            }

            for (i in column.indices) {
                val y = i + minY
                println("  y=$y ${column[i]}")
            }
            println()
        }
    }

}

