private const val DAY = 1

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.extractCalibrationValue(
                { first { it.isDigit() }.digitToInt() },
                { last { it.isDigit() }.digitToInt() }
            )
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            line.extractCalibrationValue(
                { getDigit() },
                { getDigit(true) }
            )
        }
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 142)
    val testInputPart2 = readTestInput(DAY, 2)
    check(part2(testInputPart2) == 281)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}

private fun String.extractCalibrationValue(firstDigit: String.() -> Int, secondDigit: String.() -> Int): Int {
    return firstDigit(this) * 10 + secondDigit(this)
}

private fun String.getDigit(fromEnd: Boolean = false): Int {
    val indices = if (fromEnd) indices.reversed() else indices
    for (index in indices) {
        if (this[index].isDigit()) return this[index].digitToInt()

        val digit = 1 + digitsAsWord.indexOfFirst { digitWord ->
            startsWith(digitWord, index)
        }
        if (digit > 0) return digit
    }
    error("No number found")
}


private val digitsAsWord = listOf(
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine"
)

private val digitsAsWordWithDigit = digitsAsWord.mapIndexed { index, it -> it to (index + 1).toString() }
