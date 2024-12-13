import kotlin.math.roundToLong
import kotlin.time.measureTime


val Pair<Long, Long>.sum: Long
    get() = first + second

fun day13() {
    data class Game(val aButton: Pair<Long, Long>, val bButton: Pair<Long, Long>, val prize: Pair<Long, Long>)

    operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> = Pair(first + other.first, second + other.second)

    val aRegex = "Button A: X\\+(\\d+), Y\\+(\\d+)".toRegex()
    val bRegex = "Button B: X\\+(\\d+), Y\\+(\\d+)".toRegex()
    val prizeRegex = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

    fun List<String>.toGame(): Game {
        check(size == 3)
        val (aButtonX, aButtonY) = aRegex.matchEntire(this[0])!!.groupValues.drop(1).map { it.toLong() }
        val (bButtonX, bButtonY) = bRegex.matchEntire(this[1])!!.groupValues.drop(1).map { it.toLong() }
        val (prizeX, prizeY) = prizeRegex.matchEntire(this[2])!!.groupValues.drop(1).map { it.toLong() }
        return Game(
            aButton = Pair(aButtonX, aButtonY),
            bButton = Pair(bButtonX, bButtonY),
            prize = Pair(prizeX, prizeY),
        )
    }

    fun part1(games: List<Game>): Long {
        var runningSum = 0L
        for (game in games) {
            /** solve for a, b
             * ===
             * game.prize.first = a * game.aButton.first + b * game.bButton.first
             * game.prize.second = a * game.aButton.second + b * game.bButton.second
             * ===
             * game.prize.first + game.prize.second = a * (game.aButton.first + game.aButton.second) + b * (game.bButton.first + game.bButton.second)
             * a = (game.prize.first + game.prize.second - b * (game.bButton.first + game.bButton.second)) / (game.aButton.first + game.aButton.second)
             * ===
             * game.prize.first = ((game.prize.first + game.prize.second - b * (game.bButton.first + game.bButton.second)) / (game.aButton.first + game.aButton.second)) * game.aButton.first + b * game.bButton.first
             * 0 = ((game.prize.sum - b * (game.bButton.sum)) / (game.aButton.sum)) * game.aButton.first + b * game.bButton.first - game.prize.first
             * 0 = ((game.prize.sum * game.aButton.first - b * game.aButton.first * game.bButton.sum) / game.aButton.sum) + b * game.bButton.first - game.prize.first
             * 0 = ((game.prize.sum * game.aButton.first / game.aButton.sum) - (b * game.aButton.first * game.bButton.sum) / game.aButton.sum) + b * game.bButton.first - game.prize.first
             * b * (game.aButton.first * game.bButton.sum) / game.aButton.sum) - game.bButton.first) = ((game.prize.sum * game.aButton.first / game.aButton.sum) - game.prize.first
             * b  = (((game.prize.sum * game.aButton.first / game.aButton.sum) - game.prize.first) / ((game.aButton.first * game.bButton.sum) / game.aButton.sum) - game.bButton.first)
             */
            val b = (((game.prize.sum.toDouble() * game.aButton.first.toDouble() / game.aButton.sum.toDouble()) - game.prize.first.toDouble()) / (((game.aButton.first.toDouble() * game.bButton.sum.toDouble()) / game.aButton.sum.toDouble()) - game.bButton.first.toDouble())).roundToLong()
            val a = ((game.prize.sum.toDouble() - b.toDouble() * game.bButton.sum.toDouble()) / game.aButton.sum.toDouble()).roundToLong()
            val finalX = a * game.aButton.first + b * game.bButton.first
            val finalY = a * game.aButton.second + b * game.bButton.second
            val final = Pair(finalX, finalY)
            if (final == game.prize) {
                runningSum += 3 * a + b
            }
        }
        return runningSum
    }

    fun part2(games: List<Game>): Long {
        val offset = Pair(10000000000000L, 10000000000000L)
        return part1(games.map { game ->
            Game(
                aButton = game.aButton,
                bButton = game.bButton,
                prize = game.prize + offset,
            )
        })
    }

    // Or read a large test input from the `src/Day13_test.txt` file:
    val testInput = readInput("Day13_test").filter { it.isNotEmpty() }.chunked(3).map { it.toGame() }
    check(part1(testInput) == 480L)
    // check(part2(testInput) == 80L)

    // Read the input from the `src/Day13.txt` file.
    val input = readInput("Day13").filter { it.isNotEmpty() }.chunked(3).map { it.toGame() }
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
