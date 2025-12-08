package days.day8

import days.Day
import dev.stenz.algorithms.coordinate.Coord3d
import dev.stenz.algorithms.coordinate.distanceTo
import dev.stenz.algorithms.coordinate.x
import dev.stenz.algorithms.uti.ConsoleColors

class Day8 : Day(true) {
    override fun partOne(): Any {
        val boxes = readInput().map { Coord3d(it, ",") }

        val circuits = boxes.map { Circuit(mutableListOf(it)) }.toMutableList()

        val times = if (this.useExampleInput) 10 else 1000

        val connections = mutableMapOf<Pair<Coord3d, Coord3d>, Boolean>()

        repeat(times) {
            if (it % 100 == 0) print("${it/10}% ")
            getLeastDistanceAndConnect(circuits, connections)
        }
        println("100%")

        val sortedCircuit = circuits.sortedBy { -it.boxes.size }
        return sortedCircuit[0].boxes.size * sortedCircuit[1].boxes.size * sortedCircuit[2].boxes.size
    }

    override fun partTwo(): Any {
        val boxes = readInput().map { Coord3d(it, ",") }

        val circuits = boxes.map { Circuit(mutableListOf(it)) }.toMutableList()

        val connections = mutableMapOf<Pair<Coord3d, Coord3d>, Boolean>()

        var i = 0

        while (true){
            i++
            if (i % 100 == 0) println("${circuits.size}")
            val connection = getLeastDistanceAndConnect(circuits, connections)
            if (circuits.size == 1) {
                return connection.first.x() * connection.second.x()
            }
        }
    }

    data class Circuit(
        val boxes: MutableList<Coord3d>
    ) {
        fun merge(c: Circuit) {
            this.boxes.addAll(c.boxes)
        }

        override fun toString(): String {
            return "${this.boxes.size}: ${boxes.joinToString(", ")}"
        }
    }

    fun getLeastDistanceAndConnect(
        circuits: MutableList<Circuit>,
        connections: MutableMap<Pair<Coord3d, Coord3d>, Boolean>,
        printConnection: Boolean = false
    ): Pair<Coord3d, Coord3d> {
        var leastDistance: Double = Double.MAX_VALUE
        var b1: Coord3d? = null
        var b2: Coord3d? = null
        var c1: Circuit? = null
        var c2: Circuit? = null
        circuits.forEach { circuit ->
            circuit.boxes.forEach { box ->
                circuits.forEach { circuit2 ->
                    circuit2.boxes.forEach { box2 ->
                        if (box != box2) {
                            val distance = box.distanceTo(box2)
                            if (distance < leastDistance && !connections.containsKey(box to box2) && !connections.containsKey(box2 to box)) {
                                leastDistance = distance
                                b1 = box
                                b2 = box2
                                c1 = circuit
                                c2 = circuit2
                            }
                        }
                    }
                }
            }
        }

        if(printConnection)println("B1: ${ConsoleColors.BLUE_BOLD}$b1${ConsoleColors.RESET} B2: ${ConsoleColors.BLUE_BOLD}$b2${ConsoleColors.RESET} Distance: $leastDistance")
        if (c1 != c2) {
            c1!!.merge(c2!!)
            circuits.remove(c2)
        }
        connections[b1!! to b2!!] = true
        return b1!! to b2!!
    }

}

