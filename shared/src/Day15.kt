import kotlin.time.measureTime

fun day15() {
    fun part1(input: List<String>): Int {
        val map = input.takeWhile { it.isNotEmpty() }.toPointMap().toMutableMap()
        val moves = input.takeLastWhile { it.isNotEmpty() }.joinToString("")
        for (move in moves) {
            val robotPosition = map.entries.single { it.value == '@' }.key
            val direction = Direction.fromChar(move)

            fun tryToMoveBox(boxPosition: Position): Boolean {
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
        return map.entries.filter { it.value == 'O' }.sumOf { it.key.x + it.key.y * 100 }
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
            val direction = Direction.fromChar(move)

            fun canMoveBox(boxPositionLeft: Position, boxPositionRight: Position): Boolean {
                val targetPositionLeft = boxPositionLeft + direction
                val targetPositionRight = boxPositionRight + direction
                val targetLeft = if (direction.x == 1) '.' else map.getValue(targetPositionLeft)
                val targetRight = if (direction.x == -1) '.' else map.getValue(targetPositionRight)
                return when {
                    targetLeft == '#' || targetRight == '#' -> false
                    targetLeft == '[' && targetRight == ']' -> canMoveBox(targetPositionLeft, targetPositionRight)
                    targetLeft == ']' && !canMoveBox(targetPositionLeft + Direction.LEFT, targetPositionLeft) -> false
                    targetRight == '[' && !canMoveBox(targetPositionRight, targetPositionRight + Direction.RIGHT) -> false
                    else -> true
                }
            }

            fun moveBox(boxPositionLeft: Position, boxPositionRight: Position) {
                val targetPositionLeft = boxPositionLeft + direction
                val targetPositionRight = boxPositionRight + direction
                val targetLeft = if (direction.x == 1) '.' else map.getValue(targetPositionLeft)
                val targetRight = if (direction.x == -1) '.' else map.getValue(targetPositionRight)
                if (targetLeft == '#' || targetRight == '#') error("Hit wall while moving box!")
                if (targetLeft == '[') {
                    check(targetRight == ']')
                    moveBox(targetPositionLeft, targetPositionRight)
                }
                if (targetLeft == ']') {
                    moveBox(targetPositionLeft + Direction.LEFT, targetPositionLeft)
                }
                if (targetRight == '[') {
                    moveBox(targetPositionRight, targetPositionRight + Direction.RIGHT)
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
                    if (canMoveBox(targetPosition, targetPosition + Direction.RIGHT)) {
                        moveBox(targetPosition, targetPosition + Direction.RIGHT)
                        map[robotPosition] = '.'
                        map[targetPosition] = '@'
                    }
                }

                ']' -> {
                    if (canMoveBox(targetPosition + Direction.LEFT, targetPosition)) {
                        moveBox(targetPosition + Direction.LEFT, targetPosition)
                        map[robotPosition] = '.'
                        map[targetPosition] = '@'
                    }
                }

                else -> error("Unknown target $target")
            }
        }
        return map.entries.filter { it.value == '[' }.sumOf { it.key.x + it.key.y * 100 }
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
