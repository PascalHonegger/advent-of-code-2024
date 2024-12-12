import kotlin.time.measureTime

fun day12() {
    fun Map<Pair<Int, Int>, Char>.forEachRegion(block: (region: Set<Pair<Int, Int>>) -> Unit) {
        val notYetVisited = toMutableMap()
        while (notYetVisited.isNotEmpty()) {
            val (start, plot) = notYetVisited.entries.first()
            val region = mutableSetOf<Pair<Int, Int>>()
            fun visit(pos: Pair<Int, Int>) {
                if (notYetVisited[pos] != plot) return
                notYetVisited.remove(pos)
                region += pos
                visit(pos + Pair(1, 0))
                visit(pos + Pair(-1, 0))
                visit(pos + Pair(0, 1))
                visit(pos + Pair(0, -1))
            }
            visit(start)
            block(region)
        }
    }

    fun part1(garden: Map<Pair<Int, Int>, Char>): Long {
        var runningSum = 0L
        garden.forEachRegion { region ->
            val area = region.size
            val perimeter = region.count { !region.contains(it + Pair(1, 0)) } +
                    region.count { !region.contains(it + Pair(-1, 0)) } +
                    region.count { !region.contains(it + Pair(0, 1)) } +
                    region.count { !region.contains(it + Pair(0, -1)) }

            runningSum += area * perimeter
        }
        return runningSum
    }

    fun part2(garden: Map<Pair<Int, Int>, Char>): Long {
        var runningSum = 0L
        val xRange = 0..garden.maxOf { it.key.first }
        val yRange = 0..garden.maxOf { it.key.second }
        garden.forEachRegion { region ->
            val area = region.size
            fun countSides(directionToCheck: Pair<Int, Int>, scanTopDown: Boolean): Long {
                var count = 0L
                for (x in xRange) {
                    for (y in yRange) {
                        val point = Pair(x, y)
                        if (point !in region) continue
                        val hasRelevantWall = (point + directionToCheck) !in region
                        val hasAdjacentWall = if (scanTopDown) {
                            (point + Pair(0, -1)) in region &&
                            (point + Pair(0, -1) + directionToCheck) !in region
                        } else {
                            (point + Pair(-1, 0)) in region &&
                            (point + Pair(-1, 0) + directionToCheck) !in region
                        }
                        if (hasRelevantWall && !hasAdjacentWall) count++
                    }
                }
                return count
            }

            val numberOfSides = countSides(Pair(1, 0), true) +
                    countSides(Pair(-1, 0), true) +
                    countSides(Pair(0, 1), false) +
                    countSides(Pair(0, -1), false)

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
