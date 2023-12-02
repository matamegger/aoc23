fun main() {
    val input = readInput(day = 1)
    val part1Sum = input.sumOf { line ->
        line.extractCalibrationValue(
            { first { it.isDigit() }.digitToInt() },
            { last { it.isDigit() }.digitToInt() }
        )
    }

    println("The sum of the calibration values for part 1 is: $part1Sum")

    val part2Sum =
        input.sumOf { line ->
            line.extractCalibrationValue(
                { getDigit() },
                { getDigit(true) }
            )
        }

    println("The sum of the calibration values for part 2 is: $part2Sum")
}

private fun String.extractCalibrationValue(firstDigit: String.() -> Int, secondDigit: String.() -> Int): Int {
    return firstDigit(this) * 10 + last { it.isDigit() }.digitToInt()
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
