fun day06() {
    fun part1(input: String): Int {
        val visited = mutableSetOf<Position>()
        val obstacles = input.lineSequence().flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == '#') {
                    Position(x, y)
                } else null
            }
        }.filterNotNull().toSet()
        val validX = 0..<input.lineSequence().first().length
        val validY = 0..<input.lineSequence().count()
        check(validX == validY)

        var direction = Direction.UP
        var position = input.lineSequence().flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == '^') {
                    Position(x, y)
                } else null
            }
        }.filterNotNull().single()

        while (position.x in validX && position.y in validY) {
            visited += position
            val nextPosition = position + direction
            if (nextPosition in obstacles) {
                direction = direction.clockwise()
            } else {
                position = nextPosition
            }
        }

        return visited.size
    }

    fun part2(input: String): Int {
        val currentPath = mutableSetOf<Triple<Int, Int, Char>>()
        val obstacles = input.lineSequence().flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == '#') {
                    Pair(x, y)
                } else null
            }
        }.filterNotNull().toMutableSet()
        val validX = 0..<input.lineSequence().first().length
        val validY = 0..<input.lineSequence().count()
        check(validX == validY)

        var direction = Triple(0, -1, '^')
        val start = input.lineSequence().flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == '^') {
                    Triple(x, y, '^')
                } else null
            }
        }.filterNotNull().single()

        fun nextDirection() = when (direction) {
            Triple(0, -1, '^') -> Triple(1, 0, '>')
            Triple(1, 0, '>') -> Triple(0, 1, 'v')
            Triple(0, 1, 'v') -> Triple(-1, 0, '<')
            Triple(-1, 0, '<') -> Triple(0, -1, '^')
            else -> error("Unsupported direction")
        }

        var position = start
        fun simulatePath() {
            currentPath.clear()
            position = start
            direction = Triple(0, -1, '^')

            fun nextPosition() =
                Triple(position.first + direction.first, position.second + direction.second, position.third)

            while (position.first in validX && position.second in validY && position !in currentPath) {
                currentPath += position
                val nextPos = nextPosition()
                if (Pair(nextPos.first, nextPos.second) in obstacles) {
                    direction = nextDirection()
                    position = Triple(position.first, position.second, direction.third)
                } else {
                    position = nextPos
                }
            }
        }
        simulatePath()

        return currentPath
            .map {
                when (it.third) {
                    '^' -> Pair(it.first, it.second - 1)
                    'v' -> Pair(it.first, it.second + 1)
                    '<' -> Pair(it.first - 1, it.second)
                    '>' -> Pair(it.first + 1, it.second)
                    else -> error("Unknown direction")
                }
            }
            .distinct()
            .filter { it != Pair(start.first, start.second) && it.first in validX && it.second in validY && it !in obstacles }
            .count { newObstacle ->
                obstacles += newObstacle
                simulatePath()
                var foundCycle = position in currentPath
                obstacles -= newObstacle
                return@count foundCycle
            }
    }

    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInputAsText("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day06.txt` file.
    val input = readInputAsText("Day06")
    println(part1(input))
    println(part2(input))
}
