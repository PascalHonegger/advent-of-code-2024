import kotlin.time.measureTime

fun day10() {
    fun countPathsToTops(topographicMap: Map<Pair<Int, Int>, Int>, pos: Pair<Int, Int>, distinct: Boolean): Int {
        val visitedTops: MutableCollection<Pair<Int, Int>> = if (distinct) mutableSetOf() else mutableListOf()
        fun countPathsToTops(pos: Pair<Int, Int>, prevValue: Int) {
            val value = topographicMap[pos]
            when {
                value == null || value != prevValue + 1 -> Unit
                value == 9 -> visitedTops.add(pos)
                else -> {
                    countPathsToTops(pos + Pair(1, 0), value)
                    countPathsToTops(pos + Pair(0, 1), value)
                    countPathsToTops(pos + Pair(-1, 0), value)
                    countPathsToTops(pos + Pair(0, -1), value)
                }
            }
        }

        countPathsToTops(pos + Pair(1, 0), 0)
        countPathsToTops(pos + Pair(0, 1), 0)
        countPathsToTops(pos + Pair(-1, 0), 0)
        countPathsToTops(pos + Pair(0, -1), 0)
        return visitedTops.size
    }

    fun part1(topographicMap: Map<Pair<Int, Int>, Int>): Int {
        return topographicMap.entries.sumOf { (pos, v) ->
            if (v != 0) return@sumOf 0

            countPathsToTops(topographicMap, pos, distinct = true)
        }
    }

    fun part2(topographicMap: Map<Pair<Int, Int>, Int>): Int {
        return topographicMap.entries.sumOf { (pos, v) ->
            if (v != 0) return@sumOf 0

            countPathsToTops(topographicMap, pos, distinct = false)
        }
    }

    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test").toPointMap().mapValues { it.value.digitToInt() }
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10").toPointMap().mapValues { it.value.digitToInt() }
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
