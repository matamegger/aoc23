private const val DAY = 12

fun main() {
    fun String.isPureDefect() = matches("#+".toRegex())
    fun String.isPureUnknown() = matches("""\?+""".toRegex())

    fun test(group: String, questionMarks: List<Int>, arrangementNumber: Int, numbers: List<Int>): Boolean {
        val fingerPrint = arrangementNumber.toString(2).padStart(questionMarks.size, '0')

        val defectGroups = buildString {
            append(group)
            questionMarks.forEachIndexed { index, qIndex ->
                replace(qIndex, qIndex + 1, if (fingerPrint[index] == '1') "." else "#")
            }
        }.split(".")
            .filter { it.isNotBlank() }
        if (defectGroups.size != numbers.size) return false
        return defectGroups.zip(numbers).all { it.first.length == it.second }
    }

    fun bruteForce(group: String, numbers: List<Int>): Long {
        val questionMarks = group.withIndex().filter { (i, c) -> c == '?' }.map { it.index }
        val variations = 1 shl questionMarks.size
        val workingVariations = (0..<variations).count { arrangementNumber ->
            test(group, questionMarks, arrangementNumber, numbers)
        }
        return workingVariations.toLong()
    }

    fun part1(input: List<String>): Long {
        return input.map {
            val (row, numbers) = it.split(" ")
            row.split(".").filter(String::isNotBlank) to numbers.split(",").map(String::toInt)
        }
            .map {
                val group = it.first.joinToString(".")
                bruteForce(group, it.second)
            }
            .sum()
    }

    fun <T> List<T>.repeat(times: Int): List<T> = (1..<times).fold(this) { acc, i ->
        acc + this
    }

    fun part2(input: List<String>): Long {
        return input.map {
            val (row, numbers) = it.split(" ")
            ("$row?").repeat(4).plus(row).split(".").filter(String::isNotBlank) to numbers.split(",").map(String::toInt)
                .repeat(5)
        }
            .map {
                val group = it.first.joinToString(".")
                bruteForce(group, it.second)
            }
            .sum()
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 21L)
//    check(part2(testInput) == 525152L)

    val input = readInput(DAY)
    part1(input).println() //==7025
//    part2(input).println()
}