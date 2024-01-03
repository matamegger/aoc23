private const val DAY = 9


fun main() {

    tailrec fun List<Long>.getNextNumber(offset: Long = 0): Long {
//        println(this.joinToString() + " -> " + offset.toString())
        val differences = zipWithNext { a, b -> b - a }
        if (differences.all { it == 0L }) {
            return offset + last()
        }
        return differences.getNextNumber(offset + last())
    }

    fun part1(input: List<String>): Long {
        return input.map {
            it.split(" ")
                .map(String::toLong)
                .getNextNumber()
        }.sum()
    }

    fun part2(input: List<String>): Long {
        return input.map {
            it.split(" ")
                .map(String::toLong)
                .reversed()
                .getNextNumber()
        }.sum()
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}
