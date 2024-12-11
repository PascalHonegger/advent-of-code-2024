import kotlin.time.measureTime

fun day11() {
    fun blink(startNumbers: List<Long>, steps: Int): Long {
        val cache = mutableMapOf<Pair<Long, Int>, Long>()

        fun calculateIt(num: Long, remainingSteps: Int): Long {
            if (remainingSteps == 0) return 1L
            val key = Pair(num, remainingSteps)
            cache[key]?.let { return it }

            val nextSteps = remainingSteps - 1
            return when {
                num == 0L -> calculateIt(1L, nextSteps)
                num.toString().length % 2L == 0L -> num.toString().let { str ->
                    calculateIt(str.take(str.length / 2).toLong(), nextSteps) + calculateIt(str.drop(str.length / 2).toLong(), nextSteps)
                }
                else -> calculateIt(num * 2024L, nextSteps)
            }.also { cache[key] = it }
        }

        return startNumbers.sumOf { calculateIt(it, steps) }
    }

    fun part1(initial: List<Long>): Long = blink(initial, 25)

    fun part2(initial: List<Long>): Long = blink(initial, 75)

    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readInputAsText("Day11_test").asSpaceSeparatedLongs()
    check(part1(testInput) == 55312L)

    // Read the input from the `src/Day11.txt` file.
    val input = readInputAsText("Day11").asSpaceSeparatedLongs()
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
