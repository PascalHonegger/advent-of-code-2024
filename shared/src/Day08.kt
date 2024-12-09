import kotlin.time.measureTime

fun day08() {
    fun part1(rows: List<String>): Int {
        check(rows.size == rows.first().length)
        val validIndex = rows.indices
        val antennas = rows.flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                Triple(x, y, c)
            }
        }.filter { it.third != '.' }.groupBy(keySelector = { it.third }, valueTransform = { Pair(it.first, it.second) })
        val antinodes = antennas.flatMapTo(mutableSetOf()) { (frequency, points) ->
            points.flatMapIndexed { index, p ->
                points
                    .filter { otherPoint ->
                        otherPoint !== p
                    }.map { otherPoint ->
                        Pair(
                            otherPoint.first - (p.first - otherPoint.first),
                            otherPoint.second - (p.second - otherPoint.second)
                        )
                    }
            }
        }
        return antinodes.count { it.first in validIndex && it.second in validIndex }
    }

    fun part2(rows: List<String>): Int {
        check(rows.size == rows.first().length)
        val validIndex = rows.indices
        val antennas = rows.flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                Triple(x, y, c)
            }
        }.filter { it.third != '.' }.groupBy(keySelector = { it.third }, valueTransform = { Pair(it.first, it.second) })
        val antinodes = antennas.flatMapTo(mutableSetOf()) { (frequency, points) ->
            points.flatMapIndexed { index, p ->
                points
                    .filter { otherPoint ->
                        otherPoint !== p
                    }
                    .flatMap { otherPoint ->
                        sequence {
                            val xDiff = p.first - otherPoint.first
                            val yDiff = p.second - otherPoint.second
                            var x = otherPoint.first - xDiff
                            var y = otherPoint.second - yDiff
                            while (x in validIndex && y in validIndex) {
                                yield(Pair(x, y))
                                x -= xDiff
                                y -= yDiff
                            }
                            x = otherPoint.first + xDiff
                            y = otherPoint.second + yDiff
                            while (x in validIndex && y in validIndex) {
                                yield(Pair(x, y))
                                x += xDiff
                                y += yDiff
                            }
                        }
                    }
            }
        }
        println(antinodes.size)
        return antinodes.size
    }

    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
