import kotlin.math.abs

fun day01() {
    fun part1(input: List<String>): Int {
        val ints = input.map { line -> line.split("   ").map { it.toInt() } }
        val lefts = ints.map { it.first() }.sorted()
        val rights = ints.map { it.last() }.sorted()

        return lefts.zip(rights).sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val ints = input.map { line -> line.split("   ").map { it.toInt() } }
        val lefts = ints.map { it.first() }.sorted()
        val rights = ints.map { it.last() }.groupingBy { it }.eachCount()

        return lefts.sumOf { it * (rights[it] ?: 0) }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    println(part2(input))
    println(part1(input))
}
