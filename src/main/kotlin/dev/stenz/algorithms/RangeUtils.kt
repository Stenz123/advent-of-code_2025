package dev.stenz.algorithms

import kotlin.math.max
import kotlin.math.min

fun Int.rangeToEither(a: Int): IntRange =
    min(this, a)..max(this, a)
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
