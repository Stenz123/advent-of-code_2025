package days.day12

import com.sun.org.apache.xpath.internal.operations.Bool
import days.Day
import dev.stenz.algorithms.coordinate.arrayMapFromStringList

class Day12: Day(true) {
    override fun partOne(): Any {
        val presents = parsePresents(readInput())
        val presentAreas = /*presents.map { it.sumOf { it.count { it } } }*/ Array(6){9}

        return readInput().takeLastWhile { it.isNotEmpty() }.count { line ->
            val (w, h) = line.substringBefore(":").split("x").map(String::toInt)
            val presentAmounts = line.substringAfter(": ").split(" ").map(String::toInt).toTypedArray()

            val entirePresentArea = presentAmounts.reduceIndexed { index, acc, _ ->
                acc + presentAmounts[index] * presentAreas[index]
            }
            w*h >= entirePresentArea
        }
    }

    override fun partTwo(): Any {
        return "Finish"
    }

    fun parsePresents(lines: List<String>): Array<Array<BooleanArray>> {
        val presents: Array<Array<BooleanArray>> = Array(6) { arrayOf() }
        var id = 0
        for (i in 1..29 step 5) {
            presents[id++] = arrayMapFromStringList(lines.subList(i, i + 3))
                .map { it.map { it == "#" }.toBooleanArray() }
                .toTypedArray()
        }
        return presents
    }
}

