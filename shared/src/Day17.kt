import kotlin.time.measureTime

private enum class ComboOperand {
    Literal0,
    Literal1,
    Literal2,
    Literal3,
    RegisterA,
    RegisterB,
    RegisterC,
    Reserved,
}

private enum class Operation {
    Adv,
    Bxl,
    Bst,
    Jnz,
    Bxc,
    Out,
    Bdv,
    Cdv,
}

fun day17() {
    data class Computer(
        var registerA: Long = 0,
        var registerB: Long = 0,
        var registerC: Long = 0,
        var instructionPointer: Int = 0,
        val output: MutableList<Int> = mutableListOf<Int>(),
        val debug: Boolean = false,
    ) {

        init {
            if (debug) println("Initialized $this")
        }

        private fun asCombo(operand: Int): Int = when (ComboOperand.entries.first { it.ordinal == operand }) {
            ComboOperand.Literal0 -> 0
            ComboOperand.Literal1 -> 1
            ComboOperand.Literal2 -> 2
            ComboOperand.Literal3 -> 3
            ComboOperand.RegisterA -> registerA.toInt()
            ComboOperand.RegisterB -> registerB.toInt()
            ComboOperand.RegisterC -> registerC.toInt()
            ComboOperand.Reserved -> TODO()
        } and 0b111

        fun execute(instructions: List<Int>) {
            if (debug) println("Executing instructions")
            while (instructionPointer in instructions.indices) {
                val operation = Operation.entries.first { it.ordinal == instructions[instructionPointer] }
                val operand = instructions[instructionPointer + 1]
                instructionPointer += 2
                execute(operation, operand)
            }
        }

        private fun execute(operation: Operation, operand: Int) {
            if (debug) println("Operation $operation with operand $operand")
            when (operation) {
                Operation.Adv -> registerA = registerA shr asCombo(operand)
                Operation.Bdv -> registerB = registerA shr asCombo(operand)
                Operation.Cdv -> registerC = registerA shr asCombo(operand)
                Operation.Bxl -> registerB = registerB xor operand.toLong()
                Operation.Bxc -> registerB = registerB xor registerC
                Operation.Bst -> registerB = asCombo(operand).toLong()
                Operation.Jnz -> if (registerA != 0L) instructionPointer = operand
                Operation.Out -> output += asCombo(operand)
            }
            if (debug) println(toString())
        }
    }

    fun List<String>.parseInput(): Pair<Computer, List<Int>> {
        val computer = Computer(
            registerA = get(0).removePrefix("Register A: ").toLong(),
            registerB = get(1).removePrefix("Register B: ").toLong(),
            registerC = get(2).removePrefix("Register C: ").toLong(),
        )
        val instructions = get(4).removePrefix("Program: ").split(",").map { it.toInt() }
        return computer to instructions
    }

    Computer(registerC = 9).run {
        execute(listOf(2, 6))
        check(registerB == 1L)
    }

    Computer(registerA = 10).run {
        execute(listOf(5, 0, 5, 1, 5, 4))
        check(output == listOf(0, 1, 2))
    }

    Computer(registerA = 2024).run {
        execute(listOf(0, 1, 5, 4, 3, 0))
        check(registerA == 0L)
        check(output == listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0))
    }

    Computer(registerB = 29).run {
        execute(listOf(1, 7))
        check(registerB == 26L)
    }

    Computer(registerB = 2024, registerC = 43690).run {
        execute(listOf(4, 0))
        check(registerB == 44354L)
    }

    Computer(registerA = 117440).run {
        execute(listOf(0, 3, 5, 4, 3, 0))
        check(output == listOf(0, 3, 5, 4, 3, 0))
    }

    fun part2(instructions: List<Int>): Long {
        var input = instructions.map { 1 }.toMutableList()

        fun runComputer(input: List<Int>): List<Int> {
            var binaryInput = 0L
            for ((index, part) in input.withIndex()) {
                binaryInput = binaryInput xor (part.toLong() shl (3 * index))
            }
            Computer(registerA = binaryInput, debug = false).run {
                execute(instructions)
                return output
            }
        }

        fun bruteForce(index: Int = input.lastIndex): Boolean {
            if (index == -1) return true
            while (input[index] != 8) {
                val output = runComputer(input)
                if (output[index] == instructions[index]) {
                    if (bruteForce(index - 1)) {
                        return true
                    }
                }
                input[index]++
            }
            input[index] = 1
            return false
        }
        bruteForce()

        var binaryInput = 0L
        for ((index, part) in input.withIndex()) {
            binaryInput = binaryInput xor (part.toLong() shl (3 * index))
        }
        return binaryInput
    }

    // Or read a large test input from the `src/Day17_test.txt` file:
    val (testComputer, testInstructions) = readInput("Day17_test").parseInput()
    testComputer.execute(testInstructions)
    check(testComputer.output == listOf(4, 6, 3, 5, 6, 3, 5, 2, 1, 0))

    // Read the input from the `src/Day17.txt` file.
    measureTime {
        val (computer, instructions) = readInput("Day17").parseInput()
        computer.execute(instructions)
        println(computer.output.joinToString(","))
    }.also { println("Part1 took $it") }

    measureTime {
        val (_, instructions) = readInput("Day17").parseInput()
        println(part2(instructions))
    }.also { println("Part2 took $it") }
}
