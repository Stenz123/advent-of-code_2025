package dev.stenz.algorithms.coordinate

import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Top y-1 ↑ | Bottom y+1 ↓ | Left x-1 ← | Right x+1 →
 */
typealias Coord = Pair<Int, Int>

fun Coord(str: String, delimiter: String): Coord =
    str.split(delimiter).map { it.trim().toInt() }.let { (x, y) -> x to y }

fun Coord.x() = first
fun Coord.y() = second

fun Coord.top() = Coord(x(), y() - 1)
fun Coord.bottom() = Coord(x(), y() + 1)
fun Coord.left() = Coord(x() - 1, y())
fun Coord.right() = Coord(x() + 1, y())

fun Coord.topRight() = Coord(x() + 1, y() - 1)
fun Coord.bottomLeft() = Coord(x() - 1, y() + 1)
fun Coord.topLeft() = Coord(x() - 1, y() - 1)
fun Coord.bottomRight() = Coord(x() + 1, y() + 1)

fun Coord.isInBounds(xBounds: IntRange, yBounds: IntRange) = x() in xBounds && y() in yBounds

fun Coord.get8Neighbours() = listOf(
    top(),
    bottom(),
    right(),
    left(),
    topLeft(),
    topRight(),
    bottomLeft(),
    bottomRight(),
)

fun Coord.get4Neighbours() = listOf(
    top(),
    bottom(),
    right(),
    left()
)

fun calculateAngle(a: Coord, b: Coord, c: Coord): Double {
    val ba = doubleArrayOf((a.x() - b.x()).toDouble(), (a.y() - b.y()).toDouble()) // vector b to a
    val bc = doubleArrayOf((c.x() - b.x()).toDouble(), (c.y() - b.y()).toDouble()) // vector b to c

    val dotProduct = ba[0] * bc[0] + ba[1] * bc[1]
    val magnitudeBa = sqrt(ba[0].pow(2) + ba[1].pow(2))
    val magnitudeBc = sqrt(bc[0].pow(2) + bc[1].pow(2))

    return Math.toDegrees(acos(dotProduct / (magnitudeBa * magnitudeBc)))
}

/*
* 0 --> p, q and r are collinear
* 1 --> Clockwise
* -1 --> Counterclockwise
*/
fun orientation(p1: Coord, p2: Coord, p3: Coord): Int {
    val slope = (p2.y() - p1.y()) * (p3.x() - p2.x()) - (p2.x() - p1.x()) * (p3.y() - p2.y())
    return when {
        slope == 0 -> 0 // collinear
        slope > 0 -> 1 // clockwise
        else -> -1 // counterclockwise
    }
}
