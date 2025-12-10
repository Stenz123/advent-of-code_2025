package days.day10

import days.Day
import org.chocosolver.solver.Model
import org.chocosolver.solver.search.strategy.Search
import org.chocosolver.solver.variables.IntVar

class Day10 : Day(false) {
    override fun partOne(): Any {
        val (indicatorLights, lineButtons) = parse()

        val res = indicatorLights.mapIndexedNotNull pressLoop@ { index, indicatorLight ->
            val buttons = lineButtons[index].map { parseButton(it) }

            //CurrentState, Last Button, PressCount
            val queue: ArrayDeque<LightWrapper> = ArrayDeque()
            queue.add(LightWrapper(indicatorLight.press(indicatorLight).first))
            //buttons.forEach { queue.add(it to it) }

            while (true) {
                val currLight = queue.removeFirst()
                queue.addAll(buttons.mapNotNull { button ->
                    if (!button.contentEquals(currLight.buttonHist.lastOrNull())) {
                        val newLightState = currLight.light.press(button).first
                        if (newLightState.contentEquals(indicatorLight)){

                            (currLight.buttonHist + button) .forEach { _ -> println(button.decode()+" ") }

                            return@pressLoop currLight.buttonHist.size + 1
                        }
                        return@mapNotNull LightWrapper(newLightState, currLight.buttonHist + button)
                    }
                    return@mapNotNull null
                })
            }
            null

        }
        return res.sum()
    }

    data class LightWrapper(
        val light: IndicatorLight,
        val buttonHist: List<Button> = listOf(),
        val joltage: Joltage? = null
    )

    override fun partTwo(): Any {
        val (_, lineButtons, joltages) = parse()

        return lineButtons.mapIndexed { i, buttons ->
            val joltage = joltages[i]

            val maxPresses = joltage.max()
            val model = Model()
            val variables = buttons.mapIndexed { buttonIndex, button ->
                model.intVar(buttonIndex.toString(), 0, maxPresses)
            }

            joltage.forEachIndexed { joltageIndex, joltageWanted ->
                val vars = buttons.mapIndexedNotNull {i,b -> i.takeIf {joltageIndex in b  }}.map{variables[it]}.toTypedArray()
                val coeficients = IntArray(vars.size){1}
                model.scalar(vars, coeficients, "=", joltageWanted).post()
            }

            val totalSum: IntVar = model.intVar("totalSum", 0, joltage.sum())
            model.sum(variables.toTypedArray(), "=", totalSum).post()
            model.setObjective(Model.MINIMIZE, totalSum)

            model.solver.setSearch(Search.minDomLBSearch(*variables.toTypedArray()))

            var ans = 0
            while(model.solver.solve()){
                ans = variables.sumOf { it.value }
            }
            ans

        }.sum()

    }

    fun parse(): Triple<List<BooleanArray>, List<List<List<Int>>>, List<Joltage>> {
        val indicatorLights = readInput().map { line ->
            line.substringAfter("[").substringBefore("]").toCharArray().map { it == '#' }.toBooleanArray()
        }

        val lineButtons = readInput().map { line ->
            line.substringAfter("]").substringBefore("{").trim().split(" ").map { button ->
                button.trim('(', ')').split(",").map(String::toInt)
            }
        }

        val joltage = readInput().map { line ->
            line.substringAfter("{").substringBefore("}").split(",").map(String::toInt)
        }

        return Triple(indicatorLights, lineButtons, joltage)
    }

    //-Xnested-type-aliases //Compiler argument
    typealias IndicatorLight = BooleanArray
    typealias Button = BooleanArray
    typealias Joltage = List<Int>

    fun parseButton(rawButton: List<Int>) = BooleanArray(rawButton.last()+1) { it in rawButton }

    fun IndicatorLight.press(button: Button, joltage: Joltage? = null): Pair<IndicatorLight, Joltage?> {
        val res = this.clone()
        val joltageRes = joltage?.toMutableList()
        button.forEachIndexed { index, bool ->
            if (bool) {
                res[index] = !this[index]
                if (joltageRes != null) joltageRes[index]++
            }
        }
        return res to joltageRes
    }

    fun BooleanArray.decode(): String {
        val res = mutableListOf<Int>()
        this.forEachIndexed{ index, bool ->
            if (bool)res.add(index)
        }

        return "(${res.joinToString(", ")})"

    }
}

