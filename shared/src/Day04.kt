fun day04() {
    fun part1(wordGrid: List<String>): Int {
        return wordGrid.mapIndexed { y, row ->
            row.mapIndexed { x, col ->
                if (col != 'X') return@mapIndexed emptyList()

                fun getXmas(stepX: Int, stepY: Int): String {
                    return listOfNotNull(
                        col,
                        wordGrid.get2D(x + stepX, y + stepY),
                        wordGrid.get2D(x + 2 * stepX, y + 2 * stepY),
                        wordGrid.get2D(x + 3 * stepX, y + 3 * stepY),
                    ).joinToString("")
                }

                listOf(
                    getXmas(1, 0),
                    getXmas(-1, 0),
                    getXmas(0, 1),
                    getXmas(0, -1),
                    getXmas(1, 1),
                    getXmas(1, -1),
                    getXmas(-1, 1),
                    getXmas(-1, -1),
                )
            }
        }.flatten().flatten().count { it == "XMAS" }
    }

    fun part2(wordGrid: List<String>): Int {
        return wordGrid.mapIndexed { y, row ->
            row.mapIndexed { x, col ->
                if (col != 'A') return@mapIndexed emptyList()

                fun getWord(stepX: Int, stepY: Int): String {
                    return listOfNotNull(
                        wordGrid.get2D(x - stepX, y - stepY),
                        col,
                        wordGrid.get2D(x + stepX, y + stepY),
                    ).joinToString("")
                }

                listOf(
                    getWord(1, 1),
                    getWord(1, -1),
                    getWord(-1, 1),
                    getWord(-1, -1),
                )
            }
        }.flatten().count { words -> words.filter { it == "MAS" || it == "SAM" }.size == 4 }
    }

    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
