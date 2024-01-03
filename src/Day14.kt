private const val DAY = 14

data class LoadState(
    val load: Int,
    val rollingStones: Int
)

fun main() {
    fun List<String>.transpose(): List<String> {
        val width = first().length
        assert(all { it.length == width })
        val strings = this
        return buildList<String> {
            for (w in 0..<width) {
                add(
                    buildString {
                        for (s in strings) append(s[w])
                    }
                )
            }

        }
    }

    fun part1(input: List<String>): Long {
        return input.reversed().transpose().map { row ->
            row.foldIndexed(LoadState(0, 0)) { index, state, position ->
                when (position) {
                    '.' -> state
                    'O' -> state.copy(rollingStones = state.rollingStones + 1)
                    '#' -> {
                        val load = state.load + (state.rollingStones * (2 * index + 1 - state.rollingStones)) / 2
                        LoadState(load, 0)
                    }

                    else -> state
                }
            }.let { state ->
                state.load + (state.rollingStones * (2 * row.length + 1 - state.rollingStones)) / 2
            }.toLong()
        }.sum()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readTestInput(DAY)
    check(part1(testInput) == 136L)
    check(part2(testInput) == 0L)

    val input = readInput(DAY)
    part1(input).println()
    part2(input).println()
}