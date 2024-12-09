import kotlin.time.measureTime

fun day09() {
    fun part1(diskMap: String): Long {
        val blocks = buildList {
            diskMap.forEachIndexed { index, c ->
                val char = if (index % 2 == 0) {
                    (index / 2).toString()
                } else {
                    "."
                }
                repeat(c.digitToInt()) {
                    add(char)
                }
            }
        }.toMutableList()

        var currentFreeIndex = blocks.indexOfFirst { it == "." }
        var currentDataIndex = blocks.lastIndex
        while (currentDataIndex > currentFreeIndex) {
            blocks.swap(currentDataIndex, currentFreeIndex)
            do {
                currentFreeIndex++
            } while (blocks[currentFreeIndex] != ".")
            do {
                currentDataIndex--
            } while (blocks[currentDataIndex] == ".")
        }

        return blocks.filter { it != "." }.mapIndexed { index, c -> index * c.toLong() }.sum()
    }

    fun part2(diskMap: String): Long {
        val blocks = buildList {
            diskMap.forEachIndexed { index, c ->
                val char = if (index % 2 == 0) {
                    (index / 2).toString()
                } else {
                    "."
                }
                repeat(c.digitToInt()) {
                    add(char)
                }
            }
        }.toMutableList()

        var currentDataBlock = blocks.last()
        do {
            var currentDataIndexStart = blocks.indexOfFirst { it == currentDataBlock }
            var currentDataIndexStop = blocks.indexOfLast { it == currentDataBlock }
            val dataSize = (currentDataIndexStop - currentDataIndexStart) + 1

            var currentFreeIndexStart = 0
            var currentFreeIndexStop = 0

            while (currentFreeIndexStart < currentDataIndexStart) {
                while (currentFreeIndexStart < currentDataIndexStart && blocks[currentFreeIndexStart] != ".") {
                    currentFreeIndexStart++
                }
                if (blocks[currentFreeIndexStart] != ".") break

                currentFreeIndexStop = currentFreeIndexStart
                while (blocks[currentFreeIndexStop + 1] == "."){
                    currentFreeIndexStop++
                }

                val freeSize = (currentFreeIndexStop - currentFreeIndexStart) + 1
                if (freeSize >= dataSize) {
                    blocks.swap(currentDataIndexStart, currentFreeIndexStart, dataSize)
                    break
                }
                currentFreeIndexStart = currentFreeIndexStop + 1
            }

            currentFreeIndexStart = 0
            currentDataBlock = (currentDataBlock.toInt() - 1).toString()
        } while (currentDataBlock != "0")

        return blocks.withIndex().filter { it.value != "." }.sumOf { (index, c) -> index * c.toLong() }
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInputAsText("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day09.txt` file.
    val input = readInputAsText("Day09")
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
