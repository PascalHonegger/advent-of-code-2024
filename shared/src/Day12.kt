import kotlin.time.measureTime

fun day12() {
    fun PointMap<Char>.forEachRegion(block: (region: Set<Position>) -> Unit) {
        val notYetVisited = toMutableMap()
        while (notYetVisited.isNotEmpty()) {
            val (start, plot) = notYetVisited.entries.first()
            val region = mutableSetOf<Position>()
            fun visit(pos: Position) {
                if (notYetVisited[pos] != plot) return
                notYetVisited.remove(pos)
                region += pos
                visit(pos + Direction.UP)
                visit(pos + Direction.RIGHT)
                visit(pos + Direction.DOWN)
                visit(pos + Direction.LEFT)
            }
            visit(start)
            block(region)
        }
    }

    fun part1(garden: PointMap<Char>): Long {
        var runningSum = 0L
        garden.forEachRegion { region ->
            val area = region.size
            val perimeter = region.count { (it + Direction.UP) !in region } +
                    region.count { (it + Direction.RIGHT) !in region } +
                    region.count { (it + Direction.DOWN) !in region } +
                    region.count { (it + Direction.LEFT) !in region }

            runningSum += area * perimeter
        }
        return runningSum
    }

    fun part2(garden: PointMap<Char>): Long {
        var runningSum = 0L
        val xRange = 0..garden.maxOf { it.key.x }
        val yRange = 0..garden.maxOf { it.key.y }
        garden.forEachRegion { region ->
            val area = region.size
            fun countSides(directionToCheck: Direction, scanTopDown: Boolean): Long {
                var count = 0L
                if (scanTopDown) {
                    for (x in xRange) {
                        var hadRelevantWall = false
                        for (y in yRange) {
                            val point = Position(x, y)
                            if (point !in region) {
                                hadRelevantWall = false
                                continue
                            }
                            val hasRelevantWall = (point + directionToCheck) !in region
                            if (hasRelevantWall && !hadRelevantWall) count++
                            hadRelevantWall = hasRelevantWall
                        }
                    }
                } else {
                    for (y in yRange) {
                        var hadRelevantWall = false
                        for (x in xRange) {
                            val point = Position(x, y)
                            if (point !in region) {
                                hadRelevantWall = false
                                continue
                            }
                            val hasRelevantWall = (point + directionToCheck) !in region
                            if (hasRelevantWall && !hadRelevantWall) count++
                            hadRelevantWall = hasRelevantWall
                        }
                    }
                }
                return count
            }

            val numberOfSides = countSides(Direction.LEFT, true) +
                    countSides(Direction.RIGHT, true) +
                    countSides(Direction.UP, false) +
                    countSides(Direction.DOWN, false)

            runningSum += area * numberOfSides
        }
        return runningSum
    }

    // Or read a large test input from the `src/Day12_test.txt` file:
    val test1Input = readInput("Day12_test1").toPointMap()
    val test2Input = readInput("Day12_test2").toPointMap()
    val test3Input = readInput("Day12_test3").toPointMap()
    val test4Input = readInput("Day12_test4").toPointMap()
    val test5Input = readInput("Day12_test5").toPointMap()
    check(part1(test1Input) == 140L)
    check(part1(test2Input) == 772L)
    check(part1(test3Input) == 1930L)
    check(part2(test1Input) == 80L)
    check(part2(test2Input) == 436L)
    check(part2(test3Input) == 1206L)
    check(part2(test4Input) == 236L)
    check(part2(test5Input) == 368L)

    // Read the input from the `src/Day12.txt` file.
    val input = readInput("Day12").toPointMap()
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
