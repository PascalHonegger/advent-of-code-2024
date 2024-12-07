import kotlinx.coroutines.*
import kotlin.time.measureTime

@OptIn(DelicateCoroutinesApi::class)
fun day07() {
    data class Equation(val solution: Long, val parts: List<Long>)

    fun solveIt(equations: List<Equation>, withPipeOperator: Boolean): Long = runBlocking {
        equations
            .map { eq ->
                val workingSet = ArrayDeque(eq.parts)

                fun performCalculation() {
                    if (workingSet.size == 1) {
                        return
                    }
                    val a = workingSet.removeFirst()
                    val b = workingSet.removeFirst()

                    fun performOperation(newValue: Long) {
                        if (newValue == eq.solution && workingSet.isEmpty()) {
                            error("found it!")
                        }

                        if (newValue >= eq.solution) {
                            // all operations only increase the value, no point in going down this path
                            return
                        }

                        workingSet.addFirst(newValue)
                        performCalculation()
                        workingSet.removeFirst()
                    }

                    // Try +
                    performOperation(a + b)

                    // Try *
                    performOperation(a * b)

                    // Try ||
                    if (withPipeOperator) {
                        performOperation("${a}${b}".toLong())
                    }

                    workingSet.addFirst(b)
                    workingSet.addFirst(a)
                }
                GlobalScope.async {
                    try {
                        performCalculation()
                    } catch (_: Exception) {
                        // Hacky way to early return
                        return@async Pair(eq.solution, true)
                    }
                    Pair(eq.solution, false)
                }
            }.awaitAll()
            .filter { it.second }
            .sumOf { it.first }
    }

    fun part1(equations: List<Equation>): Long = solveIt(equations, withPipeOperator = false)

    fun part2(equations: List<Equation>): Long = solveIt(equations, withPipeOperator = true)

    fun List<String>.toEquations() = map {
        val (solution, parts) = it.split(": ")
        Equation(solution.toLong(), parts.asSpaceSeparatedLongs())
    }

    // Or read a large test input from the `src/Day07_test.txt` file:
    val testInput = readInput("Day07_test").toEquations()
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07").toEquations()
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
