import kotlin.math.min

private val numberMatcher = Regex("[0-9]+")

private const val DAY = 5


fun main() {
    fun part1(input: List<String>): Long {
        return input
            .drop(2)
            .split(String::isBlank)
            .fold(input.first().extractSeeds()) { acc, it ->
                val mappings = it.drop(1).map { it.extractMapping() }
                acc.map {
                    mappings
                        .firstOrNull { mapping -> it in mapping.source }
                        ?.map(it)
                        ?: it
                }
            }
            .min()
    }

    fun part2(input: List<String>): Long {
        val seeds = input.first().extractSeeds2()
        val mappings = input
            .drop(2)
            .split(String::isBlank)
            .map { it.drop(1).map { it.extractMapping() } }
        return seeds.map {
            it.fold(Long.MAX_VALUE) { minSeedMapping, seed ->
                val mapped = mappings.fold(seed) { mappedSeed, blockMapping ->
                    blockMapping
                        .firstOrNull { mapping -> mappedSeed in mapping.source }
                        ?.map(mappedSeed)
                        ?: mappedSeed
                }
                min(mapped, minSeedMapping)
            }
        }.min() //2254686
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput(DAY)
    part1(input).println()
    check(part2(input).also { it.println() } == 2254686L)
}

data class Mapping(
    val destination: LongRange,
    val source: LongRange,
)

fun Mapping.map(sourceNumber: Long) = sourceNumber - source.first + destination.first

fun String.extractSeeds() = removePrefix("seeds: ")
    .split(" ")
    .map(String::toLong)

fun String.extractSeeds2() = split(": ")
    .last()
    .split(" ")
    .map(String::toLong)
    .chunked(2)
    .map {
        it.first()..<it.first() + it.last()
    }


fun String.extractMapping(): Mapping {
    val (destination, source, length) = split(" ").map(String::toLong)
    return Mapping(destination..<destination + length, source..<source + length)
}

fun <T> List<T>.split(predicate: (T) -> Boolean) = foldIndexed(listOf(-1)) { index, indices, s ->
    if (predicate(s)) {
        indices + index
    } else {
        indices
    }
}.plus(size)
    .zipWithNext()
    .map { slice(it.first + 1..<it.second) }

fun <T> List<T>.splitIndexed(predicate: (Int, T) -> Boolean) = foldIndexed(listOf(-1)) { index, indices, s ->
    if (predicate(index, s)) {
        indices + index
    } else {
        indices
    }
}.plus(size)
    .zipWithNext()
    .map { slice(it.first + 1..<it.second) }
