fun day03() {
    fun part1(instructions: String): Int {
        val muls = "mul\\((?<digit1>\\d+),(?<digit2>\\d+)\\)".toRegex().findAll(instructions)
        return muls.sumOf { it.groups["digit1"]!!.value.toInt() * it.groups["digit2"]!!.value.toInt() }
    }

    fun part2(instructions: String): Int {
        val muls = "mul\\((?<digit1>\\d+),(?<digit2>\\d+)\\)".toRegex().findAll(instructions).toList()
        val dos = "do\\(\\)".toRegex().findAll(instructions).toList()
        val donts = "don't\\(\\)".toRegex().findAll(instructions).toList()
        return muls
            .filter { mul ->
                val start = mul.range.start
                val previousDo = dos.findLast { it.range.start < start }
                val previousDont = donts.findLast { it.range.start < start }
                previousDont == null || (previousDo?.range?.start ?: 0) > previousDont.range.start
            }
            .sumOf { it.groups["digit1"]!!.value.toInt() * it.groups["digit2"]!!.value.toInt() }
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInputAsText("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    // Read the input from the `src/Day03.txt` file.
    val input = readInputAsText("Day03")
    println(part1(input))
    println(part2(input))
}
