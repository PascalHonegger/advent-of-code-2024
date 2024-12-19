import kotlin.time.measureTime

fun day18() {
    fun part1(positions: List<Position>, gridSize: IntRange): Int {
        val start = Position(gridSize.first, gridSize.first)
        val end = Position(gridSize.last, gridSize.last)
        val wallLookup = positions.toSet()

        val distances = dijkstraWeights(start) { position ->
            sequence {
                for (direction in Direction.ALL) {
                    val target = position + direction
                    if (target.x in gridSize && target.y in gridSize && target !in wallLookup) {
                        yield(target to Weight(1))
                    }
                }
            }
        }

        return distances.getValue(end).value
    }

    fun part2(positions: List<Position>, gridSize: IntRange): Position {
        val start = Position(gridSize.first, gridSize.first)
        val end = Position(gridSize.last, gridSize.last)

        val wallLookup = positions.toMutableSet()
        for(pos in positions.reversed()) {
            wallLookup -= pos
            val hasPath = dijkstraWeights(start) { position ->
                sequence {
                    for (direction in Direction.ALL) {
                        val target = position + direction
                        if (target.x in gridSize && target.y in gridSize && target !in wallLookup) {
                            yield(target to Weight(1))
                        }
                    }
                }
            }.getValue(end).value != Int.MAX_VALUE

            if (hasPath) {
                return pos
            }
        }

        error("No blocked path detected!")
    }

    // Or read a large test input from the `src/Day18_test.txt` file:
    val testInput = readInput("Day18_test").map { it.toPosition() }
    check(part1(testInput.take(12), 0..6) == 22)
    check(part2(testInput, 0..6) == Position(6,1))

    // Read the input from the `src/Day18.txt` file.
    val input = readInput("Day18").map { it.toPosition() }
    measureTime {
        println(part1(input.take(1024), 0..70))
        println(part2(input, 0..70))
    }.also { println(it) }
}
