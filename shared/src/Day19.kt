import kotlin.time.measureTime

fun day19() {
    fun String.countPossibilities(towels: Trie<Unit>): Long {
        val cache = mutableMapOf<Pair<Int, Trie.Node<Unit>>, Long>()
        fun canBeBuilt(index: Int, node: Trie.Node<Unit>): Long {
            val cacheKey = index to node
            cache[cacheKey]?.let { return it }
            if (index !in indices) return if (node.value != null) 1 else 0
            val matchingChild = node.children[this[index]]
            if (matchingChild == null) return 0
            // Can be built ignoring this value
            val countIgnoringValue = canBeBuilt(index + 1, matchingChild)
            // Can be built when using this value
            val countWithValue = if (matchingChild.value != null) canBeBuilt(index + 1, towels.root) else 0
            val result = countIgnoringValue + countWithValue
            cache[cacheKey] = result
            return result
        }
        return canBeBuilt(0, towels.root)
    }

    fun String.canBeBuiltWith(towels: List<String>): Boolean {
        val regex = towels.joinToString("|") { "($it)" }.let { "^($it)+$" }.toRegex()
        return this matches regex
    }

    fun part1(input: List<String>): Int {
        val towels = input.first().split(", ")
        val designs = input.drop(2)
        val towelsTrie = Trie<Unit>()
        towels.forEach { towelsTrie.insert(it, Unit) }
        return designs.count { it.canBeBuiltWith(towels) }
    }

    fun part2(input: List<String>): Long {
        val towels = input.first().split(", ")
        val designs = input.drop(2)
        val towelsTrie = Trie<Unit>()
        towels.forEach { towelsTrie.insert(it, Unit) }
        return designs.sumOf { it.countPossibilities(towelsTrie) }
    }

    // Or read a large test input from the `src/Day19_test.txt` file:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    // Read the input from the `src/Day19.txt` file.
    val input = readInput("Day19")
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
