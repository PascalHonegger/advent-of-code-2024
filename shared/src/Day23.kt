import kotlin.time.measureTime

fun day23() {
    data class Computer(val name: String) {
        val connectedTo = mutableSetOf<Computer>()
    }

    fun List<String>.toComputers(): Collection<Computer> {
        val computers = mutableMapOf<String, Computer>()
        for (line in this) {
            val (c1, c2) = line.split('-')
            if (c1 !in computers) computers[c1] = Computer(c1)
            if (c2 !in computers) computers[c2] = Computer(c2)
        }
        for (line in this) {
            val (c1, c2) = line.split('-').map { computers.getValue(it) }
            c1.connectedTo += c2
            c2.connectedTo += c1
        }
        return computers.values
    }

    fun countCliques(start: Computer, size: Int): Set<Set<Computer>> {
        val cliques = mutableSetOf<Set<Computer>>()
        val history = mutableSetOf<Computer>(start)
        fun findCliques(computer: Computer) {
            if (!history.add(computer)) return
            if (history.size == size) {
                if (history.all { it.connectedTo.intersect(history).size == size - 1 })
                    cliques += history.toSet()
            } else {
                computer.connectedTo.forEach(::findCliques)
            }
            history.remove(computer)
        }
        start.connectedTo.forEach(::findCliques)
        return cliques
    }

    fun part1(input: List<String>): Int {
        return input.toComputers().filter { it.name.startsWith('t') }
            .flatMapTo(mutableSetOf()) { countCliques(it, 3) }.size
    }

    fun part2(input: List<String>): String {
        var maxClique = emptySet<Computer>()

        fun bronKerbosch(r: Set<Computer>, p: MutableSet<Computer>, x: MutableSet<Computer>) {
            if (p.isEmpty() && x.isEmpty()) {
                // Report R as a maximal clique
                if (r.size > maxClique.size) {
                    maxClique = r
                }
                return
            }

            for (v in p.toList()) { // Iterate over a copy to avoid modification issues
                bronKerbosch(
                    r + v,
                    p.intersect(v.connectedTo).toMutableSet(),
                    x.intersect(v.connectedTo).toMutableSet()
                )
                p -= v
                x += v
            }
        }

        bronKerbosch(emptySet(), input.toComputers().toMutableSet(), mutableSetOf())
        return maxClique.map { it.name }.sorted().joinToString(",")
    }

    // Or read a large test input from the `src/Day23_test.txt` file:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    // Read the input from the `src/Day23.txt` file.
    val input = readInput("Day23")
    measureTime {
        println(part1(input))
    }.also { println("Part1 took $it") }
    measureTime {
        println(part2(input))
    }.also { println("Part2 took $it") }
}
