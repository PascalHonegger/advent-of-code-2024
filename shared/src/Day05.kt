fun day05() {
    fun part1(input: String): Int {
        val orderRules = input.lines()
            .takeWhile { it.isNotEmpty() }
            .map { rules -> rules.split("|").map { it.toInt() } }
            .groupBy(
                keySelector = { it[0] },
                valueTransform = { it[1] }
            )

        val pages = input.lines()
            .takeLastWhile { it.isNotEmpty() }
            .map { pages -> pages.split(",").map { it.toInt() } }

        return pages
            .filter { pageNumbers ->
                pageNumbers.forEachIndexed { index, pageNum ->
                    val mustBeLaterNumbers = orderRules[pageNum] ?: return@forEachIndexed
                    val previousNumbers = pageNumbers.take(index).ifEmpty { return@forEachIndexed }
                    if (previousNumbers.any { it in mustBeLaterNumbers }) {
                        return@filter false
                    }
                }
                true
            }
            .sumOf { it[it.size / 2] }
    }

    fun part2(input: String): Int {
        val orderRules = input.lines()
            .takeWhile { it.isNotEmpty() }
            .map { rules -> rules.split("|").map { it.toInt() } }

        val pages = input.lines()
            .takeLastWhile { it.isNotEmpty() }
            .map { pages -> pages.split(",").map { it.toInt() } }

        return pages
            .mapNotNull { pageNumbers ->
                val foo = pageNumbers.toMutableList()
                var didChangeFoo = false
                var index = 0
                while (index < foo.size) {
                    var didChangeFooThisLoop = false
                    val pageNum = foo[index]
                    val mustBeLaterNumbers = orderRules.filter { it.first() == pageNum }.map { it.last() }
                    for (prevIndices in 0..<index) {
                        val prevValue = foo[prevIndices]
                        if (prevValue in mustBeLaterNumbers) {
                            foo.removeAt(prevIndices)
                            foo.add(index, prevValue)
                            didChangeFoo = true
                            didChangeFooThisLoop = true
                        }
                    }
                    if (didChangeFooThisLoop) {
                        index = 0
                    } else {
                        index++
                    }
                }
                if (didChangeFoo) foo else null
            }
            .sumOf { it[it.size / 2] }
    }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInputAsText("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day05.txt` file.
    val input = readInputAsText("Day05")
    println(part1(input))
    println(part2(input))
}
