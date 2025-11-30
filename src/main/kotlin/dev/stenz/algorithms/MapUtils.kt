package dev.stenz.algorithms.coordinate

import dev.stenz.algorithms.uti.ConsoleColors
import java.io.Console
import kotlin.coroutines.Continuation
import kotlin.math.max

fun arrayMapFromRawString(data: String): Array<Array<String>> = arrayMapFromStringList(data.split("\n"))

fun arrayMapFromStringList(data: List<String>): Array<Array<String>> =
    Array(data.size) { row ->
        Array(data[row].length) { col ->
            data[row][col].toString()
        }
    }

fun mapFromStringList(data: List<String>, filter: (c: Char) -> Boolean = { true }): MutableMap<Coord, String> {
    val map = mutableMapOf<Coord, String>()
    for (y in data.indices) {
        for (x in data[y].indices) {
            if (filter(data[y][x])) {
                map[x to y] = data[y][x].toString()
            }
        }
    }
    return map
}

fun listFromStringList(data: List<String>, positiveChars: List<Char>): MutableList<Coord> {
    val list = mutableListOf<Coord>()
    for (y in data.indices) {
        for (x in data[y].indices) {
            if (data[y][x] in positiveChars) {
                list.add(x to y)
            }
        }
    }
    return list
}

private fun drawXNumbers(range: IntRange, xOffset: Int) {
    val numbers = range.map { it.toString() }
    val maxLength = max(numbers.first().length, numbers.last().length)
    val paddedNumbers = numbers.map { it.padStart(maxLength) }
    for (i in 0..<maxLength) {
        if (i == maxLength - 1) {
            print(" ".repeat(xOffset+1)+ ConsoleColors.YELLOW+"X"+ConsoleColors.RESET)
        } else {
            print(" ".repeat(xOffset+2))
        }
        paddedNumbers.forEachIndexed { j, it ->
            print(if (j % 2 == 0) ConsoleColors.CYAN_BRIGHT else ConsoleColors.CYAN)
            print(it[i])
        }
        println(ConsoleColors.RESET)
    }
    println("${" ".repeat(xOffset-1)}${ConsoleColors.YELLOW}Y ${ConsoleColors.BLACK_BRIGHT}+${"-".repeat(numbers.size)}${ConsoleColors.RESET}")
}

private fun drawYNumber(number: Int, maxLength: Int) =
    print("${ConsoleColors.CYAN}${number.toString().padStart(maxLength - number.toString().length + 1)} ${ConsoleColors.BLACK_BRIGHT}|${ConsoleColors.RESET}")

fun Map<Coord, String>.draw(negativeChar: Char = ' ') {
    val minX = this.keys.minOf(Coord::x)
    val maxX = this.keys.maxOf(Coord::x)
    val minY = this.keys.minOf(Coord::y)
    val maxY = this.keys.maxOf(Coord::y)

    val maxXNumberCharCount = maxOf(minX.toString().length, maxX.toString().length)
    val maxYNumberCharCount = maxOf(minY.toString().length, maxY.toString().length)

    drawXNumbers(minX..maxX, maxYNumberCharCount)
    for (y in (minY..maxY)) {
        drawYNumber(y, maxYNumberCharCount)
        for (x in (minX..maxX)) {
            if (containsKey(x to y)) print(this[x to y])
            else print(negativeChar)
        }
        println()
    }
}

fun List<Coord>.draw(positiveChar: Char = 'X', negativeChar: Char = ' ') {
    val minX = this.minOf(Coord::x)
    val maxX = this.maxOf(Coord::x)
    val minY = this.minOf(Coord::y)
    val maxY = this.maxOf(Coord::y)

    val maxXNumberCharCount = maxOf(minX.toString().length, maxX.toString().length)
    val maxYNumberCharCount = maxOf(minY.toString().length, maxY.toString().length)

    drawXNumbers(minX..maxX, maxYNumberCharCount)
    for (y in (minY..maxY)) {
        drawYNumber(y, maxYNumberCharCount)
        for (x in (minX..maxX)) {
            if (this.contains(x to y)) print(positiveChar)
            else print(negativeChar)
        }
        println()
    }
}

fun <T> Array<Array<T>>.draw(mapper: ((T) -> Any)? = null) {
    val maxYNumberCharCount = this.size.toString().length
    drawXNumbers(0..this.size, maxYNumberCharCount)
    for (y in this.indices) {
        drawYNumber(y, maxYNumberCharCount)
        for (x in this[y].indices) {
            if (mapper != null) {
                print(mapper(this[y][x]))
            } else {
                print(this[y][x])
            }
        }
        println()
    }
}

/*
* Returns a list of the coordinates that is surrounded by a border.
* If the start point is in the map, every point from the map is going to be filled, else the map is going to be the border.
*/
fun basicFill(
    map: List<Coord>,
    startPoint: Coord,
    isInBounds: (Coord) -> Boolean = { true },
    fill8Directions: Boolean = false
): Set<Coord> {
    val inScopeSubject: (Coord) -> Boolean =
        if (startPoint in map) { c -> c in map } else { c -> c !in map && isInBounds(c) }
    val getNeighbours = if (fill8Directions) { c: Coord -> c.get8Neighbours() } else { c -> c.get4Neighbours() }

    val result = hashMapOf(startPoint to true)
    val queue = ArrayDeque<Coord>()
    queue.add(startPoint)
    while (queue.isNotEmpty()) {
        val curr = queue.removeLast()
        val neighbours = getNeighbours(curr)
            .filter { inScopeSubject(it) }
            .filter { it !in result }
        result.putAll(neighbours.map { it to true })
        queue.addAll(neighbours)
    }
    return result.map { it.key }.toSet()
}

