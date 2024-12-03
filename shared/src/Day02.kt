fun day02() {
    fun List<Int>.isSafe(): Boolean {
        val diffs = zipWithNext().map { it.second - it.first }
        return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
    }

    fun part1(levels: List<String>): Int {
        return levels.map { it.asSpaceSeparatedInts() }.count { it.isSafe() }
    }

    fun part2(levels: List<String>): Int {
        return levels.map { it.asSpaceSeparatedInts() }.count { level ->
            level.indices.any { numToIgnoreIndex ->
                level.filterIndexed { index, _ -> index != numToIgnoreIndex }.isSafe()
            }
        }
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
