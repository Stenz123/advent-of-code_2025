package days.day3

import days.Day

class Day3 : Day(false) {
    override fun partOne(): Any {
        return readInput().sumOf { line ->
            val batteries = line.toCharArray().map(Character::getNumericValue)
            var firstDigit = batteries[0] to 0
            var secondDigit: Int = -1

            for (i in 0..<batteries.size - 1) if (batteries[i] > firstDigit.first) firstDigit = batteries[i] to i
            for (i in firstDigit.second + 1..<batteries.size) if (batteries[i] > secondDigit) secondDigit = batteries[i]

            //println("${firstDigit.first}$secondDigit".toInt())
            "${firstDigit.first}$secondDigit".toInt()
        }
    }

    override fun partTwo(): Any {
        return readInput().sumOf { line ->
            val batteries = line.toCharArray().map(Character::getNumericValue)
            val digits = mutableListOf<Int>()
            var lastIndex = -1
            for (digitsLeft in 11 downTo 0) {
                val digit = findDigit(batteries, lastIndex, digitsLeft)
                lastIndex = digit.second
                digits.add(digit.first)
            }
            digits.joinToString("").toLong()
        }
    }
    fun findDigit(batteries: List<Int>, startIndex: Int, digitsLeft: Int): Pair<Int, Int> {
        var max = -1 to -1
        for (i in startIndex+1..<batteries.size - digitsLeft) if (batteries[i]>max.first)max = batteries[i] to i
        return max

    }
}

