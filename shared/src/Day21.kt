import kotlin.time.measureTime

fun day21() {
    /* +---+---+---+
     * | 7 | 8 | 9 |
     * +---+---+---+
     * | 4 | 5 | 6 |
     * +---+---+---+
     * | 1 | 2 | 3 |
     * +---+---+---+
     *     | 0 | A |
     *     +---+---+ */
    val numberKeypad = mapOf(
        '7' to Position(0, 0),
        '8' to Position(1, 0),
        '9' to Position(2, 0),
        '4' to Position(0, 1),
        '5' to Position(1, 1),
        '6' to Position(2, 1),
        '1' to Position(0, 2),
        '2' to Position(1, 2),
        '3' to Position(2, 2),
        'X' to Position(0, 3),
        '0' to Position(1, 3),
        'A' to Position(2, 3),
    )

    /*     +---+---+
     *     | ^ | A |
     * +---+---+---+
     * | < | v | > |
     * +---+---+---+ */
    val directionalKeypad = mapOf(
        'X' to Position(0, 0),
        '^' to Position(1, 0),
        'A' to Position(2, 0),
        '<' to Position(0, 1),
        'v' to Position(1, 1),
        '>' to Position(2, 1),
    )

    fun expand(pos: Position, char: Char, keypad: Map<Char, Position> = directionalKeypad): Pair<Position, String> {
        val forbidden = keypad.getValue('X')
        val moveTo = keypad.getValue(char)
        val sequence = buildString {
            if (Position(moveTo.x, pos.y) == forbidden) {
                repeat(moveTo.y - pos.y) { append(Direction.DOWN) }
                repeat(pos.y - moveTo.y) { append(Direction.UP) }
                repeat(moveTo.x - pos.x) { append(Direction.RIGHT) }
                repeat(pos.x - moveTo.x) { append(Direction.LEFT) }
            } else if (Position(pos.x, moveTo.y) == forbidden) {
                repeat(pos.x - moveTo.x) { append(Direction.LEFT) }
                repeat(moveTo.x - pos.x) { append(Direction.RIGHT) }
                repeat(moveTo.y - pos.y) { append(Direction.DOWN) }
                repeat(pos.y - moveTo.y) { append(Direction.UP) }
            } else {
                repeat(pos.x - moveTo.x) { append(Direction.LEFT) }
                repeat(moveTo.y - pos.y) { append(Direction.DOWN) }
                repeat(pos.y - moveTo.y) { append(Direction.UP) }
                repeat(moveTo.x - pos.x) { append(Direction.RIGHT) }
            }
            append('A')
        }
        return moveTo to sequence
    }

    fun simulateRobots(sequences: List<String>, indirections: Int): Long {
        return sequences.sumOf { sequence ->
            var positions = mutableListOf(numberKeypad.getValue('A'))
            positions += List(indirections) { directionalKeypad.getValue('A') }
            val cache = mutableMapOf<Triple<Int, Char, Position>, Pair<Long, Position>>()

            fun nextStep(depth: Int, char: Char): Long {
                val cacheKey = Triple(depth, char, positions[depth])
                cache[cacheKey]?.let { (value, nextPosition) ->
                    positions[depth] = nextPosition
                    return value
                }
                val (nextPosition, deeperParts) = expand(positions[depth], char)
                positions[depth] = nextPosition
                val result =
                    if (depth == indirections) deeperParts.length.toLong()
                    else deeperParts.sumOf { nextStep(depth + 1, it) }
                cache[cacheKey] = Pair(result, nextPosition)
                return result
            }

            sequence.sumOf { char ->
                val (newNumberPos, numberSequence) = expand(positions.first(), char, numberKeypad)
                positions[0] = newNumberPos
                numberSequence.sumOf { nextStep(1, it) }
            } * sequence.takeWhile { it.isDigit() }.toLong()
        }
    }

    // Or read a large test input from the `src/Day21_test.txt` file:
    val testInput = readInput("Day21_test")

    check(simulateRobots(testInput, 2) == 126384L)
    check(simulateRobots(testInput, 3) == 310188L)
    check(simulateRobots(testInput, 4) == 757754L)
    check(simulateRobots(testInput, 5) == 1881090L)
    check(simulateRobots(testInput, 6) == 4656624L)
    check(simulateRobots(testInput, 7) == 11592556L)
    check(simulateRobots(testInput, 8) == 28805408L)
    check(simulateRobots(testInput, 9) == 71674912L)
    check(simulateRobots(testInput, 10) == 178268300L)
    check(simulateRobots(testInput, 11) == 443466162L)
    check(simulateRobots(testInput, 12) == 1103192296L)
    check(simulateRobots(testInput, 13) == 2744236806L)
    check(simulateRobots(testInput, 14) == 6826789418L)
    check(simulateRobots(testInput, 15) == 16982210284L)
    check(simulateRobots(testInput, 16) == 42245768606L)
    check(simulateRobots(testInput, 17) == 105091166058L)
    check(simulateRobots(testInput, 18) == 261427931594L)
    check(simulateRobots(testInput, 19) == 650334539256L)
    check(simulateRobots(testInput, 20) == 1617788558680L)
    check(simulateRobots(testInput, 21) == 4024453458310L)
    check(simulateRobots(testInput, 22) == 10011330575914L)
    check(simulateRobots(testInput, 23) == 24904446930002L)
    check(simulateRobots(testInput, 24) == 61952932092390L)
    check(simulateRobots(testInput, 25) == 154115708116294L)

    // Read the input from the `src/Day21.txt` file.
    val input = readInput("Day21")
    measureTime {
        println(simulateRobots(input, 2))
    }.also { println("Part1 took $it") }
    measureTime {
        println(simulateRobots(input, 25))
    }.also { println("Part2 took $it") }
}
