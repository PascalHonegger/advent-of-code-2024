import kotlin.time.measureTime

fun day15() {
    fun Char.toDirection() = when (this) {
        '<' -> Pair(-1, 0)
        'v' -> Pair(0, 1)
        '>' -> Pair(1, 0)
        '^' -> Pair(0, -1)
        else -> error("Unknown this $this")
    }

    fun part1(input: List<String>): Int {
        val map = input.takeWhile { it.isNotEmpty() }.toPointMap().toMutableMap()
        val moves = input.takeLastWhile { it.isNotEmpty() }.joinToString("")
        for (move in moves) {
            val robotPosition = map.entries.single { it.value == '@' }.key
            val direction = move.toDirection()

            fun tryToMoveBox(boxPosition: Pair<Int, Int>): Boolean {
                val targetPosition = boxPosition + direction
                return when (val target = map.getValue(targetPosition)) {
                    '#' -> false
                    '.' -> {
                        map[targetPosition] = 'O'
                        true
                    }

                    'O' -> tryToMoveBox(targetPosition)
                    else -> error("Unknown target $target")
                }
            }

            val targetPosition = robotPosition + direction
            when (val target = map.getValue(targetPosition)) {
                '#' -> Unit
                '.' -> {
                    map[robotPosition] = '.'
                    map[targetPosition] = '@'
                }

                'O' -> {
                    if (tryToMoveBox(targetPosition)) {
                        map[robotPosition] = '.'
                        map[targetPosition] = '@'
                    }
                }

                else -> error("Unknown target $target")
            }
        }
        return map.entries.filter { it.value == 'O' }.sumOf { it.key.first + it.key.second * 100 }
    }

    fun part2(input: List<String>): Int {
        val map = input.takeWhile { it.isNotEmpty() }.map {
            it.toCharArray().joinToString("") { c ->
                when (c) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    '@' -> "@."
                    else -> error("Unknown c $c")
                }
            }
        }.toPointMap().toMutableMap()
        val moves = input.takeLastWhile { it.isNotEmpty() }.joinToString("")
        for (move in moves) {
            val robotPosition = map.entries.single { it.value == '@' }.key
            val direction = move.toDirection()

            fun canMoveBox(boxPositionLeft: Pair<Int, Int>, boxPositionRight: Pair<Int, Int>): Boolean {
                val targetPositionLeft = boxPositionLeft + direction
                val targetPositionRight = boxPositionRight + direction
                val targetLeft = if (direction.first == 1) '.' else map.getValue(targetPositionLeft)
                val targetRight = if (direction.first == -1) '.' else map.getValue(targetPositionRight)
                return when {
                    targetLeft == '#' || targetRight == '#' -> false
                    targetLeft == '[' && targetRight == ']' -> canMoveBox(targetPositionLeft, targetPositionRight)
                    targetLeft == ']' && !canMoveBox(targetPositionLeft + Pair(-1, 0), targetPositionLeft) -> false
                    targetRight == '[' && !canMoveBox(targetPositionRight, targetPositionRight + Pair(1, 0)) -> false
                    else -> true
                }
            }

            fun moveBox(boxPositionLeft: Pair<Int, Int>, boxPositionRight: Pair<Int, Int>) {
                val targetPositionLeft = boxPositionLeft + direction
                val targetPositionRight = boxPositionRight + direction
                val targetLeft = if (direction.first == 1) '.' else map.getValue(targetPositionLeft)
                val targetRight = if (direction.first == -1) '.' else map.getValue(targetPositionRight)
                if (targetLeft == '#' || targetRight == '#') error("Hit wall while moving box!")
                if (targetLeft == '[') {
                    check(targetRight == ']')
                    moveBox(targetPositionLeft, targetPositionRight)
                }
                if (targetLeft == ']') {
                    moveBox(targetPositionLeft + Pair(-1, 0), targetPositionLeft)
                }
                if (targetRight == '[') {
                    moveBox(targetPositionRight, targetPositionRight + Pair(1, 0))
                }
                map[boxPositionLeft] = '.'
                map[boxPositionRight] = '.'
                check(map[targetPositionLeft] == '.')
                check(map[targetPositionRight] == '.')
                map[targetPositionLeft] = '['
                map[targetPositionRight] = ']'
            }

            val targetPosition = robotPosition + direction
            when (val target = map.getValue(targetPosition)) {
                '#' -> Unit
                '.' -> {
                    map[robotPosition] = '.'
                    map[targetPosition] = '@'
                }

                '[' -> {
                    if (canMoveBox(targetPosition, targetPosition + Pair(1, 0))) {
                        moveBox(targetPosition, targetPosition + Pair(1, 0))
                        map[robotPosition] = '.'
                        map[targetPosition] = '@'
                    }
                }

                ']' -> {
                    if (canMoveBox(targetPosition + Pair(-1, 0), targetPosition)) {
                        moveBox(targetPosition + Pair(-1, 0), targetPosition)
                        map[robotPosition] = '.'
                        map[targetPosition] = '@'
                    }
                }

                else -> error("Unknown target $target")
            }
        }
        return map.entries.filter { it.value == '[' }.sumOf { it.key.first + it.key.second * 100 }
    }

    // Or read a large test input from the `src/Day15_test.txt` file:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    // Read the input from the `src/Day15.txt` file.
    val input = readInput("Day15")
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
