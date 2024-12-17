import kotlin.time.measureTime

fun day14() {
    data class Robot(var position: Position, val velocity: Direction)

    val robotPattern = "p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)".toRegex()

    fun String.toRobot(): Robot {
        val values = robotPattern.matchEntire(this)!!.groupValues.drop(1).map { it.toInt() }
        return Robot(
            position = Position(values[0], values[1]),
            velocity = Direction(values[2], values[3]),
        )
    }

    fun Robot.moveWithin(space: Pair<Int, Int>) {
        position += velocity
        if (position.x < 0) {
            position = position.copy(x = space.first + position.x)
        }
        if (position.y < 0) {
            position = position.copy(y = space.second + position.y)
        }
        if (position.x >= space.first) {
            position = position.copy(x = position.x - space.first)
        }
        if (position.y >= space.second) {
            position = position.copy(y = position.y - space.second)
        }
    }

    fun List<Robot>.printWithin(seconds: Int, space: Pair<Int, Int>) {
        val map = groupingBy { it.position }.eachCount()
        val string = buildString {
            repeat(space.second) { y ->
                repeat(space.first) { x ->
                    append(if (map.containsKey(Position(x, y))) "x" else " ")
                }
                appendLine()
            }
            appendLine()
        }
        // Hacky way to "detect" a Christmas tree
        if ("xxxxxxxxxx" in string) {
            println("$seconds seconds")
            println(string)
            readln()
        }

    }

    fun part1(robots: List<Robot>, space: Pair<Int, Int>, iterations: Int): Int {
        repeat(iterations) {
            for (robot in robots) {
                robot.moveWithin(space)
            }
        }
        val horizontalSplit = space.first / 2
        val verticalSplit = space.second / 2
        return listOf(
            robots.count { it.position.x < horizontalSplit && it.position.y < verticalSplit },
            robots.count { it.position.x < horizontalSplit && it.position.y > verticalSplit },
            robots.count { it.position.x > horizontalSplit && it.position.y < verticalSplit },
            robots.count { it.position.x > horizontalSplit && it.position.y > verticalSplit },
        ).product()
    }

    fun part2(robots: List<Robot>, space: Pair<Int, Int>): Int {
        var seconds = 0
        while(true) {
            seconds++
            for (robot in robots) {
                robot.moveWithin(space)
            }
            robots.printWithin(seconds, space)
        }
        return seconds
    }

    // Or read a large test input from the `src/Day14_test.txt` file:
    val testInput = readInput("Day14_test").map { it.toRobot() }
    check(part1(testInput, Pair(11, 7), 100) == 12)

    // Read the input from the `src/Day14.txt` file.
    val input = readInput("Day14").map { it.toRobot() }
    measureTime {
        println(part1(input, Pair(101, 103), 100))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(readInput("Day14").map { it.toRobot() }, Pair(101, 103)))
    }.also { println("Part2 took $it") }
}
