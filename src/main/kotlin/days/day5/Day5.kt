package days.day5

import days.Day

class Day5 : Day(false) {
    override fun partOne(): Any {
        val freshFoodRanges = readInput().takeWhile { it != "" }.map {
            val parts = it.split("-").map(String::toLong)
            parts.first()..parts.last()
        }

        return readInput().takeLastWhile { it != "" }.map(String::toLong).count { foodId ->
            freshFoodRanges.any { foodId in it }
        }
    }

    override fun partTwo() =
         mergeAll(readInput().takeWhile { it != "" }.map {
            val parts = it.split("-").map(String::toLong)
            parts.first()..parts.last()
        }).sumOf { it.last - it.first + 1 }

    fun mergeAll(ranges: List<LongRange>): List<LongRange> {
        if (ranges.isEmpty()) return emptyList()

        val sorted = ranges.sortedBy { it.first }
        val result = mutableListOf<LongRange>()

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

    fun LongRange.merge(that: LongRange): LongRange? {
        return if (this.last + 1 < that.first || that.last + 1 < this.first) {
            null
        } else {
            minOf(this.first, that.first)..maxOf(this.last, that.last)
        }
    }
}
