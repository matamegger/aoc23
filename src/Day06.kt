import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

private val numberMatcher = Regex("[0-9]+")

private const val DAY = 6


fun main() {
    fun String.extractNumbers(): List<Int> = substringAfter(": ")
        .split(" ")
        .filter(String::isNotBlank)
        .map(String::toInt)

    fun String.extractNumber(): Long = substringAfter(": ")
        .split(" ")
        .filter(String::isNotBlank)
        .joinToString(separator = "")
        .toLong()


    fun part1(input: List<String>): Int {
        val (times, distances) = input.let {
            if (it.size != 2) error("Invalid count of input lines")
            it.map { it.extractNumbers() }
        }
        fun distance(totalTime: Int, buttonTime: Int) = totalTime * buttonTime - buttonTime * buttonTime
        return times.zip(distances)
            .map { (time, distance) ->
                val halfTime = time / 2.0
                val tmp = sqrt(halfTime * halfTime - distance)
                val minButtonTime = (halfTime - tmp).let {
                    if (it == ceil(it)) ceil(it + 0.1).toInt() else ceil(it).toInt()
                }
                val maxButtonTime = (halfTime + tmp).let {
                    if (it == ceil(it)) floor(it - 0.1).toInt() else floor(it).toInt()
                }
                minButtonTime..maxButtonTime
            }
            .map { it.last - it.first + 1 }
            .reduce(Int::times)
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = input.let {
            if (it.size != 2) error("Invalid count of input lines")
            it.map { it.extractNumber() }
        }
        val halfTime = time / 2.0
        val tmp = sqrt(halfTime * halfTime - distance)
        val minButtonTime = (halfTime - tmp).let {
            if (it == ceil(it)) ceil(it + 0.1).toInt() else ceil(it).toInt()
        }
        val maxButtonTime = (halfTime + tmp).let {
            if (it == ceil(it)) floor(it - 0.1).toInt() else floor(it).toInt()
        }
        val buttonTimeRange = minButtonTime..maxButtonTime
        return buttonTimeRange.last - buttonTimeRange.first + 1L
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}