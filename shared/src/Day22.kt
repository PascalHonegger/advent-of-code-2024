import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.math.max
import kotlin.time.measureTime

private inline fun calculateSecret(num: Long, depth: Int, callback: (intermediate: Long) -> Unit = {}): Long {
    var secret = num
    callback(secret)
    repeat(depth) {
        secret = (secret xor (secret shl 6)) and 0b111111111111111111111111L
        secret = (secret xor (secret shr 5)) and 0b111111111111111111111111L
        secret = (secret xor (secret shl 11)) and 0b111111111111111111111111L
        callback(secret)
    }
    return secret
}

@OptIn(DelicateCoroutinesApi::class)
fun day22() {
    fun part1(buyers: List<Long>): Long = buyers.sumOf { calculateSecret(it, 2000) }

    fun part2(buyers: List<Long>): Long {
        val prices = buyers.map { initialSecret ->
            val lastDigits = mutableListOf<Int>()
            calculateSecret(initialSecret, 2000) {
                lastDigits.add(abs(it.mod(10L)).toInt())
            }
            lastDigits
        }
        val changeRates = prices.map { it.zipWithNext().map { (a, b) -> b - a } }

        val noMatch = listOf(-1, -1, -1, -1)

        val tasks = mutableListOf<Deferred<Long>>()

        for (firstDelta in -9..9) {
            for (secondDelta in (-9 - firstDelta).coerceAtLeast(-9)..(9 + firstDelta).coerceAtMost(9)) {
                for (thirdDelta in (-9 - secondDelta).coerceAtLeast(-9)..(9 + secondDelta).coerceAtMost(9)) {
                    for (fourthDelta in (-9 - thirdDelta).coerceAtLeast(-9)..(9 + thirdDelta).coerceAtMost(9)) {
                        tasks += GlobalScope.async {
                            val matchingIndices = changeRates.map { rates ->
                                for (index in 0..(rates.size - 4)) {
                                    if (rates[index + 0] == firstDelta && rates[index + 1] == secondDelta && rates[index + 2] == thirdDelta && rates[index + 3] == fourthDelta) {
                                        return@map index
                                    }
                                }
                                -1
                            }
                            if (matchingIndices == noMatch) return@async 0L
                            return@async prices.zip(matchingIndices)
                                .filter { (_, index) -> index != -1 }
                                .sumOf { (price, index) -> price[index + 4].toLong() }
                        }
                    }
                }
            }
        }

        return runBlocking { tasks.awaitAll().max() }
    }

    check(calculateSecret(123L, 1) == 15887950L)
    check(calculateSecret(123L, 2) == 16495136L)
    check(calculateSecret(123L, 3) == 527345L)
    check(calculateSecret(123L, 4) == 704524L)
    check(calculateSecret(123L, 5) == 1553684L)
    check(calculateSecret(123L, 6) == 12683156L)
    check(calculateSecret(123L, 7) == 11100544L)
    check(calculateSecret(123L, 8) == 12249484L)
    check(calculateSecret(123L, 9) == 7753432L)
    check(calculateSecret(123L, 10) == 5908254L)
    val lastDigits = mutableListOf<Int>()
    calculateSecret(123L, 9) {
        lastDigits.add(abs(it.mod(10L)).toInt())
    }
    check(lastDigits.toString() == listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2).toString())
    check(
        lastDigits.zipWithNext().map { (a, b) -> b - a }.toString() == listOf(
            -3,
            6,
            -1,
            -1,
            0,
            2,
            -2,
            0,
            -2
        ).toString()
    )
    check(calculateSecret(1L, 2000) == 8685429L)
    check(calculateSecret(10L, 2000) == 4700978L)
    check(calculateSecret(100L, 2000) == 15273692L)
    check(calculateSecret(2024L, 2000) == 8667524L)

    // Or read a large test input from the `src/Day22_test.txt` file:
    val testInput = readInput("Day22_test").map { it.toLong() }
    val testInput2 = readInput("Day22_test2").map { it.toLong() }
    check(part1(testInput) == 37327623L)
    measureTime {
        check(part2(testInput2) == 23L)
    }.also { println("Part2 test took $it") }

    // Read the input from the `src/Day22.txt` file.
    val input = readInput("Day22").map { it.toLong() }
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        // TOO LOW :( 1571
        println(part2(input))
    }.also { println("Part2 took $it") }
}
