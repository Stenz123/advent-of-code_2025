package days.day2

import days.Day
import dev.stenz.algorithms.util.printlnRed
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class Day2 : Day(false) {
    override fun partOne(): Any {
        return readInput().first()
            .split(",")
            .map {
                val parts = it.split("-").map(String::toLong)
                parts.first() to parts.last()
            }.map { pair ->
                //println("${pair.first}-${pair.second}")
                var num = pair.first
                val res = mutableListOf<Long>()
                while (num <= pair.second) {
                    val length = num.length()
                    if (length % 2 == 1) {
                        num = (num.getAt(0, length) + 1L) * 10.0.pow(length - 1).toLong()
                        continue
                    }
                    val mid = length / 2
                    var i = 0
                    while (num.getAt(i, length) == num.getAt(mid + i)) {
                        if (i == mid - 1) {
                            res.add(num) //this is a fake id heheheheheh
                            //printlnRed("FAKE: $num")
                            break
                        }
                        i++
                    }
                    val diff = num.getAt(i, length) - num.getAt(mid + i)
                    if (diff <= 0) num++
                    else num++//= (diff * 10.0.pow(length - (mid + i) -1)).toLong()
                }
                res
            }.flatten().sum()
    }

    override fun partTwo(): Any {
        val mem = hashMapOf<Int, List<Int>>()
        return readInput().first()
            .split(",")
            .map {
                val parts = it.split("-").map(String::toLong)
                parts.first() to parts.last()
            }.map { pair ->
                println("${pair.first}-${pair.second}")
                var num = pair.first
                val res = mutableListOf<Long>()
                while (num <= pair.second) {
                    val length = num.length()
                    val factors = factorsOfNumber(length, mem)
                    if (length > 1) {
                        numberCheck@ for (factor in factors) {
                            val secSize = length / factor
                            if (checkNumLegit(num, secSize, length)) {
                                res.add(num)
                                printlnRed("$num")
                                break@numberCheck
                            }
                        }
                    }
                    num++
                }
                res
            }.flatten().sum()
    }

    fun checkNumLegit(num: Long, secSize: Int, length: Int): Boolean {
        val numberOfSecs = length / secSize
        for (i in 0..<numberOfSecs - 1) {
            for (index in 0..<secSize) {
                if (num.getAt(index + secSize * i) != num.getAt(index + secSize * (i + 1))) {
                    return false
                }
            }
        }
        return true
    }

    fun Long.length(): Int = when (this) {
        0L -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }

    fun Long.getAt(i: Int, length: Int? = null): Long =
        floor(this / 10.0.pow((length ?: this.length()) - i - 1.0) % 10).toLong()

    fun factorsOfNumber(num: Int, mem: HashMap<Int, List<Int>>): List<Int> {
        if (mem.containsKey(num)) return mem[num]!!
        val factors = mutableListOf<Int>()
        if (num < 1)
            return factors
        (1..num / 2)
            .filter { num % it == 0 }
            .filter { it != 1 }
            .forEach { factors.add(it) }
        factors.add(num)
        mem[num] = factors.toList()
        return factors
    }
}

