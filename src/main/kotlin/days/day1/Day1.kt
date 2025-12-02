package days.day1

import days.Day
import dev.stenz.algorithms.uti.ConsoleColors
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round


class Day1 : Day(false) {
    override fun partOne(): Any {
        var dial = 50
        return readInput().count({ line ->
            val operation = if (line.first() == 'R') 1 else -1
            dial = (dial + line.substring(1, line.length).toInt() * operation) % 100
            dial == 0
        })
    }

    override fun partTwo(): Any {
        var dial = 50;
        return readInput().map { line ->
            val operation = if (line.first() == 'R') 1 else -1
            dial = (dial + line.substring(1, line.length).toInt() * operation)

            roundToZero(dial/100.0).let { res ->
                if (dial < 0){
                    val result = 0
                }
                return@map abs(res)
            }
        }.sum()
    }
}

fun roundToZero(d: Double): Double = if (d < 0) ceil(d) else floor(d)