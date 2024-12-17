import kotlin.time.measureTime

fun day16() {
    fun part1(maze: PointMap<Char>): Int {
        val start = maze.entries.single { it.value == 'S' }.key
        val end = maze.entries.single { it.value == 'E' }.key

        val distances = dijkstraWeights(Pair(start, Direction.RIGHT)) { (position, currentDirection) ->
            sequence {
                val possibleDirections = when (currentDirection) {
                    Direction.UP -> listOf(
                        Direction.UP to Weight(1),
                        Direction.LEFT to Weight(1001),
                        Direction.RIGHT to Weight(1001),
                    )

                    Direction.DOWN -> listOf(
                        Direction.DOWN to Weight(1),
                        Direction.LEFT to Weight(1001),
                        Direction.RIGHT to Weight(1001),
                    )

                    Direction.LEFT -> listOf(
                        Direction.UP to Weight(1001),
                        Direction.DOWN to Weight(1001),
                        Direction.LEFT to Weight(1),
                    )

                    Direction.RIGHT -> listOf(
                        Direction.UP to Weight(1001),
                        Direction.DOWN to Weight(1001),
                        Direction.RIGHT to Weight(1),
                    )

                    else -> error("Unexpected $currentDirection")
                }
                for ((direction, weight) in possibleDirections) {
                    val target = position + direction
                    if (maze[target] != '#') {
                        yield(Pair(target, direction) to weight)
                    }
                }
            }
        }

        return distances.entries.filter { it.key.first == end }.minOf { it.value.value }
    }

    fun part2(maze: PointMap<Char>): Int {
        val start = maze.entries.single { it.value == 'S' }.key
        val end = maze.entries.single { it.value == 'E' }.key

        val (distances, paths) = dijkstraPaths(Pair(start, Direction.RIGHT)) { (position, currentDirection) ->
            sequence {
                val possibleDirections = when (currentDirection) {
                    Direction.UP -> listOf(
                        Direction.UP to Weight(1),
                        Direction.LEFT to Weight(1001),
                        Direction.RIGHT to Weight(1001),
                    )

                    Direction.DOWN -> listOf(
                        Direction.DOWN to Weight(1),
                        Direction.LEFT to Weight(1001),
                        Direction.RIGHT to Weight(1001),
                    )

                    Direction.LEFT -> listOf(
                        Direction.UP to Weight(1001),
                        Direction.DOWN to Weight(1001),
                        Direction.LEFT to Weight(1),
                    )

                    Direction.RIGHT -> listOf(
                        Direction.UP to Weight(1001),
                        Direction.DOWN to Weight(1001),
                        Direction.RIGHT to Weight(1),
                    )

                    else -> error("Unexpected $currentDirection")
                }
                for ((direction, weight) in possibleDirections) {
                    val target = position + direction
                    if (maze[target] != '#') {
                        yield(Pair(target, direction) to weight)
                    }
                }
            }
        }

        val minDistance = distances.entries.filter { it.key.first == end }.minOf { it.value.value }
        val minDistanceDirections = distances.entries.filter { it.key.first == end && it.value.value == minDistance }.map { it.key.second }
        return paths.entries
            .filter { it.key.first == end  && it.key.second in minDistanceDirections}
            .flatMap {
                sequence {
                    val workingSet = it.value.toMutableList()
                    while (workingSet.isNotEmpty()) {
                        val curr = workingSet.removeFirst()
                        yield(curr.first)
                        workingSet += paths.getValue(curr)
                    }
                }
            }
            .let { it + start + end }
            .distinct()
            .size
    }

    // Or read a large test input from the `src/Day16_test.txt` file:
    val testInput1 = readInput("Day16_test1").toPointMap()
    val testInput2 = readInput("Day16_test2").toPointMap()
    check(part1(testInput1) == 7036)
    check(part1(testInput2) == 11048)
    check(part2(testInput1) == 45)
    check(part2(testInput2) == 64)

    // Read the input from the `src/Day16.txt` file.
    val input = readInput("Day16").toPointMap()
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
