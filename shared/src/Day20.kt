import kotlin.time.measureTime

fun day20() {
    fun calculateDistanceMap(maze: PointMap<Char>): MutableMap<Position, Int> {
        val start = maze.entries.single { it.value == 'S' }.key
        val end = maze.entries.single { it.value == 'E' }.key

        val tempMap = maze.toMutableMap()
        val distanceFromEndMap = mutableMapOf<Position, Int>()
        var distanceFromEnd = 0
        var pos = end
        while (pos != start) {
            distanceFromEndMap[pos] = distanceFromEnd
            tempMap[pos] = '#'
            pos = Direction.ALL.map { pos + it }.single { tempMap[it] != '#' }
            distanceFromEnd++
        }
        distanceFromEndMap[start] = distanceFromEnd
        return distanceFromEndMap
    }

    fun part1(maze: PointMap<Char>, minCheatCodeSaving: Int): Int {
        val distanceMap = calculateDistanceMap(maze)

        return distanceMap.entries.sumOf { (key, value) ->
            val cheatedMins = Direction.ALL.mapNotNull { distanceMap[key + it + it] }
            cheatedMins.count { (value - 2 - it) >= minCheatCodeSaving }
        }
    }

    fun part2(maze: PointMap<Char>, minCheatCodeSaving: Int): Int {
        val distanceMap = calculateDistanceMap(maze)

        return distanceMap.entries.sumOf { (key, value) ->
            val cheatedMins = distanceMap.filter { it.key.distanceTo(key) <= 20 }
            cheatedMins.count { (value - it.key.distanceTo(key) - it.value) >= minCheatCodeSaving }
        }
    }

    // Or read a large test input from the `src/Day20_test.txt` file:
    val testInput = readInput("Day20_test").toPointMap()
    check(part1(testInput, 65) == 0)
    check(part1(testInput, 64) == 1)
    check(part1(testInput, 40) == 2)
    check(part2(testInput, 77) == 0)
    check(part2(testInput, 76) == 3)
    check(part2(testInput, 74) == 7)

    // Read the input from the `src/Day20.txt` file.
    val input = readInput("Day20").toPointMap()
    measureTime {
        println(part1(input, 100))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input, 100))
    }.also { println("Part2 took $it") }
}
