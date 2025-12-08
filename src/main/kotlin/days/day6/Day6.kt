package days.day6

import days.Day

class Day6: Day(false) {
    override fun partOne(): Any {
        val rawInput = readInput()
        val parsedNumbers = rawInput.subList(0, rawInput.size-1).map {
            it.split(" ").filter(String::isNotBlank)
        }.map { it.map(String::toLong) }

        val operations = rawInput.last()
            .split(" ")
            .filter(String::isNotBlank)

        val equasions: MutableList<Pair<List<Long>, Char>> = mutableListOf()

        for (i in parsedNumbers.first().indices) {
            val numberList = mutableListOf<Long>()
            for (list in parsedNumbers){
                numberList.add(list[i])
            }
            equasions.add(numberList to operations[i][0])
        }

        return equasions.sumOf { eq ->
            if (eq.second == '*') eq.first.reduce(Long::times)
            else eq.first.sum()
        }
    }

    override fun partTwo(): Any {
        val lineSize = readInput().maxOf { it.length }
        val rawInput = readInput().map { it.padEnd(lineSize, ' ') }

        var res = 0L
        val lastLineIndex = rawInput.size-1
        val stringBuilder = StringBuilder()
        var nums = mutableListOf<Long>()
        var operation: Char? = null
        var col = lineSize - 1
        while (col >= 0) {
            for (line in rawInput.indices) {
                val char = rawInput[line][col]
                if (line == lastLineIndex && char != ' ') {
                    operation = char
                } else {
                    stringBuilder.append(char)
                }
            }
            nums.add(stringBuilder.toString().trim().toLong())
            stringBuilder.clear()
            if (operation != null) {
                res += if (operation == '*') nums.reduce(Long::times)
                    else nums.sum()
                operation = null
                nums = mutableListOf()
                col--
            }
            col--
        }


        return res
    }
}

