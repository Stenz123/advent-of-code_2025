package dev.stenz.algorithms.coordinate

import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Top y-1 ↑ | Bottom y+1 ↓ | Left x-1 ← | Right x+1 →
 */
typealias Coord3d = Triple<Int, Int, Int>

fun Coord3d(str: String, delimiter: String): Coord3d =
    str.split(delimiter).map { it.trim().toInt() }.let { (x, y, z) -> Coord3d(x, y, z) }

fun Coord3d.x() = first
fun Coord3d.y() = second
fun Coord3d.z() = third

fun Coord3d.distanceTo(p: Coord3d) =
    sqrt(
        (this.x()-p.x()).toDouble().pow(2) +
        (this.y()-p.y()).toDouble().pow(2) +
        (this.z()-p.z()).toDouble().pow(2)
    )
