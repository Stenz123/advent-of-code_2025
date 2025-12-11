package days.day10

import days.Day
import org.chocosolver.solver.Model
import org.chocosolver.solver.search.strategy.Search
import org.chocosolver.solver.variables.IntVar

class Day10 : Day(false) {
    override fun partOne(): Any {
        val (indicatorLights, lineButtons) = parse()

        return indicatorLights.mapIndexedNotNull pressLoop@{ index, indicatorLight ->
            val buttons = lineButtons[index].map { parseButton(it) }

            val queue: ArrayDeque<LightWrapper> = ArrayDeque()
            queue.add(LightWrapper(indicatorLight.press(indicatorLight)))
            while (queue.isNotEmpty()) {
                val currLight = queue.removeFirst()
                queue.addAll(buttons.map { button ->
                    if (!button.contentEquals(currLight.buttonHist.lastOrNull())) continue
                    val newLightState = currLight.light.press(button)
                    if (newLightState.contentEquals(indicatorLight)) return@pressLoop currLight.buttonHist.size + 1
                    LightWrapper(newLightState, currLight.buttonHist + button)
                })
            }
            null
        }.sum()
    }

    override fun partTwo(): Any {
        val (_, lineButtons, joltages) = parse()

        return lineButtons.mapIndexed { i, buttons ->
            val joltage: List<Int> = joltages[i]

            val maxPresses = joltage.max()
            val model = Model()
            val variables = buttons.mapIndexed { buttonIndex, _ ->
                model.intVar(buttonIndex.toString(), 0, maxPresses)
            }

            joltage.forEachIndexed { joltageIndex, joltageWanted ->
                val vars = buttons
                    .mapIndexedNotNull { index, b -> index.takeIf { joltageIndex in b } }
                    .map { variables[it] }
                    .toTypedArray()

                val coefficients = IntArray(vars.size) { 1 }
                model.scalar(vars, coefficients, "=", joltageWanted).post()
            }

            val totalSum: IntVar = model.intVar("totalSum", 0, joltage.sum())
            model.sum(variables.toTypedArray(), "=", totalSum).post()
            model.setObjective(Model.MINIMIZE, totalSum)

            model.solver.setSearch(Search.minDomLBSearch(*variables.toTypedArray()))

            var ans = 0
            while (model.solver.solve()) {
                ans = variables.sumOf { it.value }
            }
            ans
        }.sum()
    }

    data class LightWrapper(
        val light: BooleanArray,
        val buttonHist: List<BooleanArray> = listOf(),
    )

    fun parse(): Triple<List<BooleanArray>, List<List<List<Int>>>, List<List<Int>>> {
        val indicatorLights: List<BooleanArray> = readInput().map { line ->
            line.substringAfter("[")
                .substringBefore("]")
                .toCharArray()
                .map { it == '#' }
                .toBooleanArray()
        }

        val lineButtons: List<List<List<Int>>> = readInput().map { line ->
            line.substringAfter("]")
                .substringBefore("{")
                .trim()
                .split(" ")
                .map { button ->
                    button.trim('(', ')')
                        .split(",")
                        .map(String::toInt)
                }
        }

        val joltage: List<List<Int>> = readInput().map { line ->
            line.substringAfter("{")
                .substringBefore("}")
                .split(",")
                .map(String::toInt)
        }

        return Triple(indicatorLights, lineButtons, joltage)
    }

    fun parseButton(rawButton: List<Int>): BooleanArray =
        BooleanArray(rawButton.last() + 1) { it in rawButton }

    fun BooleanArray.press(button: BooleanArray): BooleanArray =
        BooleanArray(size) { i -> this[i] xor button[i] }

}