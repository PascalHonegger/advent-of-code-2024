import kotlinx.io.buffered
import kotlinx.io.bytestring.decodeToString
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteString

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> {
    return readInputAsText(name).lines()
}

/**
 * Reads text from the given input txt file
 */
fun readInputAsText(name: String): String {
    return SystemFileSystem.source(Path("../inputs/$name.txt"))
        .buffered()
        .readByteString()
        .decodeToString()
        .trim()
}

/**
 * Calculate product by multiplying all elements together
 */
fun Iterable<Int>.product() = reduce(Int::times)

/**
 * Calculate product by multiplying all elements together
 */
fun Iterable<Long>.product() = reduce(Long::times)


/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/** Converts "1 2  3" to [1, 2, 3] */
fun String.asSpaceSeparatedInts() = split(" ").filter { it.isNotBlank() }.map { it.toInt() }
fun String.asSpaceSeparatedLongs() = split(" ").filter { it.isNotBlank() }.map { it.toLong() }

inline fun <reified T> MutableMap<T, Int>.increase(key: T, by: Int = 1): Int {
    val existingValue = this[key] ?: 0
    val newValue = existingValue + by
    this.put(key, newValue)
    return newValue
}

inline fun <reified T> List<T>.repeated(n: Int) = buildList<T> {
    repeat(n) {
        addAll(this@repeated)
    }
}

/**
 * Taken from https://www.baeldung.com/kotlin/lcm
 */
fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

/**
 * Safe transpose a list of unequal-length lists.
 *
 * Example:
 * transpose(List(List(1, 2, 3), List(4, 5, 6), List(7, 8)))
 * -> List(List(1, 4, 7), List(2, 5, 8), List(3, 6))
 *
 * Inspired by https://gist.github.com/clementgarbay/49288c006252955c2a3c6139a61ca92a
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    // Helpers
    fun <E> List<E>.head(): E = this.first()
    fun <E> List<E>.tail(): List<E> = this.takeLast(this.size - 1)
    fun <E> E.append(xs: List<E>): List<E> = listOf(this).plus(xs)

    return filter { it.isNotEmpty() }.let { ys ->
        if (ys.isNotEmpty()) {
            ys.map { it.head() }.append(ys.map { it.tail() }.transpose())
        } else {
            emptyList()
        }
    }
}

fun List<String>.get2D(x: Int, y: Int): Char? {
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            return row[x]
        }
    }
    return null
}

data class Position(val x: Int, val y: Int)

data class Direction(val x: Int, val y: Int) {

    override fun toString(): String = when (this) {
        LEFT -> "<"
        DOWN -> "v"
        RIGHT -> ">"
        UP -> "^"
        else -> "($x, $y)"
    }

    companion object {
        val UP = Direction(0, -1)
        val DOWN = Direction(0, 1)
        val LEFT = Direction(-1, 0)
        val RIGHT = Direction(1, 0)

        fun fromChar(value: Char): Direction = when (value) {
            '<' -> LEFT
            'v' -> DOWN
            '>' -> RIGHT
            '^' -> UP
            else -> throw IllegalArgumentException("Unknown direction: $value")
        }
    }
}

fun Direction.clockwise() = when (this) {
    Direction.UP -> Direction.RIGHT
    Direction.RIGHT -> Direction.DOWN
    Direction.DOWN -> Direction.LEFT
    Direction.LEFT -> Direction.UP
    else -> error("Unsupported direction to rotate clockwise: $this")
}

fun Direction.counterclockwise() = when (this) {
    Direction.UP -> Direction.LEFT
    Direction.LEFT -> Direction.DOWN
    Direction.DOWN -> Direction.RIGHT
    Direction.RIGHT -> Direction.UP
    else -> error("Unsupported direction to rotate counterclockwise: $this")
}

typealias PointMap<T> = Map<Position, T>

fun List<String>.toPointMap(): PointMap<Char> = flatMapIndexed { y, row ->
    row.mapIndexed { x, c ->
        Position(x, y) to c
    }
}.toMap()

fun PointMap<Char>.printMap() {
    for (y in 0..maxOf { it.key.y }) {
        for (x in 0..maxOf { it.key.x }) {
            print(get(Position(x, y)))
        }
        kotlin.io.println()
    }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int, sizeToSwap: Int = 1) {
    require(sizeToSwap > 0)
    repeat(sizeToSwap) {
        val tmp = this[index1 + it]
        this[index1 + it] = this[index2 + it]
        this[index2 + it] = tmp
    }
}

inline fun <T> Iterable<T>.sumOfIndexed(transform: (index: Int, T) -> Int) = mapIndexed(transform).sum()

operator fun Position.plus(direction: Direction): Position =
    Position(x = x + direction.x, y = y + direction.y)

val IntRange.simpleSize get() = last - first + 1
